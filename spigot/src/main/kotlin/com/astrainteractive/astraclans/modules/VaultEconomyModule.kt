package com.astrainteractive.astraclans.modules

import com.astrainteractive.astraclans.utils.isPluginExists
import ru.astrainteractive.astralibs.Logger
import ru.astrainteractive.astralibs.di.IModule
import ru.astrainteractive.astralibs.utils.economy.IEconomyProvider
import ru.astrainteractive.astralibs.utils.economy.VaultEconomyProvider

object VaultEconomyModule : IModule<IEconomyProvider?>() {
    override fun initializer(): IEconomyProvider? {
        val vaultProvider: IEconomyProvider? = if (isPluginExists("Vault")) {
            VaultEconomyProvider.also { it.onEnable() }
        } else {
            Logger.warn("Vault not found")
            null
        }
        return vaultProvider
    }
}