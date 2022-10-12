package usacease

import REAL_DB
import kotlin.test.BeforeTest
import kotlin.test.Test
import com.astrainteractive.astraclans.domain.DatabaseModule
import com.astrainteractive.astraclans.domain.api.use_cases.ClanJoinUseCase
import com.astrainteractive.astraclans.domain.api.use_cases.ClanLeaveUseCase
import com.astrainteractive.astraclans.domain.api.use_cases.InvitePlayerUseCase
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.exception.ClanOperationException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class ClanLeaveUseCase {
    lateinit var clanDTO: ClanDTO
    lateinit var clanLeaderMemberDTO: ClanMemberDTO
    val clanMemberDTO = DTO.ClanMemberDTO

    @BeforeTest
    fun prepare() {
        DatabaseModule.createDatabase(REAL_DB)
        clanDTO = DTO.ClanDTO.run {
            ClanDataSource.insert(this)
        }
        clanLeaderMemberDTO = ClanMemberDTO(
            minecraftName = clanDTO.leaderName,
            minecraftUUID = clanDTO.leaderUUID
        )
    }

    fun invitePlayer() = InvitePlayerUseCase.Params(clanLeaderMemberDTO, clanMemberDTO).run {
        runBlocking { InvitePlayerUseCase(this@run) }
    }

    fun joinClan(memberDTO: ClanMemberDTO) = ClanJoinUseCase.Params(clanDTO.clanTag, memberDTO).run {
        runBlocking { ClanJoinUseCase(this@run) }
    }

    fun leaveClan(memberDTO: ClanMemberDTO) = ClanLeaveUseCase.Param(memberDTO).run {
        runBlocking { ClanLeaveUseCase(this@run) }
    }

    @Test
    fun ClanLeaveUseCase() {
        assertThrows<ClanOperationException.NotInClan> { leaveClan(clanMemberDTO) }
        assertDoesNotThrow { invitePlayer() }
        assertDoesNotThrow { joinClan(clanMemberDTO) }
        assertDoesNotThrow { leaveClan(clanMemberDTO) }
        assertThrows<ClanOperationException.NotInClan> { leaveClan(clanMemberDTO) }
        assertThrows<ClanOperationException.YouAreLeader> { leaveClan(clanLeaderMemberDTO) }
    }
}