package usacease

import REAL_DB
import com.astrainteractive.astraclans.domain.DatabaseModule
import com.astrainteractive.astraclans.domain.api.response.InvitePlayerResponse
import com.astrainteractive.astraclans.domain.api.use_cases.InvitePlayerUseCase
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import kotlinx.coroutines.runBlocking

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
        InvitePlayerUseCase.Params(clanDTO, clanLeaderMemberDTO).also {
            val result = runBlocking { InvitePlayerUseCase(it) }
            assert(result is InvitePlayerResponse.AlreadyInClan)
        }

        InvitePlayerUseCase.Params(clanDTO, clanMemberDTO).also {
            runBlocking { InvitePlayerUseCase(it) }.also { result ->
                assert(result is InvitePlayerResponse.Success)
            }
            runBlocking { InvitePlayerUseCase(it) }.also { result ->
                assert(result is InvitePlayerResponse.AlreadyInvited)
            }
        }

    }
}