package com.astrainteractive.astraclans.domain.api.use_cases

import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.api.response.ClanDisbandResponse
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.datasource.ClanMemberDataSource
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO

object ClanDisbandUseCase : UseCase<ClanDisbandResponse, ClanDisbandUseCase.Param>() {
    class Param(val clanMemberDTO: ClanMemberDTO)

    override suspend fun run(params: Param): ClanDisbandResponse? {
        val clanDTO = ClanDataSource.select(params.clanMemberDTO.minecraftUUID) ?: return ClanDisbandResponse.NotLeader
        ClanMemberDataSource.deleteFromClan(clanDTO) ?: return ClanDisbandResponse.ErrorInDatabase
        val result = ClanDataSource.delete(clanDTO)
        AstraClansAPI.forgetClan(clanDTO)
        return if (result) ClanDisbandResponse.Success(clanDTO)
        else ClanDisbandResponse.ErrorInDatabase
    }
}