package com.astrainteractive.astraclans

import CommandManager
import com.astrainteractive.astraclans.commands.clan.ClanCommandController
import com.astrainteractive.astraclans.di.*
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
import com.astrainteractive.astralibs.utils.Injector
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
            discordSRV
            economyProvider
            clanCommandController
            commandManager
            playerStatusProvider
            AstraClansAPI.playerStatusProvider = inject()
        }
        Bukkit.getPluginManager().getPlugin("PlaceholderAPI")?.let {
            if (PapiExpansions.isRegistered) {
                Logger.warn("PAPI already registered")
                return@let
            }
            Logger.log("PAPI registered")
            PapiExpansions.register()
        }
        AsyncHelper.launch {
            ClanDataSource.selectAll().forEach(AstraClansAPI::rememberClan)
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


