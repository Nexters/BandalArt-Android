// @formatter:off

@file:Suppress("UnstableApiUsage","unused")

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import internal.ApplicationConstants
import internal.androidExtensions
import internal.applyPlugins
import internal.configureAndroid
import internal.isAndroidProject
import internal.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.KotlinClosure2
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

private const val EXPLICIT_API = "-Xexplicit-api=strict"

internal abstract class BuildLogicPlugin(private val block: Project.() -> Unit) : Plugin<Project> {
  final override fun apply(project: Project) {
    with(project, block = block)
  }
}

internal class AndroidApplicationPlugin : BuildLogicPlugin({
  applyPlugins(Plugins.AndroidApplication, Plugins.KotlinAndroid)

  extensions.configure<BaseAppModuleExtension> {
    configureAndroid(this)

    defaultConfig {
      targetSdk = ApplicationConstants.TargetSdk
      versionCode = ApplicationConstants.VersionCode
      versionName = ApplicationConstants.VersionName
    }
  }
})

internal class AndroidLibraryPlugin : BuildLogicPlugin({
  applyPlugins(Plugins.AndroidLibrary, Plugins.KotlinAndroid)

  extensions.configure<LibraryExtension> {
    configureAndroid(this)

    defaultConfig.apply {
      targetSdk = ApplicationConstants.TargetSdk
    }
  }
})

internal class AndroidHiltPlugin : BuildLogicPlugin({
  applyPlugins(
    libs.findPlugin("android-hilt").get().get().pluginId,
    Plugins.Ksp,
  )

  dependencies.add("ksp", libs.findLibrary("android-hilt-compile").get())
  dependencies.add("implementation", libs.findLibrary("android-hilt-runtime").get())
})

internal class AndroidComposePlugin : BuildLogicPlugin({
  extensions.configure<com.android.build.gradle.BaseExtension> {
    buildFeatures.compose = true

    composeOptions {
      kotlinCompilerExtensionVersion = libs.findVersion("androidx-compose-compiler").get().toString()
    }
  }
})

internal class JvmKotlinPlugin : BuildLogicPlugin({
  applyPlugins(Plugins.JavaLibrary, Plugins.KotlinJvm)

  extensions.configure<JavaPluginExtension> {
    sourceCompatibility = ApplicationConstants.JavaVersion
    targetCompatibility = ApplicationConstants.JavaVersion
  }

  extensions.configure<KotlinProjectExtension> {
    jvmToolchain(ApplicationConstants.JavaVersionAsInt)
  }

  extensions.configure<SourceSetContainer> {
    getByName("main").java.srcDir("src/main/kotlin")
    getByName("test").java.srcDir("src/test/kotlin")
  }

  dependencies.add("detektPlugins", libs.findLibrary("detekt-plugin-formatting").get())
})

internal class KotlinExplicitApiPlugin : BuildLogicPlugin({
  tasks
    .matching { task ->
      task is KotlinCompile &&
        !task.name.contains("test", ignoreCase = true)
    }
    .configureEach {
      if (!project.hasProperty("kotlin.optOutExplicitApi")) {
        val kotlinCompile = this as KotlinCompile
        if (EXPLICIT_API !in kotlinCompile.kotlinOptions.freeCompilerArgs) {
          kotlinCompile.kotlinOptions.freeCompilerArgs += EXPLICIT_API
        }
      }
    }
})

internal class TestKotestPlugin : BuildLogicPlugin({
  useTestPlatformForTarget()
  dependencies.add("testImplementation", libs.findLibrary("test-kotest-framework").get())
})

// ref: https://kotest.io/docs/quickstart#test-framework
private fun Project.useTestPlatformForTarget() {
  fun AbstractTestTask.setTestConfiguration() {
    // https://stackoverflow.com/a/36178581/14299073
    outputs.upToDateWhen { false }
    testLogging {
      events = setOf(
        TestLogEvent.PASSED,
        TestLogEvent.SKIPPED,
        TestLogEvent.FAILED,
      )
    }
    afterSuite(
      KotlinClosure2<TestDescriptor, TestResult, Unit>({ desc, result ->
        if (desc.parent == null) { // will match the outermost suite
          val output = "Results: ${result.resultType} " +
            "(${result.testCount} tests, " +
            "${result.successfulTestCount} passed, " +
            "${result.failedTestCount} failed, " +
            "${result.skippedTestCount} skipped)"
          println(output)
        }
      })
    )
  }

  if (isAndroidProject) {
    androidExtensions.testOptions {
      unitTests.all { test ->
        test.useJUnitPlatform()

        if (!test.name.contains("debug", ignoreCase = true)) {
          test.enabled = false
        }
      }
    }
    tasks.withType<Test>().configureEach {
      setTestConfiguration()
    }
  } else {
    tasks.withType<Test>().configureEach {
      useJUnitPlatform()
      setTestConfiguration()
    }
  }
}
