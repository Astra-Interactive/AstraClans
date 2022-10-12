package com.astrainteractive.astraclans.domain.api.use_cases

import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.datasource.LandDataSource
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.dto.LandDTO
import com.astrainteractive.astraclans.domain.exception.ClanOperationException

object ClaimChunkUseCase : UseCase<LandDTO, ClaimChunkUseCase.Params>() {
    class Params(val memberDTO: ClanMemberDTO, val landDTO: LandDTO)

    override suspend fun run(params: Params): LandDTO {
        val memberDTO = params.memberDTO
        val clanDTO = ClanDataSource.select(memberDTO.minecraftUUID)
        if (clanDTO == null || clanDTO.leaderUUID != memberDTO.minecraftUUID) throw ClanOperationException.PlayerNotClanLeader(memberDTO)
        // TODO check on near self
        // TODO check on near other lands
        val landDTO = params.landDTO.copy(clanID = clanDTO.id)
        LandDataSource.select(landDTO.worldName, landDTO.x, landDTO.z)?.let {
            throw ClanOperationException.LandAlreadyClaimed(memberDTO)
        }
        val result = LandDataSource.insert(landDTO) ?: throw ClanOperationException.ErrorInDatabase(memberDTO)
        AstraClansAPI.onLandChanged(clanDTO)
        return result

    }
}