package usacease

import REAL_DB
import com.astrainteractive.astraclans.domain.DatabaseModule
import com.astrainteractive.astraclans.domain.api.use_cases.ClaimChunkUseCase
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.exception.ClanOperationException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
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
            assertThrows<ClanOperationException.PlayerNotClanLeader> { runBlocking { ClaimChunkUseCase(it) } }
        }

        ClaimChunkUseCase.Params(clanLeaderMemberDTO, landDTO).also {
            assertDoesNotThrow { runBlocking { ClaimChunkUseCase(it) }  }
        }

        ClaimChunkUseCase.Params(clanLeaderMemberDTO, landDTO).also {
            assertThrows<ClanOperationException.LandAlreadyClaimed> { runBlocking { ClaimChunkUseCase(it) } }
        }

    }
}