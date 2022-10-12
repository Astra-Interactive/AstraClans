package com.astrainteractive.astraclans.domain.api.use_cases

import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.datasource.FlagDataSource
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.dto.FlagDTO
import com.astrainteractive.astraclans.domain.exception.ClanOperationException

object SetClanFlagUseCase : UseCase<FlagDTO, SetClanFlagUseCase.Params>() {
    class Params(val memberDTO: ClanMemberDTO, val flagDTO: FlagDTO)

    override suspend fun run(params: Params): FlagDTO {
        val clanLeaderDTO = params.memberDTO
        val clanDTO = ClanDataSource.select(clanLeaderDTO.minecraftUUID)
        if (clanDTO == null || clanLeaderDTO.minecraftUUID != clanDTO.leaderUUID) throw ClanOperationException.PlayerNotClanLeader(clanLeaderDTO)
        val flagDTO = params.flagDTO.copy(clanID = clanDTO.id)
        val result = FlagDataSource.updateOrInsert(flagDTO) ?: throw ClanOperationException.ErrorInDatabase(clanLeaderDTO)
        AstraClansAPI.onFlagChanged(clanDTO)
        return result
    }
}