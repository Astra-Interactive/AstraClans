package usacease

import REAL_DB
import com.astrainteractive.astraclans.domain.DatabaseModule
import com.astrainteractive.astraclans.domain.api.use_cases.InvitePlayerUseCase
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.exception.ClanOperationException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

import kotlin.test.BeforeTest
import kotlin.test.Test

class InvitePlayerUseCase {
    @BeforeTest
    fun prepare() {
        DatabaseModule.createDatabase(REAL_DB)
    }

    @Test
    fun `InvitePlayerUseCase`() {
        val clanDTO = DTO.ClanDTO.run {
            ClanDataSource.insert(this)
        }
        val clanMemberDTO = DTO.ClanMemberDTO
        val clanLeaderMemberDTO = ClanMemberDTO(
            minecraftName = clanDTO.leaderName,
            minecraftUUID = clanDTO.leaderUUID
        )
        InvitePlayerUseCase.Params(clanLeaderMemberDTO, clanLeaderMemberDTO).also {
            assertThrows<ClanOperationException.AlreadyInClan> { runBlocking { InvitePlayerUseCase(it) } }
        }

        InvitePlayerUseCase.Params(clanLeaderMemberDTO, clanMemberDTO).also {
            assertDoesNotThrow { runBlocking { InvitePlayerUseCase(it) } }
            assertThrows<ClanOperationException.AlreadyInvited> { runBlocking { InvitePlayerUseCase(it) } }
        }

    }
}