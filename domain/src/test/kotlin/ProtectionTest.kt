import com.astrainteractive.astraclans.domain.DatabaseModule
import com.astrainteractive.astraclans.domain.api.*
import com.astrainteractive.astraclans.domain.api.response.ClaimChunkResponse
import com.astrainteractive.astraclans.domain.api.response.ClanCreateResponse
import com.astrainteractive.astraclans.domain.api.response.SetClanFlagsResponse
import com.astrainteractive.astraclans.domain.api.use_cases.ClaimChunkUseCase
import com.astrainteractive.astraclans.domain.api.use_cases.ClanCreateUseCase
import com.astrainteractive.astraclans.domain.api.use_cases.SetClanFlagUseCase
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.dto.FlagDTO
import com.astrainteractive.astraclans.domain.dto.FlagsEnum
import kotlinx.coroutines.runBlocking

import kotlin.test.BeforeTest
import kotlin.test.Test

class ProtectionTest {
    var clanLeaderDTO = DTO.ClanMemberDTO
    var clanMemberDTO = DTO.ClanMemberDTO
    val noClanMemberDTO = DTO.ClanMemberDTO
    var clanDTO = DTO.ClanDTO.copy(leaderName = clanLeaderDTO.minecraftName, leaderUUID = clanLeaderDTO.minecraftUUID)
    var clanLandDTO = DTO.LandDTO
    val freeLandDTO = DTO.LandDTO

    @BeforeTest
    fun prepare() {
        DatabaseModule.createDatabase(REAL_DB)
        val result = ClanCreateUseCase.Params(clanDTO.clanTag, clanDTO.clanName, clanLeaderDTO).run {
            val params = this
            runBlocking { ClanCreateUseCase(params) }
        } as ClanCreateResponse.Success
        clanDTO = result.result
        clanLeaderDTO = clanLeaderDTO.copy(clanID = clanDTO.id)
        clanMemberDTO = clanMemberDTO.copy(clanID = clanDTO.id)
        ClaimChunkUseCase.Params(clanLeaderDTO, clanLandDTO.copy(clanID = clanDTO.id)).also {
            val result = runBlocking { ClaimChunkUseCase(it) } as ClaimChunkResponse.Success
            clanLandDTO = result.result
        }
    }

    private fun updateFlag(value: Boolean, flagDTO: FlagDTO): FlagDTO {
        return SetClanFlagUseCase.Params(clanLeaderDTO, flagDTO.copy(enabled = value)).run {
            val param = this
            runBlocking { SetClanFlagUseCase(param) } as SetClanFlagsResponse.Success
        }.result
    }

    @Test
    fun IsFlagEnabled() {
        var flagDTO = FlagDTO(clanID = clanDTO.id, flag = FlagsEnum.BLOCK_BREAK_DENY, enabled = false)
        assert(AstraClansAPI.isFlagEnabled(freeLandDTO, FlagsEnum.BLOCK_BREAK_DENY) == null)

        listOf(true, false).forEach { value ->
            flagDTO = updateFlag(value, flagDTO)
            assert(AstraClansAPI.isFlagEnabled(clanLandDTO, FlagsEnum.BLOCK_BREAK_DENY) == value)
        }
    }

    @Test
    fun isFlanEnabledForPlayer() {
        val flag = FlagsEnum.BLOCK_BREAK_DENY
        var flagDTO = FlagDTO(clanID = clanDTO.id, flag = flag, enabled = false)
        // Free land
        assert(AstraClansAPI.isFlagEnabledForPlayer(noClanMemberDTO, freeLandDTO, flag) == null)
        assert(AstraClansAPI.isFlagEnabledForPlayer(noClanMemberDTO, freeLandDTO, flag) == null)
        assert(AstraClansAPI.isFlagEnabledForPlayer(clanLeaderDTO, freeLandDTO, flag) == null)
        assert(AstraClansAPI.isFlagEnabledForPlayer(clanLeaderDTO, freeLandDTO, flag) == null)
        // Clan leader on clan land
        flagDTO = updateFlag(true, flagDTO)
        assert(AstraClansAPI.isFlagEnabledForPlayer(clanLeaderDTO, clanLandDTO, flag) == null)
        flagDTO = updateFlag(false, flagDTO)
        assert(AstraClansAPI.isFlagEnabledForPlayer(clanLeaderDTO, clanLandDTO, flag) == null)
        // Custom player on clan land
        flagDTO = updateFlag(true, flagDTO)
        assert(AstraClansAPI.isFlagEnabledForPlayer(noClanMemberDTO, clanLandDTO, flag) == true)
        flagDTO = updateFlag(false, flagDTO)
        assert(AstraClansAPI.isFlagEnabledForPlayer(noClanMemberDTO, clanLandDTO, flag) == false)

    }

    @Test
    fun CheckCanBreak() {
        var flagDTO = FlagDTO(clanID = clanDTO.id, flag = FlagsEnum.BLOCK_BREAK_DENY, enabled = false)
        val canBreakClanLand: (ClanMemberDTO) -> Boolean? = {
            AstraClansAPI.canBreak(it, clanLandDTO)
        }
        val canBreakFreeLand: (ClanMemberDTO) -> Boolean? = {
            AstraClansAPI.canBreak(it, freeLandDTO)
        }
        // Test free land
        assert(canBreakFreeLand(clanLeaderDTO) == null)
        assert(canBreakFreeLand(noClanMemberDTO) == null)

        // Clan leader own land
        flagDTO = updateFlag(true, flagDTO)
        assert(canBreakClanLand(clanLeaderDTO) == null)

        flagDTO = updateFlag(false, flagDTO)
        assert(canBreakClanLand(clanLeaderDTO) == null)

        // No-clan Player
        flagDTO = updateFlag(true, flagDTO)
        assert(canBreakClanLand(noClanMemberDTO) == true)

        flagDTO = updateFlag(false, flagDTO)
        assert(canBreakClanLand(noClanMemberDTO) == false)

    }
}