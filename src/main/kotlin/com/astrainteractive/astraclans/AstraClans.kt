package com.astrainteractive.astraclans

import CommandManager
import com.astrainteractive.astraclans.domain.DatabaseModule
import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astralibs.AstraLibs
import com.astrainteractive.astralibs.Logger
import com.astrainteractive.astralibs.ServerVersion
import com.astrainteractive.astralibs.events.GlobalEventManager
import com.astrainteractive.astralibs.utils.Injector.inject
import com.astrainteractive.astralibs.utils.Injector.remember
import com.astrainteractive.astraclans.events.EventHandler
import com.astrainteractive.astraclans.utils.PapiExpansions
import com.astrainteractive.astraclans.utils.PluginTranslation
import com.astrainteractive.astraclans.utils._Files
import com.astrainteractive.astraclans.utils._EmpireConfig
import com.astrainteractive.astralibs.async.AsyncHelper
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

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

    /**
     * Command manager for your commands.
     *
     * You can create multiple managers.
     */
    private lateinit var commandManager: CommandManager


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
        commandManager = CommandManager()
        _EmpireConfig.create()
        Logger.log("Logger enabled", "AstraTemplate")
        Logger.warn("Warn message from logger", "AstraTemplate")
        Logger.error("Error message", "AstraTemplate")
        if (ServerVersion.version == ServerVersion.UNMAINTAINED)
            Logger.warn("Your server version is not maintained and might be not fully functional!", "AstraTemplate")
        else
            Logger.log(
                "Your server version is: ${ServerVersion.getServerVersion()}. This version is supported!",
                "AstraTemplate"
            )
        Bukkit.getPluginManager().getPlugin("PlaceholderAPI")?.let {
            if (PapiExpansions.isRegistered) return@let
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


