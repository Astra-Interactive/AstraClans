package usacease

import DTO
import REAL_DB
import com.astrainteractive.astraclans.domain.DatabaseModule
import com.astrainteractive.astraclans.domain.api.use_cases.ClanJoinUseCase
import com.astrainteractive.astraclans.domain.api.use_cases.InvitePlayerUseCase
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.exception.ClanOperationException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.BeforeTest
import kotlin.test.Test

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

        ClanJoinUseCase.Params(clanDTO.clanTag, clanLeaderMemberDTO).also {
            assertThrows<ClanOperationException.AlreadyInClan> { runBlocking { ClanJoinUseCase(it) } }
        }

        ClanJoinUseCase.Params(clanDTO.clanTag, clanMemberDTO).also {
            assertThrows<ClanOperationException.NotInvited> { runBlocking { ClanJoinUseCase(it) } }
        }

        // Invite player
        InvitePlayerUseCase.Params(clanLeaderMemberDTO, clanMemberDTO).also {
            runBlocking { InvitePlayerUseCase(it) }
        }
        ClanJoinUseCase.Params(clanDTO.clanTag, clanMemberDTO).also {
            assertDoesNotThrow { runBlocking { ClanJoinUseCase(it) } }
        }

        ClanJoinUseCase.Params(clanDTO.clanTag, clanMemberDTO).also {
            // TODO
//            val result = runBlocking { ClanJoinUseCase(it) }
//            assert(result is ClanJoinResponse.NotInvited || result is ClanJoinResponse.AlreadyInClan)
        }
    }
}