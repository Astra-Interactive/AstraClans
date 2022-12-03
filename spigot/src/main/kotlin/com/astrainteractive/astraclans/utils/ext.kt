package com.astrainteractive.astraclans.utils

import com.astrainteractive.astraclans.modules.TranslationProvider
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

inline fun <reified T> getPlugin(plugin: String): T? = Bukkit.getPluginManager().getPlugin(plugin) as? T?

fun isPluginExists(plugin: String): Boolean = Bukkit.getPluginManager().getPlugin(plugin) != null

fun CommandSender.sendTranslationMessage(msg: PluginTranslation.() -> String) {
    msg(TranslationProvider.value).also(::sendMessage)
}
