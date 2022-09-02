package com.astrainteractive.astraclans.domain.api.response

import com.astrainteractive.astraclans.domain.dto.PendingInviteDTO

sealed class InvitePlayerResponse : ApiResponse<PendingInviteDTO> {
    object AlreadyInvited : InvitePlayerResponse()
    object ErrorInDatabase : InvitePlayerResponse()
    object AlreadyInClan : InvitePlayerResponse()
    class Success(val result: PendingInviteDTO) : InvitePlayerResponse()
}