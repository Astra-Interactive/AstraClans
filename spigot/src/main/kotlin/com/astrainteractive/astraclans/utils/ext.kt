package com.astrainteractive.astraclans.utils

import org.bukkit.Bukkit

inline fun <reified T> getPlugin(plugin: String): T? = Bukkit.getPluginManager().getPlugin(plugin) as? T?

fun isPluginExists(plugin: String): Boolean = Bukkit.getPluginManager().getPlugin(plugin) != null
