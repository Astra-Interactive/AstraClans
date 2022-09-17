package com.astrainteractive.astraclans.domain.api.response

import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO

sealed class ClanLeaveResponse : ApiResponse<ClanMemberDTO> {
    object ErrorInDatabase : ClanLeaveResponse()
    object YouAreLeader : ClanLeaveResponse()
    object NotInClan : ClanLeaveResponse()
    class Success(val clanDTO: ClanDTO) : ClanLeaveResponse()
}