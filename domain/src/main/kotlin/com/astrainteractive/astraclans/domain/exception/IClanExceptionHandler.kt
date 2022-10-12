package com.astrainteractive.astraclans.domain.exception

interface IClanExceptionHandler {
    fun handle(e: ClanOperationException) {
        when (e) {
            is ClanOperationException.AlreadyInClan -> handle(e)
            is ClanOperationException.AlreadyInvited -> handle(e)
            is ClanOperationException.EmptyClanName -> handle(e)
            is ClanOperationException.EmptyClanTag -> handle(e)
            is ClanOperationException.ErrorInDatabase -> handle(e)
            is ClanOperationException.LandAlreadyClaimed -> handle(e)
            is ClanOperationException.NoClaimedChunkNearby -> handle(e)
            is ClanOperationException.NotEnoughMoney -> handle(e)
            is ClanOperationException.NotInClan -> handle(e)
            is ClanOperationException.NotInvited -> handle(e)
            is ClanOperationException.PlayerAlreadyInClan -> handle(e)
            is ClanOperationException.PlayerNotClanLeader -> handle(e)
            is ClanOperationException.TagNotFound -> handle(e)
            is ClanOperationException.YouAreLeader -> handle(e)
        }
    }

    fun handle(e: ClanOperationException.AlreadyInClan)

    fun handle(e: ClanOperationException.AlreadyInvited)

    fun handle(e: ClanOperationException.EmptyClanName)

    fun handle(e: ClanOperationException.EmptyClanTag)


    fun handle(e: ClanOperationException.ErrorInDatabase)

    fun handle(e: ClanOperationException.LandAlreadyClaimed)

    fun handle(e: ClanOperationException.NoClaimedChunkNearby)

    fun handle(e: ClanOperationException.NotEnoughMoney)

    fun handle(e: ClanOperationException.NotInClan)

    fun handle(e: ClanOperationException.NotInvited)

    fun handle(e: ClanOperationException.PlayerAlreadyInClan)

    fun handle(e: ClanOperationException.PlayerNotClanLeader)

    fun handle(e: ClanOperationException.TagNotFound)

    fun handle(e: ClanOperationException.YouAreLeader)

    fun handle(e: Exception)
}