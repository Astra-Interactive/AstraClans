package com.astrainteractive.astraclans.domain.api.use_cases

import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.config.PluginConfig
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.datasource.ClanMemberDataSource
import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.exception.ClanOperationException
import ru.astrainteractive.astralibs.domain.IUseCase
import ru.astrainteractive.astralibs.utils.economy.IEconomyProvider
import java.util.*


class ClanCreateUseCase(
    private val config: PluginConfig,
    private val economyProvider: IEconomyProvider?
) : IUseCase<ClanDTO, ClanCreateUseCase.Params> {

    class Params(
        val clanTag: String?,
        val clanName: String?,
        val player: ClanMemberDTO
    )

    override suspend fun run(params: Params): ClanDTO {
        val clanTag = params.clanTag
        val clanName = params.clanName
        val player = params.player
        if (clanTag == null) throw ClanOperationException.EmptyClanTag(player)
        if (clanName == null) throw ClanOperationException.EmptyClanName(player)
        ClanDataSource.select(player.minecraftUUID)?.let {
            throw ClanOperationException.AlreadyInClan(player)
        }
        ClanMemberDataSource.select(player.minecraftUUID)?.let {
            throw ClanOperationException.AlreadyInClan(player)
        }
        if (config.economy.clanCreatePurchaseAmount>0) {
            val playerUUID = UUID.fromString(player.minecraftUUID)
            val playerBalance = economyProvider?.getBalance(playerUUID) ?: 0.0
            println("Balance: $playerBalance; required: ${config.economy.clanCreatePurchaseAmount}")
            if (playerBalance < config.economy.clanCreatePurchaseAmount)
                throw ClanOperationException.NotEnoughMoney(player)
            val takeMoneyResult =
                economyProvider?.takeMoney(playerUUID, config.economy.clanCreatePurchaseAmount.toDouble()) ?: false
            if (!takeMoneyResult) throw ClanOperationException.NotEnoughMoney(player)
        }
        val _clanDTO = ClanDTO(
            clanTag = clanTag,
            clanName = clanName,
            leaderUUID = player.minecraftUUID,
            leaderName = player.minecraftName
        )
        val clanDTO = ClanDataSource.insert(_clanDTO) ?: throw ClanOperationException.ErrorInDatabase(player)

        AstraClansAPI.rememberClan(clanDTO)
        return clanDTO

    }
}