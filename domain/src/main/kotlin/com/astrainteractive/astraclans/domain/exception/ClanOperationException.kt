package com.astrainteractive.astraclans.domain.exception

import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO

sealed class ClanOperationException : Exception() {
    class PlayerNotClanLeader(val sender: ClanMemberDTO) : ClanOperationException()
    class ErrorInDatabase(val sender: ClanMemberDTO) : ClanOperationException()
    class LandAlreadyClaimed(val sender: ClanMemberDTO) : ClanOperationException()
    class NoClaimedChunkNearby(val sender: ClanMemberDTO) : ClanOperationException()
    class PlayerAlreadyInClan(val sender: ClanMemberDTO) : ClanOperationException()
    class NotEnoughMoney(val sender: ClanMemberDTO) : ClanOperationException()
    class EmptyClanName(val sender: ClanMemberDTO) : ClanOperationException()
    class EmptyClanTag(val sender: ClanMemberDTO) : ClanOperationException()
    class AlreadyInClan(val sender: ClanMemberDTO) : ClanOperationException()
    class NotInvited(val sender: ClanMemberDTO) : ClanOperationException()
    class TagNotFound(val sender: ClanMemberDTO) : ClanOperationException()
    class YouAreLeader(val sender: ClanMemberDTO) : ClanOperationException()
    class NotInClan(val sender: ClanMemberDTO) : ClanOperationException()
    class AlreadyInvited(val sender: ClanMemberDTO) : ClanOperationException()
}