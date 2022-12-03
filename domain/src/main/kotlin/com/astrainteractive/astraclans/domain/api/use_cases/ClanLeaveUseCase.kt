package com.astrainteractive.astraclans.domain.api.use_cases

import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.datasource.ClanMemberDataSource
import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.exception.ClanOperationException
import ru.astrainteractive.astralibs.domain.IUseCase

object ClanLeaveUseCase : IUseCase<ClanDTO, ClanLeaveUseCase.Param> {
    class Param(val memberDTO: ClanMemberDTO)

    override suspend fun run(params: Param): ClanDTO {
        val sender = params.memberDTO
        ClanDataSource.select(params.memberDTO.minecraftUUID)?.let {
            throw ClanOperationException.YouAreLeader(sender)
        }

        val realMemberDTO =
            ClanMemberDataSource.select(params.memberDTO.minecraftUUID) ?: throw ClanOperationException.NotInClan(sender)
        ClanMemberDataSource.delete(realMemberDTO) ?: throw ClanOperationException.ErrorInDatabase(sender)
        val clanDTO = ClanDataSource.selectByID(realMemberDTO.clanID)?.let { clanDTO ->
            AstraClansAPI.onMemberChanged(clanDTO)
            clanDTO
        } ?: throw ClanOperationException.ErrorInDatabase(sender)
        return clanDTO
    }
}