package com.astrainteractive.astraclans.domain.api.use_cases

import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.api.SetClanFlagsResponse
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.datasource.FlagDataSource
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.dto.FlagDTO

object SetClanFlagUseCase : UseCase<SetClanFlagsResponse, SetClanFlagUseCase.Params>() {
    class Params(val memberDTO: ClanMemberDTO, val flagDTO: FlagDTO)

    override suspend fun run(params: Params): SetClanFlagsResponse? {
        val clanLeaderDTO = params.memberDTO
        val clanDTO = ClanDataSource.select(clanLeaderDTO.minecraftUUID)
        val flagDTO = params.flagDTO
        if (clanDTO == null || clanLeaderDTO.minecraftUUID != clanDTO.leaderUUID) return SetClanFlagsResponse.NotLeader
        val result = FlagDataSource.updateOrInsert(flagDTO) ?: return SetClanFlagsResponse.ErrorInDatabase
        AstraClansAPI.onFlagChanged(clanDTO)
        return SetClanFlagsResponse.Success(result)
    }
}