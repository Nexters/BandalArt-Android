@file:Suppress("NOTHING_TO_INLINE", "ObjectPropertyName")

import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

inline fun PluginDependenciesSpec.bandalart(pluginId: String): PluginDependencySpec =
  id("bandalart.plugin.$pluginId")

inline fun PluginDependenciesSpec.android(pluginId: String): PluginDependencySpec =
  id("com.android.$pluginId")

val PluginDependenciesSpec.`kotlin-parcelize`: PluginDependencySpec
  inline get() = id("kotlin-parcelize")
