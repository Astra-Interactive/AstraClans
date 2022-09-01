package com.astrainteractive.models

data class PluginConfig(
    val discord: Discord,
    val clan: Clan,
    val economy: Economy
) {
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

    data class Clan(
        val protection: Protection
    ) {
        data class Protection(
            // Max amount of setBase
            val maxHomes: Int = 1,
            // Delay for /base command
            val homeTeleportCD: Int = 5,
        )
    }

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
        data class ClanChat(
            // Category of clan channels
            val categoryID: String,
        )
    }
}