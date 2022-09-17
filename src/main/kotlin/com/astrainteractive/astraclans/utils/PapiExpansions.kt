package com.astrainteractive.astraclans.utils

import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer

object PapiExpansions : PlaceholderExpansion() {
    override fun getIdentifier(): String = "aclans"

    override fun getAuthor(): String = "RomanMakeev"

    override fun getVersion(): String = "1.0.0"
    override fun onRequest(player: OfflinePlayer?, params: String): String? {
        val default = { "" }
        player ?: return default()
        return when (params) {
            "clan" -> AstraClansAPI.getPlayerClan(player.toDTO())?.clanName?.let { "[$it${ChatColor.RESET}]" } ?: default()
            "clan_tag" -> AstraClansAPI.getPlayerClan(player.toDTO())?.clanTag?.let { "[$it${ChatColor.RESET}]" } ?: default()
            else -> default()
        }
    }
}