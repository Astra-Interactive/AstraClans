package com.astrainteractive.astraclans.di

import CommandManager
import com.astrainteractive.astraclans.commands.clan.ClanCommandController
import com.astrainteractive.astraclans.config.PluginConfig
import com.astrainteractive.astraclans.domain.api.IPlayerStatusProvider
import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.utils.DiscordController
import com.astrainteractive.astralibs.utils.Injector
import com.astrainteractive.astralibs.utils.economy.IEconomyProvider
import com.astrainteractive.astralibs.utils.economy.VaultEconomyProvider
import github.scarsz.discordsrv.DiscordSRV
import github.scarsz.discordsrv.hooks.VaultHook
import org.bukkit.Bukkit
import java.util.*

inline fun <reified T> getPlugin(plugin: String): T? = Bukkit.getPluginManager().getPlugin(plugin) as? T?

fun isPluginExists(plugin: String): Boolean = Bukkit.getPluginManager().getPlugin(plugin) != null

val discordSRV = run {
    getPlugin<DiscordSRV>("DiscordSRV")?.let { discordSRV ->
        Injector.remember(DiscordController(discordSRV) { PluginConfig })
    }
}
val clanCommandController = run {
    val discordController: DiscordController? = Injector.inject()
    Injector.remember(ClanCommandController(discordController))
}
val commandManager = run {
    Injector.remember(CommandManager())
}
val economyProvider = run {
    if (isPluginExists("Vault"))
        Injector.remember(VaultEconomyProvider as IEconomyProvider)
}
val playerStatusProvider = run {
    Injector.remember(object : IPlayerStatusProvider {
        override fun isPlayerOnline(playerDTO: ClanMemberDTO): Boolean {
            return Bukkit.getPlayer(UUID.fromString(playerDTO.minecraftUUID))?.isOnline == true
        }

        override fun isAnyMemberOnline(clanDTO: ClanDTO): Boolean {
            val isMemberOnline = clanDTO.clanMember.any(::isPlayerOnline)
            val isLeaderOnline = isPlayerOnline(
                ClanMemberDTO(
                    clanID = clanDTO.id,
                    minecraftUUID = clanDTO.leaderUUID,
                    minecraftName = clanDTO.leaderName
                )
            )
            return isMemberOnline || isLeaderOnline
        }
    })
}