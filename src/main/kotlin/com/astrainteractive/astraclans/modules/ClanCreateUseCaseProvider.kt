package com.astrainteractive.astraclans.modules

import com.astrainteractive.astraclans.domain.api.use_cases.ClanCreateUseCase
import com.astrainteractive.astraclans.domain.di.IReloadable

object ClanCreateUseCaseProvider : IReloadable<ClanCreateUseCase>() {
    override fun initializer(): ClanCreateUseCase {
        val configProvider = ConfigProvider
        val economyProvider = VaultEconomyModule.value
        return ClanCreateUseCase(configProvider, economyProvider)
    }
}