package usacease

import REAL_DB
import com.astrainteractive.astraclans.domain.DatabaseModule
import com.astrainteractive.astraclans.domain.api.response.ClaimChunkResponse
import com.astrainteractive.astraclans.domain.api.use_cases.ClaimChunkUseCase
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import kotlinx.coroutines.runBlocking
import randomize

import kotlin.test.BeforeTest
import kotlin.test.Test

class ClaimChunkUseCase {
    @BeforeTest
    fun prepare() {
        DatabaseModule.createDatabase(REAL_DB)
    }

    @Test
    fun ClaimChunkUseCase() {
        val clanDTO = DTO.ClanDTO.run {
            ClanDataSource.insert(this)
        }
        val clanMemberDTO = DTO.ClanMemberDTO
        val clanLeaderMemberDTO = ClanMemberDTO(
            minecraftName = clanDTO.leaderName, minecraftUUID = clanDTO.leaderUUID
        )
        val landDTO = DTO.LandDTO
        ClaimChunkUseCase.Params(clanMemberDTO, landDTO).also {
            val result = runBlocking { ClaimChunkUseCase(it) }
            assert(result is ClaimChunkResponse.NotLeader)
        }

        ClaimChunkUseCase.Params(clanLeaderMemberDTO, landDTO).also {
            val result = runBlocking { ClaimChunkUseCase(it) }
            assert(result is ClaimChunkResponse.Success)
        }

        ClaimChunkUseCase.Params(clanLeaderMemberDTO, landDTO).also {
            val result = runBlocking { ClaimChunkUseCase(it) }
            assert(result is ClaimChunkResponse.AlreadyClaimed)
        }

    }
}