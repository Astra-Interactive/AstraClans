package com.astrainteractive.astraclans.domain.api.response

import com.astrainteractive.astraclans.domain.dto.ClanDTO

sealed class ClanCreateResponse : ApiResponse<ClanDTO> {
    object PlayerAlreadyInClan : ClanCreateResponse()
    object NotEnoughMoney : ClanCreateResponse()
    object ClanCreateError : ClanCreateResponse()
    object EmptyClanTag : ClanCreateResponse()
    object EmptyClanName : ClanCreateResponse()
    class Success(val clanDTO: ClanDTO) : ClanCreateResponse()
}
