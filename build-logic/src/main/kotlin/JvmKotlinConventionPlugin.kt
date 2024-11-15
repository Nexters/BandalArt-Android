import com.netxters.bandalart.android.convention.Plugins
import com.netxters.bandalart.android.convention.ApplicationConfig
import com.netxters.bandalart.android.convention.applyPlugins
import com.netxters.bandalart.android.convention.detektPlugins
import com.netxters.bandalart.android.convention.libs
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

internal class JvmKotlinConventionPlugin : BuildLogicConventionPlugin({
    applyPlugins(Plugins.JAVA_LIBRARY, Plugins.KOTLIN_JVM)

    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = ApplicationConfig.JavaVersion
        targetCompatibility = ApplicationConfig.JavaVersion
    }

    extensions.configure<KotlinProjectExtension> {
        jvmToolchain(ApplicationConfig.JavaVersionAsInt)
    }

    dependencies {
        detektPlugins(libs.detekt.formatting)
    }
})
