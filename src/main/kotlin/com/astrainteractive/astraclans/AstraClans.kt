package com.astrainteractive.astraclans

import com.astrainteractive.astraclans.config.*
import com.astrainteractive.astraclans.config.config.ConfigProvider
import com.astrainteractive.astraclans.config.config.IConfigProvider
import com.astrainteractive.astraclans.di.*
import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astralibs.AstraLibs
import com.astrainteractive.astralibs.Logger
import com.astrainteractive.astralibs.events.GlobalEventManager
import com.astrainteractive.astralibs.utils.Injector.inject
import com.astrainteractive.astraclans.events.EventHandler
import com.astrainteractive.astraclans.utils.*
import com.astrainteractive.astralibs.async.AsyncHelper
import com.astrainteractive.astralibs.utils.Injector
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

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

        eventHandler = EventHandler()
        pluginTranslation
        fileModule
        configModule
        databaseModule
        discordSRV
        economyProvider
        clanCommandController
        commandManager
        AstraClansAPI.playerStatusProvider = playerStatusProvider

        if (isPluginExists("PlaceholderAPI")) {
            if (PapiExpansions.isRegistered)
                Logger.warn("PAPI already registered")
            else {
                Logger.log("PAPI registered")
                PapiExpansions.register()
            }
        }

        AsyncHelper.launch {
            ClanDataSource.selectAll().forEach(AstraClansAPI::rememberClan)
        }

    }

    fun reload() {
        val configProvider: IConfigProvider = inject()
        val files = Injector.inject<Files>().also {
            it.configFile.reload()
        }
        configProvider.config = ConfigProvider.loadPluginConfig(files.configFile)
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


