package com.astrainteractive.astraclans.domain.api.use_cases

import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.api.ClaimChunkResponse
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.datasource.LandDataSource
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.dto.LandDTO

object ClaimChunkUseCase : UseCase<ClaimChunkResponse, ClaimChunkUseCase.Params>() {
    class Params(val memberDTO: ClanMemberDTO, val landDTO: LandDTO)

    override suspend fun run(params: Params): ClaimChunkResponse? {
        val memberDTO = params.memberDTO
        val clanDTO = ClanDataSource.select(memberDTO.minecraftUUID)
        val landDTO = params.landDTO
        if (clanDTO == null || clanDTO.leaderUUID != memberDTO.minecraftUUID) return ClaimChunkResponse.NotLeader
        // TODO check on near self
        // TODO check on near other lands
        LandDataSource.select(landDTO.worldName, landDTO.x, landDTO.z)?.let {
            return ClaimChunkResponse.AlreadyClaimed
        }
        val result = LandDataSource.insert(landDTO) ?: return ClaimChunkResponse.ErrorInDatabase
        AstraClansAPI.onLandChanged(clanDTO)
        return ClaimChunkResponse.Success(result)

    }
}