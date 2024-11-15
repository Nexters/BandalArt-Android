import com.netxters.bandalart.android.convention.Plugins
import com.netxters.bandalart.android.convention.applyPlugins
import com.netxters.bandalart.android.convention.implementation
import com.netxters.bandalart.android.convention.libs
import org.gradle.kotlin.dsl.dependencies

internal class AndroidRetrofitConventionPlugin : BuildLogicConventionPlugin(
    {
        applyPlugins(Plugins.KOTLINX_SERIALIZATION)

        dependencies {
            implementation(libs.retrofit)
            implementation(libs.retrofit.kotlinx.serialization.converter)
            implementation(libs.okhttp.logging.interceptor)
            implementation(libs.kotlinx.serialization.json)
        }
    },
)
