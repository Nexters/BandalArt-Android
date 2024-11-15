package com.netxters.bandalart.android.convention

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? {
    return add("implementation", dependencyNotation)
}

fun DependencyHandler.ksp(dependencyNotation: Any): Dependency? {
    return add("ksp", dependencyNotation)
}

fun DependencyHandler.androidTestImplementation(dependencyNotation: Any): Dependency? {
    return add("androidTestImplementation", dependencyNotation)
}

fun DependencyHandler.debugImplementation(dependencyNotation: Any): Dependency? {
    return add("debugImplementation", dependencyNotation)
}

fun DependencyHandler.testImplementation(dependencyNotation: Any): Dependency? {
    return add("testImplementation", dependencyNotation)
}

fun DependencyHandler.coreLibraryDesugaring(dependencyNotation: Any): Dependency? {
    return add("coreLibraryDesugaring", dependencyNotation)
}

fun DependencyHandler.detektPlugins(dependencyNotation: Any): Dependency? {
    return add("detektPlugins", dependencyNotation)
}

fun DependencyHandler.compileOnly(dependencyNotation: Any): Dependency? {
    return add("compileOnly", dependencyNotation)
}

fun DependencyHandler.project(
    path: String,
    configuration: String? = null,
): Dependency {
    return project(
        mapOf(
            "path" to path,
            "configuration" to configuration,
        ).filterValues { it != null }
    )
}
