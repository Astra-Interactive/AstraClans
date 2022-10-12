package usacease

import REAL_DB
import com.astrainteractive.astraclans.domain.DatabaseModule
import com.astrainteractive.astraclans.domain.api.use_cases.ClanCreateUseCase
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.exception.ClanOperationException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import randomize

import kotlin.test.BeforeTest
import kotlin.test.Test

class ClanCreateUseCase {
    @BeforeTest
    fun prepare() {
        DatabaseModule.createDatabase(REAL_DB)
    }

    @Test
    fun `ClanCreateUseCase`() {
        ClanCreateUseCase.Params(
            clanTag = null, clanName = "name", player = ClanMemberDTO(
                minecraftName = randomize(), minecraftUUID = randomize()
            )
        ).also { params ->
            assertThrows<ClanOperationException.EmptyClanTag> { runBlocking { ClanCreateUseCase(params) } }
        }

        ClanCreateUseCase.Params(
            clanTag = "Tag", clanName = null, player = ClanMemberDTO(
                minecraftName = randomize(), minecraftUUID = randomize()
            )
        ).also { params ->
            assertThrows<ClanOperationException.EmptyClanName> { runBlocking { ClanCreateUseCase(params) } }
        }

        val playerDTO = ClanMemberDTO(
            minecraftName = randomize(), minecraftUUID = randomize()
        )
        val params = ClanCreateUseCase.Params(randomize(), randomize(), playerDTO)
        assertDoesNotThrow { runBlocking { ClanCreateUseCase(params) } }
        assertThrows<ClanOperationException.AlreadyInClan> { runBlocking { ClanCreateUseCase(params) } }
    }
}