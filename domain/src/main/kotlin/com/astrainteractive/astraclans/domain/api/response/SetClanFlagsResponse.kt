package com.astrainteractive.astraclans.domain.api.response

import com.astrainteractive.astraclans.domain.dto.FlagDTO

sealed class SetClanFlagsResponse : ApiResponse<FlagDTO> {
    object NotLeader : SetClanFlagsResponse()
    object ErrorInDatabase : SetClanFlagsResponse()
    class Success(val result: FlagDTO) : SetClanFlagsResponse()
}