package com.astrainteractive.astraclans.domain.api.response

import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO

sealed class ClanJoinResponse : ApiResponse<ClanMemberDTO> {
    object ErrorInDatabase : ClanJoinResponse()
    object AlreadyInClan : ClanJoinResponse()
    object NotInvited : ClanJoinResponse()
    object TagNotFound : ClanJoinResponse()
    class Success(val result: ClanMemberDTO) : ClanJoinResponse()
}