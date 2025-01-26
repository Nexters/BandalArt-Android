import com.netxters.bandalart.android.convention.Plugins
import com.netxters.bandalart.android.convention.api
import com.netxters.bandalart.android.convention.applyPlugins
import com.netxters.bandalart.android.convention.implementation
import com.netxters.bandalart.android.convention.ksp
import com.netxters.bandalart.android.convention.libs
import com.netxters.bandalart.android.convention.project
import org.gradle.kotlin.dsl.dependencies

internal class AndroidFeatureConventionPlugin : BuildLogicConventionPlugin(
    {
        applyPlugins(
            "bandalart.android.library",
            "bandalart.android.library.compose",
            "bandalart.kotlin.inject",
            Plugins.KOTLIN_PARCELIZE,
        )

        dependencies {
            implementation(project(path = ":core:common"))
            implementation(project(path = ":core:designsystem"))
            implementation(project(path = ":core:domain"))
            implementation(project(path = ":core:ui"))

            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.bundles.circuit)
            implementation(libs.bundles.kotlin.inject)
            api(libs.circuit.codegen.annotation)
            ksp(libs.circuit.codegen)
        }
    },
)
