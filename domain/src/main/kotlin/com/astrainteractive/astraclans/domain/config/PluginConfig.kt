package com.astrainteractive.astraclans.domain.config

import kotlinx.serialization.Serializable

@Serializable
data class PluginConfig(
    val discord: Discord = Discord(),
    val clan: Clan = Clan(),
    val economy: Economy = Economy()
) {
    @Serializable
    data class Economy(
        // Amount to buy discord channel
        val discordChannelPurchaseAmount: Int = 0,
        // Amount to buy ClanLeader role
        val discordLeaderRolePurchaseAmount: Int = 0,
        // Amount to create clan
        val clanCreatePurchaseAmount: Int = 0,
        // Amount to invite player into clan
        val clanInvitePurchaseAmount: Int = 0,
        // amount to join into clan
        val clanJoinPurchaseAmount: Int = 0,
        // amount to create a home
        val clanHomeCreatePurchaseAmount: Int = 0,
        // Amount to teleport to home
        val clanHomeTeleportPurchaseAmount: Int = 0,
    )

    @Serializable
    data class Clan(
        val protection: Protection = Protection()
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
        val maxClanNameLength: Int = 16,
        val maxClanTagLength: Int = 16,
        val clanChat: ClanChat? = null,
    ) {
        @Serializable
        data class ClanChat(
            // Category of clan channels
            val categoryID: String,
        )
    }
}