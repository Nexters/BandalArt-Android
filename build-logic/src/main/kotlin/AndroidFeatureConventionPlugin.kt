import com.netxters.bandalart.android.convention.Plugins.KOTLIN_PARCELIZE
import com.netxters.bandalart.android.convention.applyPlugins
import com.netxters.bandalart.android.convention.implementation
import com.netxters.bandalart.android.convention.libs
import com.netxters.bandalart.android.convention.project
import org.gradle.kotlin.dsl.dependencies

internal class AndroidFeatureConventionPlugin : BuildLogicConventionPlugin(
    {
        applyPlugins(
            "bandalart.android.library",
            "bandalart.android.library.compose",
            KOTLIN_PARCELIZE,
        )

        dependencies {
            implementation(project(path = ":core:common"))
            implementation(project(path = ":core:designsystem"))
            implementation(project(path = ":core:domain"))
            implementation(project(path = ":core:ui"))
            implementation(project(path = ":core:navigation"))

            implementation(libs.androidx.navigation.compose)
            implementation(libs.bundles.androidx.lifecycle)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)
        }
    },
)
