package com.astrainteractive.astraclans

import CommandManager
import com.astrainteractive.astraclans.domain.DatabaseModule
import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.events.EventHandler
import com.astrainteractive.astraclans.modules.*
import com.astrainteractive.astraclans.utils.PapiExpansions
import com.astrainteractive.astraclans.utils.PlayerStatusProvider
import com.astrainteractive.astraclans.utils.isPluginExists
import kotlinx.coroutines.launch
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin
import ru.astrainteractive.astralibs.AstraLibs
import ru.astrainteractive.astralibs.Logger
import ru.astrainteractive.astralibs.async.PluginScope
import ru.astrainteractive.astralibs.events.GlobalEventManager
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
     * This method called when server starts or PlugMan load plugin.
     */
    override fun onEnable() {
        AstraLibs.rememberPlugin(this)
        Logger.prefix = "AstraTemplate"

        eventHandler = EventHandler()
        reload()
        DatabaseModule.createDatabase("${AstraLibs.instance.dataFolder}${File.separator}clans.db")
        CommandManager()
        AstraClansAPI.playerStatusProvider = PlayerStatusProvider
        if (isPluginExists("PlaceholderAPI")) {
            if (PapiExpansions.isRegistered)
                Logger.warn("PAPI already registered")
            else {
                Logger.log("PAPI registered")
                PapiExpansions.register()
            }
        }

        PluginScope.launch {
            ClanDataSource.selectAll().forEach(AstraClansAPI::rememberClan)
        }

    }

    fun reload() {
        TranslationProvider.reload()
        Files.configFile.reload()
        ConfigProvider.reload()
        ClanCreateUseCaseModule.reload()
    }

    /**
     * This method called when server is shutting down or when PlugMan disable plugin.
     */
    override fun onDisable() {
        eventHandler.onDisable()
        HandlerList.unregisterAll(this)
        GlobalEventManager.onDisable()
        if (isPluginExists("PlaceholderAPI"))
            PapiExpansions.unregister()
    }
}


