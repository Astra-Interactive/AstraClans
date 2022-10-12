package com.astrainteractive.astraclans.domain.api.use_cases

import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.datasource.ClanMemberDataSource
import com.astrainteractive.astraclans.domain.datasource.PendingInviteDataSource
import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.exception.ClanOperationException

object ClanJoinUseCase : UseCase<ClanJoinUseCase.Response, ClanJoinUseCase.Params>() {
    class Params(val clanTAG: String, val memberDTO: ClanMemberDTO)
    class Response(val clanDTO: ClanDTO,val memberDTO: ClanMemberDTO)
    override suspend fun run(params: Params): Response {
        val sender = params.memberDTO
        val clanDTO = ClanDataSource.selectByTag(params.clanTAG) ?: throw ClanOperationException.TagNotFound(sender)

        val memberDTO = params.memberDTO
        if (memberDTO.minecraftUUID == clanDTO.leaderUUID) throw ClanOperationException.AlreadyInClan(sender)
        ClanMemberDataSource.select(memberDTO.minecraftUUID)?.let {
            throw ClanOperationException.AlreadyInClan(sender)
        }
        PendingInviteDataSource.select(memberDTO.minecraftUUID).firstOrNull { it.clanID == clanDTO.id }
            ?: throw ClanOperationException.NotInvited(sender)
        PendingInviteDataSource.removeAll(memberDTO.minecraftUUID)
        val result =
            ClanMemberDataSource.insert(clanDTO, memberDTO) ?: throw ClanOperationException.ErrorInDatabase(sender)
        AstraClansAPI.onMemberChanged(clanDTO)
        return Response(clanDTO, result)
    }
}