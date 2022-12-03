package com.astrainteractive.astraclans.domain.exception

import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO

sealed class ClanOperationException : Exception() {
    abstract val sender: ClanMemberDTO
    class PlayerNotClanLeader(override val sender: ClanMemberDTO) : ClanOperationException()
    class ErrorInDatabase(override val sender: ClanMemberDTO) : ClanOperationException()
    class LandAlreadyClaimed(override val sender: ClanMemberDTO) : ClanOperationException()
    class NoClaimedChunkNearby(override val sender: ClanMemberDTO) : ClanOperationException()
    class PlayerAlreadyInClan(override val sender: ClanMemberDTO) : ClanOperationException()
    class NotEnoughMoney(override val sender: ClanMemberDTO) : ClanOperationException()
    class EmptyClanName(override val sender: ClanMemberDTO) : ClanOperationException()
    class EmptyClanTag(override val sender: ClanMemberDTO) : ClanOperationException()
    class AlreadyInClan(override val sender: ClanMemberDTO) : ClanOperationException()
    class NotInvited(override val sender: ClanMemberDTO) : ClanOperationException()
    class TagNotFound(override val sender: ClanMemberDTO) : ClanOperationException()
    class YouAreLeader(override val sender: ClanMemberDTO) : ClanOperationException()
    class NotInClan(override val sender: ClanMemberDTO) : ClanOperationException()
    class AlreadyInvited(override val sender: ClanMemberDTO) : ClanOperationException()
}