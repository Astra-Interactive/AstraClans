import com.astrainteractive.astraclans.domain.DatabaseModule
import com.astrainteractive.astraclans.domain.api.*
import com.astrainteractive.astraclans.domain.api.use_cases.*
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.dto.FlagDTO
import com.astrainteractive.astraclans.domain.dto.FlagsEnum
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class DatabaseTest {

    @BeforeTest
    fun prepare() {
        DatabaseModule.createDatabase(REAL_DB)
    }

    @Test
    fun `Check insert and select clan leader`() {
        val clanDTO = DTO.ClanDTO
        val insertedClan = ClanDataSource.insert(clanDTO)
        val selectedClan = ClanDataSource.select(insertedClan.leaderUUID)
        assertEquals(insertedClan, selectedClan)
    }

    @Test
    fun `Test clan update`() {
        val clanDTO = DTO.ClanDTO
        val insertedClan = ClanDataSource.insert(clanDTO)
        val copyiedClan = insertedClan.copy(clanName = "Custom Name")
        val updatedClan = ClanDataSource.update(copyiedClan)
        val selectedClan = ClanDataSource.select(insertedClan.leaderUUID)
        assertEquals(copyiedClan, updatedClan)
        assertEquals(copyiedClan, selectedClan)
    }

    @Test
    fun `Delete clan`() {
        val clanDTO = DTO.ClanDTO
        val insertedClan = ClanDataSource.insert(clanDTO)
        val deleted = ClanDataSource.delete(insertedClan)
        val selectedDeletedClan = ClanDataSource.select(insertedClan.leaderUUID)
        assert(selectedDeletedClan == null)
        assert(deleted)
    }

    @Test
    fun `ClanCreateUseCase`() {
        ClanCreateUseCase.Params(
            clanTag = null, clanName = "name", player = ClanMemberDTO(
                minecraftName = randomize(),
                minecraftUUID = randomize()
            )
        ).also { params ->
            val noTagResult = runBlocking { ClanCreateUseCase(params) }
            assert(noTagResult is ClanCreateResponse.EmptyClanTag)
        }

        ClanCreateUseCase.Params(
            clanTag = "Tag", clanName = null, player = ClanMemberDTO(
                minecraftName = randomize(),
                minecraftUUID = randomize()
            )
        ).also { params ->
            val noNameResult = runBlocking { ClanCreateUseCase(params) }
            assert(noNameResult is ClanCreateResponse.EmptyClanName)

        }

        val playerDTO = ClanMemberDTO(
            minecraftName = randomize(),
            minecraftUUID = randomize()
        )
        val params = ClanCreateUseCase.Params(randomize(), randomize(), playerDTO)
        val success = runBlocking { ClanCreateUseCase(params) }
        assert(success is ClanCreateResponse.Success)
        val clanLeaderError = runBlocking { ClanCreateUseCase(params) }
        assert(clanLeaderError is ClanCreateResponse.PlayerAlreadyInClan)
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


    @Test
    fun ClaimChunkUseCase() {
        val clanDTO = DTO.ClanDTO.run {
            ClanDataSource.insert(this)
        }
        val clanMemberDTO = DTO.ClanMemberDTO
        val clanLeaderMemberDTO = ClanMemberDTO(
            minecraftName = clanDTO.leaderName,
            minecraftUUID = clanDTO.leaderUUID
        )
        val landDTO = DTO.LandDTO
        ClaimChunkUseCase.Params(clanMemberDTO, landDTO).also {
            val result = runBlocking { ClaimChunkUseCase(it) }
            assert(result is ClaimChunkResponse.NotLeader)
        }

        ClaimChunkUseCase.Params(clanLeaderMemberDTO, landDTO).also {
            val result = runBlocking { ClaimChunkUseCase(it) }
            assert(result is ClaimChunkResponse.Success)
        }

        ClaimChunkUseCase.Params(clanLeaderMemberDTO, landDTO).also {
            val result = runBlocking { ClaimChunkUseCase(it) }
            assert(result is ClaimChunkResponse.AlreadyClaimed)
        }

    }

    @Test
    fun ChangeFlags() {
        val clanDTO = DTO.ClanDTO.run {
            ClanDataSource.insert(this)
        }
        val customMemberDTO = DTO.ClanMemberDTO
        val clanLeaderMemberDTO = ClanMemberDTO(
            minecraftName = clanDTO.leaderName,
            minecraftUUID = clanDTO.leaderUUID
        )
        FlagsEnum.values().forEach { flagsEnum ->
            val flagDTO = FlagDTO(clanID = clanDTO.id, flag = flagsEnum, enabled = false)
            SetClanFlagUseCase.Params(customMemberDTO, flagDTO).also {
                val result = runBlocking { SetClanFlagUseCase(it) }
                assert(result is SetClanFlagsResponse.NotLeader)
            }
            SetClanFlagUseCase.Params(clanLeaderMemberDTO, flagDTO).also {
                val result = runBlocking { SetClanFlagUseCase(it) }
                assert(result is SetClanFlagsResponse.Success)
            }

            SetClanFlagUseCase.Params(clanLeaderMemberDTO, flagDTO.copy(enabled = !flagDTO.enabled)).also {
                val result = runBlocking { SetClanFlagUseCase(it) }
                assert(result is SetClanFlagsResponse.Success)
            }
        }

    }

}