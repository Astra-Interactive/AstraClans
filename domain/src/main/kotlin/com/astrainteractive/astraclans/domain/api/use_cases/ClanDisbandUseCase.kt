package com.astrainteractive.astraclans.domain.api.use_cases

import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.datasource.ClanMemberDataSource
import com.astrainteractive.astraclans.domain.datasource.LandDataSource
import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.exception.ClanOperationException
import ru.astrainteractive.astralibs.domain.IUseCase

object ClanDisbandUseCase : IUseCase<ClanDTO, ClanDisbandUseCase.Param> {
    class Param(val clanMemberDTO: ClanMemberDTO)

    override suspend fun run(params: Param): ClanDTO {
        val sender = params.clanMemberDTO
        val clanDTO = ClanDataSource.select(params.clanMemberDTO.minecraftUUID) ?: throw ClanOperationException.PlayerNotClanLeader(sender)
        ClanMemberDataSource.deleteFromClan(clanDTO) ?: throw ClanOperationException.ErrorInDatabase(sender)
        LandDataSource.delete(clanDTO) ?: throw ClanOperationException.ErrorInDatabase(sender)
        val result = ClanDataSource.delete(clanDTO)
        AstraClansAPI.forgetClan(clanDTO)
        return if (result) clanDTO
        else throw ClanOperationException.ErrorInDatabase(sender)
    }
}