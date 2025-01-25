import com.netxters.bandalart.android.convention.Plugins
import com.netxters.bandalart.android.convention.applyPlugins
import com.netxters.bandalart.android.convention.implementation
import com.netxters.bandalart.android.convention.ksp
import com.netxters.bandalart.android.convention.libs
import org.gradle.kotlin.dsl.dependencies

internal class KotlinInjectConventionPlugin : BuildLogicConventionPlugin(
    {
        applyPlugins(Plugins.KSP)

        dependencies {
            implementation(libs.bundles.kotlin.inject)
            ksp(libs.kotlin.inject.compiler)
            ksp(libs.kotlin.inject.anvil.compiler)
        }
    }
)
