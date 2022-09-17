package com.astrainteractive.astraclans

import CommandManager
import com.astrainteractive.astraclans.commands.clan.ClanCommandController
import com.astrainteractive.astraclans.domain.DatabaseModule
import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.api.IPlayerStatusProvider
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astralibs.AstraLibs
import com.astrainteractive.astralibs.Logger
import com.astrainteractive.astralibs.ServerVersion
import com.astrainteractive.astralibs.events.GlobalEventManager
import com.astrainteractive.astralibs.utils.Injector.inject
import com.astrainteractive.astralibs.utils.Injector.remember
import com.astrainteractive.astraclans.events.EventHandler
import com.astrainteractive.astraclans.utils.*
import com.astrainteractive.astralibs.EmpireSerializer
import com.astrainteractive.astralibs.async.AsyncHelper
import github.scarsz.discordsrv.DiscordSRV
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*

/**
 * Initial class for your plugin
 */
class AstraClans : JavaPlugin() {
    companion object {
        lateinit var instance: AstraClans
    }

    init {
        instance = this
    }
    /**
     * Class for handling all of your events
     */
    private lateinit var eventHandler: EventHandler

    private var isStartedBefore = false
    private fun onInitialStart(block: () -> Unit) {
        if (isStartedBefore) return
        isStartedBefore = true
        block()

    }

    private fun <T> getPlugin(plugin: String): T? = Bukkit.getPluginManager().getPlugin(plugin) as? T?

    /**
     * This method called when server starts or PlugMan load plugin.
     */
    override fun onEnable() {
        AstraLibs.rememberPlugin(this)
        Logger.prefix = "AstraTemplate"
        PluginTranslation()
        _Files()

        DatabaseModule.createDatabase("${AstraLibs.instance.dataFolder}${File.separator}clans.db")

        eventHandler = EventHandler()
        _PluginConfig.create {
            EmpireSerializer.toClass<_PluginConfig>(Files.configFile)
        }
        onInitialStart {
            getPlugin<DiscordSRV>("DiscordSRV")?.let { discordSRV ->
                remember(DiscordController(discordSRV) { PluginConfig })
            }
            remember(ClanCommandController(inject()))
            CommandManager()

            setupClanPlayerStatusProvider()
        }
        Bukkit.getPluginManager().getPlugin("PlaceholderAPI")?.let {
            if (PapiExpansions.isRegistered) return@let
            PapiExpansions.register()
        }
        AsyncHelper.launch {
            ClanDataSource.selectAll().forEach(AstraClansAPI::rememberClan)
        }

    }

    private fun setupClanPlayerStatusProvider() {
        AstraClansAPI.playerStatusProvider = object : IPlayerStatusProvider {
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

    /**
     * This method called when server is shutting down or when PlugMan disable plugin.
     */
    override fun onDisable() {
        eventHandler.onDisable()
        HandlerList.unregisterAll(this)
        GlobalEventManager.onDisable()
        PapiExpansions.unregister()
    }

    /**
     * As it says, function for plugin reload
     */
    fun reloadPlugin() {
        onDisable()
        onEnable()
    }

}


