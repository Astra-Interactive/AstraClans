package usacease

import REAL_DB
import kotlin.test.BeforeTest
import kotlin.test.Test
import com.astrainteractive.astraclans.domain.DatabaseModule
import com.astrainteractive.astraclans.domain.api.response.ClanJoinResponse
import com.astrainteractive.astraclans.domain.api.response.ClanLeaveResponse
import com.astrainteractive.astraclans.domain.api.response.InvitePlayerResponse
import com.astrainteractive.astraclans.domain.api.use_cases.ClanJoinUseCase
import com.astrainteractive.astraclans.domain.api.use_cases.ClanLeaveUseCase
import com.astrainteractive.astraclans.domain.api.use_cases.InvitePlayerUseCase
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import kotlinx.coroutines.runBlocking

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
        assert(leaveClan(clanMemberDTO) is ClanLeaveResponse.NotInClan)
        assert(invitePlayer() is InvitePlayerResponse.Success)
        assert(joinClan(clanMemberDTO) is ClanJoinResponse.Success)
        assert(leaveClan(clanMemberDTO) is ClanLeaveResponse.Success)
        assert(leaveClan(clanMemberDTO) is ClanLeaveResponse.NotInClan)
        assert(leaveClan(clanLeaderMemberDTO) is ClanLeaveResponse.YouAreLeader)
    }
}