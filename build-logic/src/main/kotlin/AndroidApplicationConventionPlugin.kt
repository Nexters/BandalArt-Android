import com.android.build.api.dsl.ApplicationExtension
import com.netxters.bandalart.android.convention.Plugins
import com.netxters.bandalart.android.convention.applyPlugins
import com.netxters.bandalart.android.convention.configureAndroid
import com.netxters.bandalart.android.convention.libs
import org.gradle.kotlin.dsl.configure

internal class AndroidApplicationConventionPlugin : BuildLogicConventionPlugin(
    {
        applyPlugins(Plugins.ANDROID_APPLICATION, Plugins.KOTLIN_ANDROID)

        extensions.configure<ApplicationExtension> {
            configureAndroid(this)

            defaultConfig {
                targetSdk = libs.versions.targetSdk.get().toInt()
                versionCode = libs.versions.versionCode.get().toInt()
                versionName = libs.versions.versionName.get()
            }
        }
    },
)
