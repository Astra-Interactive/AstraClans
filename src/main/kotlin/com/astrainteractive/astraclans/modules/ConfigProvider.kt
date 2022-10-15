package com.astrainteractive.astraclans.modules

import com.astrainteractive.astraclans.domain.config.PluginConfig
import com.astrainteractive.astraclans.domain.di.IConfigProvider
import ru.astrainteractive.astralibs.EmpireSerializer



object ConfigProvider : IConfigProvider() {
    override fun initializer(): PluginConfig {
        return EmpireSerializer.toClass(Files.configFile) ?: throw Exception("Could not load config")
    }
}