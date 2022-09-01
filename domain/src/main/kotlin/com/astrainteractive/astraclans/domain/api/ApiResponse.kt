package com.astrainteractive.astraclans.domain.api

import com.astrainteractive.astraclans.domain.dto.*


sealed interface ApiResponse<T>

sealed class ClanCreateResponse : ApiResponse<ClanDTO> {
    object PlayerAlreadyInClan : ClanCreateResponse()
    object ClanCreateError : ClanCreateResponse()
    object EmptyClanTag : ClanCreateResponse()
    object EmptyClanName : ClanCreateResponse()
    class Success(val result: ClanDTO) : ClanCreateResponse()
}

sealed class InvitePlayerResponse : ApiResponse<PendingInviteDTO> {
    object AlreadyInvited : InvitePlayerResponse()
    object ErrorInDatabase : InvitePlayerResponse()
    object AlreadyInClan : InvitePlayerResponse()
    class Success(val result: PendingInviteDTO) : InvitePlayerResponse()
}

sealed class ClanJoinResponse : ApiResponse<ClanMemberDTO> {
    object ErrorInDatabase : ClanJoinResponse()
    object AlreadyInClan : ClanJoinResponse()
    object NotInvited : ClanJoinResponse()
    class Success(val result: ClanMemberDTO) : ClanJoinResponse()
}

sealed class ClaimChunkResponse : ApiResponse<LandDTO> {
    object NotLeader : ClaimChunkResponse()
    object ErrorInDatabase : ClaimChunkResponse()
    object AlreadyClaimed : ClaimChunkResponse()
    object NoClaimedChunkNearby : ClaimChunkResponse()
    class Success(val result: LandDTO) : ClaimChunkResponse()
}

sealed class SetClanFlagsResponse : ApiResponse<FlagDTO> {
    object NotLeader : SetClanFlagsResponse()
    object ErrorInDatabase : SetClanFlagsResponse()
    class Success(val result: FlagDTO) : SetClanFlagsResponse()
}