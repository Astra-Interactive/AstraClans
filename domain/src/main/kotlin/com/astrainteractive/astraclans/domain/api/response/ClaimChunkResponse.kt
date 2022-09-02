package com.astrainteractive.astraclans.domain.api.response

import com.astrainteractive.astraclans.domain.dto.LandDTO

sealed class ClaimChunkResponse : ApiResponse<LandDTO> {
    object NotLeader : ClaimChunkResponse()
    object ErrorInDatabase : ClaimChunkResponse()
    object AlreadyClaimed : ClaimChunkResponse()
    object NoClaimedChunkNearby : ClaimChunkResponse()
    class Success(val result: LandDTO) : ClaimChunkResponse()
}