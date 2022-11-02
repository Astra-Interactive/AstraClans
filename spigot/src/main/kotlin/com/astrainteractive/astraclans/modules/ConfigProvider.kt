package com.astrainteractive.astraclans.modules

import com.astrainteractive.astraclans.domain.config.PluginConfig
import ru.astrainteractive.astralibs.EmpireSerializer
import ru.astrainteractive.astralibs.di.IReloadable


object ConfigProvider : IReloadable<PluginConfig>() {
    override fun initializer(): PluginConfig {
        return EmpireSerializer.toClass(Files.configFile) ?: throw Exception("Could not load config")
    }
}