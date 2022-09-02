package usacease

import REAL_DB
import com.astrainteractive.astraclans.domain.DatabaseModule
import com.astrainteractive.astraclans.domain.api.response.ClanCreateResponse
import com.astrainteractive.astraclans.domain.api.use_cases.ClanCreateUseCase
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import kotlinx.coroutines.runBlocking
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
            val noTagResult = runBlocking { ClanCreateUseCase(params) }
            assert(noTagResult is ClanCreateResponse.EmptyClanTag)
        }

        ClanCreateUseCase.Params(
            clanTag = "Tag", clanName = null, player = ClanMemberDTO(
                minecraftName = randomize(), minecraftUUID = randomize()
            )
        ).also { params ->
            val noNameResult = runBlocking { ClanCreateUseCase(params) }
            assert(noNameResult is ClanCreateResponse.EmptyClanName)

        }

        val playerDTO = ClanMemberDTO(
            minecraftName = randomize(), minecraftUUID = randomize()
        )
        val params = ClanCreateUseCase.Params(randomize(), randomize(), playerDTO)
        val success = runBlocking { ClanCreateUseCase(params) }
        assert(success is ClanCreateResponse.Success)
        val clanLeaderError = runBlocking { ClanCreateUseCase(params) }
        assert(clanLeaderError is ClanCreateResponse.PlayerAlreadyInClan)
    }
}