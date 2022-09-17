package com.astrainteractive.astraclans.utils

import kotlinx.serialization.Serializable

val PluginConfig: _PluginConfig
    get() = _PluginConfig.instance!!

@Serializable
data class _PluginConfig(
    val discord: Discord,
    val clan: Clan,
    val economy: Economy
) {
    companion object {
        var instance: _PluginConfig? = null
            private set

        fun create(creator: () -> _PluginConfig?): _PluginConfig {
            instance = creator()
            return instance!!
        }
    }

    @Serializable
    data class Economy(
        // Amount to buy discord channel
        val discordChannelPurchaseAmount: Int,
        // Amount to buy ClanLeader role
        val discordLeaderRolePurchaseAmount: Int,
        // Amount to create clan
        val clanCreatePurchaseAmount: Int,
        // Amount to invite player into clan
        val clanInvitePurchaseAmount: Int,
        // amount to join into clan
        val clanJoinPurchaseAmount: Int,
        // amount to create a home
        val clanHomeCreatePurchaseAmount: Int,
        // Amount to teleport to home
        val clanHomeTeleportPurchaseAmount: Int,
    )

    @Serializable
    data class Clan(
        val protection: Protection
    ) {
        @Serializable
        data class Protection(
            // Max amount of setBase
            val maxHomes: Int = 1,
            // Delay for /base command
            val homeTeleportCD: Int = 5,
        )
    }

    @Serializable
    data class Discord(
        // Does plugin need to integrate with discordSRV
        val discordSRV: Boolean = false,
        // ID of Clan Leader role in discord
        val leaderRole: String? = null,
        // Message from game to discord
        val gameToDiscordFormat: String = "%player%: %message%",
        // Message from Discord to Game
        val discordToGameFormat: String = "[D] %name%: %message%",
        val maxClanNameLength: Int,
        val maxClanTagLength: Int,
        val clanChat: ClanChat,
    ) {
        @Serializable
        data class ClanChat(
            // Category of clan channels
            val categoryID: String,
        )
    }
}