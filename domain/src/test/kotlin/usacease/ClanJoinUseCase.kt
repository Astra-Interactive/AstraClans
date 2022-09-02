package usacease

import REAL_DB
import randomize

import kotlin.test.BeforeTest
import kotlin.test.Test
import com.astrainteractive.astraclans.domain.DatabaseModule
import com.astrainteractive.astraclans.domain.api.response.ClanJoinResponse
import com.astrainteractive.astraclans.domain.api.use_cases.ClanJoinUseCase
import com.astrainteractive.astraclans.domain.api.use_cases.InvitePlayerUseCase
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import kotlinx.coroutines.runBlocking

class ClanJoinUseCase {
    @BeforeTest
    fun prepare() {
        DatabaseModule.createDatabase(REAL_DB)
    }

    @Test
    fun ClanJoinUseCase() {
        val clanDTO = DTO.ClanDTO.run {
            ClanDataSource.insert(this)
        }
        val clanMemberDTO = DTO.ClanMemberDTO
        val clanLeaderMemberDTO = ClanMemberDTO(
            minecraftName = clanDTO.leaderName,
            minecraftUUID = clanDTO.leaderUUID
        )

        ClanJoinUseCase.Params(clanDTO, clanLeaderMemberDTO).also {
            val result = runBlocking { ClanJoinUseCase(it) }
            assert(result is ClanJoinResponse.AlreadyInClan)
        }

        ClanJoinUseCase.Params(clanDTO, clanMemberDTO).also {
            val result = runBlocking { ClanJoinUseCase(it) }
            assert(result is ClanJoinResponse.NotInvited)
        }

        // Invite player
        InvitePlayerUseCase.Params(clanDTO, clanMemberDTO).also {
            runBlocking { InvitePlayerUseCase(it) }
        }
        ClanJoinUseCase.Params(clanDTO, clanMemberDTO).also {
            val result = runBlocking { ClanJoinUseCase(it) }
            assert(result is ClanJoinResponse.Success)
        }

        ClanJoinUseCase.Params(clanDTO, clanMemberDTO).also {
            val result = runBlocking { ClanJoinUseCase(it) }
            assert(result is ClanJoinResponse.NotInvited || result is ClanJoinResponse.AlreadyInClan)
        }
    }
}