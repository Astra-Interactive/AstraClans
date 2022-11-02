package com.astrainteractive.astraclans.modules

import com.astrainteractive.astraclans.domain.api.use_cases.ClanCreateUseCase
import ru.astrainteractive.astralibs.di.IReloadable
import ru.astrainteractive.astralibs.di.getValue

object ClanCreateUseCaseModule:IReloadable<ClanCreateUseCase>() {
    private val config by ConfigProvider
    private val economyProvider by VaultEconomyModule
    override fun initializer(): ClanCreateUseCase {
        return ClanCreateUseCase(config, economyProvider)
    }
}