import com.netxters.bandalart.android.convention.Plugins
import com.netxters.bandalart.android.convention.applyPlugins
import com.netxters.bandalart.android.convention.implementation
import com.netxters.bandalart.android.convention.ksp
import com.netxters.bandalart.android.convention.libs
import org.gradle.kotlin.dsl.dependencies

internal class AndroidHiltConventionPlugin : BuildLogicConventionPlugin(
    {
        applyPlugins(Plugins.HILT, Plugins.KSP)

        dependencies {
            implementation(libs.hilt.android)
            ksp(libs.hilt.android.compiler)
        }
    },
)
