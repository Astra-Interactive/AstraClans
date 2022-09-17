package com.astrainteractive.astraclans.domain.api.response

import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO

sealed class ClanDisbandResponse : ApiResponse<ClanMemberDTO> {
    object ErrorInDatabase : ClanDisbandResponse()
    object NotLeader : ClanDisbandResponse()
    class Success(val clanDTO: ClanDTO) : ClanDisbandResponse()
}