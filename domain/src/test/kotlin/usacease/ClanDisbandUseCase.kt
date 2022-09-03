package usacease

import REAL_DB
import kotlin.test.BeforeTest
import kotlin.test.Test
import com.astrainteractive.astraclans.domain.DatabaseModule
import com.astrainteractive.astraclans.domain.api.response.ClanDisbandResponse
import com.astrainteractive.astraclans.domain.api.response.ClanJoinResponse
import com.astrainteractive.astraclans.domain.api.response.ClanLeaveResponse
import com.astrainteractive.astraclans.domain.api.response.InvitePlayerResponse
import com.astrainteractive.astraclans.domain.api.use_cases.ClanDisbandUseCase
import com.astrainteractive.astraclans.domain.api.use_cases.ClanJoinUseCase
import com.astrainteractive.astraclans.domain.api.use_cases.ClanLeaveUseCase
import com.astrainteractive.astraclans.domain.api.use_cases.InvitePlayerUseCase
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.datasource.ClanMemberDataSource
import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import kotlinx.coroutines.runBlocking

class ClanDisbandUseCase {
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

    fun disbandClan(memberDTO: ClanMemberDTO) = ClanDisbandUseCase.Param(memberDTO).run {
        runBlocking { ClanDisbandUseCase(this@run) }
    }

    @Test
    fun ClanLeaveUseCase() {
        assert(invitePlayer() is InvitePlayerResponse.Success)
        assert(joinClan(clanMemberDTO) is ClanJoinResponse.Success)
        assert(disbandClan(clanLeaderMemberDTO) is ClanDisbandResponse.Success)
        assert(disbandClan(clanMemberDTO) is ClanDisbandResponse.NotLeader)
        assert(ClanMemberDataSource.select(clanMemberDTO.minecraftUUID) == null)
    }
}