package com.astrainteractive.astraclans.di

import CommandManager
import com.astrainteractive.astraclans.commands.clan.ClanCommandController
import com.astrainteractive.astraclans.config.*
import com.astrainteractive.astraclans.config.config.ConfigProvider
import com.astrainteractive.astraclans.domain.config.IConfigProvider
import com.astrainteractive.astraclans.config.translation.PluginTranslation
import com.astrainteractive.astraclans.domain.DatabaseModule
import com.astrainteractive.astraclans.domain.api.IPlayerStatusProvider
import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.utils.DiscordController
import com.astrainteractive.astralibs.AstraLibs
import com.astrainteractive.astralibs.Logger
import com.astrainteractive.astralibs.utils.Injector
import com.astrainteractive.astralibs.utils.economy.IEconomyProvider
import com.astrainteractive.astralibs.utils.economy.VaultEconomyProvider
import github.scarsz.discordsrv.DiscordSRV
import org.bukkit.Bukkit
import java.io.File
import java.util.*

inline fun <reified T> getPlugin(plugin: String): T? = Bukkit.getPluginManager().getPlugin(plugin) as? T?

fun isPluginExists(plugin: String): Boolean = Bukkit.getPluginManager().getPlugin(plugin) != null

val fileModule = run {
    Injector.remember(Files())
}
val configModule = run {
    val files = Injector.inject<Files>()
    val initialConfig = ConfigProvider.loadPluginConfig(files.configFile)
    val configProvider = ConfigProvider(initialConfig)
    Injector.remember(configProvider as IConfigProvider)
}
val databaseModule = run {
    DatabaseModule.createDatabase("${AstraLibs.instance.dataFolder}${File.separator}clans.db")
//    Injector.remember(database)
}
val pluginTranslation = run {
    Injector.remember(PluginTranslation())
}

val discordSRV = run {
    getPlugin<DiscordSRV>("DiscordSRV")?.let { discordSRV ->
        Injector.remember(DiscordController(discordSRV, Injector.inject()))
    }?:Logger.warn("DiscordSRV not found")
}
val clanCommandController = run {
    val discordController: DiscordController? = Injector.inject()
    Injector.remember(ClanCommandController(discordController))
}
val commandManager = run {
    Injector.remember(CommandManager())
}
val economyProvider = run {
    if (isPluginExists("Vault")) {
        val vaultProvider = VaultEconomyProvider.also {
            it.onEnable()
        }
        Injector.remember(vaultProvider as IEconomyProvider)
    }
    else
        Logger.warn("Vault not found")
}
val playerStatusProvider = run {
    object : IPlayerStatusProvider {
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
    }
}