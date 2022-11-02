

import com.astrainteractive.astraclans.domain.DatabaseModule
import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.api.use_cases.ClaimChunkUseCase
import com.astrainteractive.astraclans.domain.api.use_cases.ClanCreateUseCase
import com.astrainteractive.astraclans.domain.api.use_cases.SetClanFlagUseCase
import com.astrainteractive.astraclans.domain.config.PluginConfig
import com.astrainteractive.astraclans.domain.di.IPlayerStatusProvider
import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.dto.FlagDTO
import com.astrainteractive.astraclans.domain.dto.FlagsEnum
import kotlinx.coroutines.runBlocking
import mock.MockEconomyProvider
import kotlin.test.BeforeTest
import kotlin.test.Test

class ProtectionTest {
    var clanLeaderDTO = DTO.ClanMemberDTO
    var clanMemberDTO = DTO.ClanMemberDTO
    val noClanMemberDTO = DTO.ClanMemberDTO
    var clanDTO = DTO.ClanDTO.copy(leaderName = clanLeaderDTO.minecraftName, leaderUUID = clanLeaderDTO.minecraftUUID)
    var clanLandDTO = DTO.LandDTO
    val freeLandDTO = DTO.LandDTO
    val clanCreateUseCase = ClanCreateUseCase(PluginConfig(),MockEconomyProvider)

    @BeforeTest
    fun prepare() {
        DatabaseModule.createDatabase(REAL_DB)
        val clanDTO = ClanCreateUseCase.Params(clanDTO.clanTag, clanDTO.clanName, clanLeaderDTO).run {
            val params = this
            runBlocking { clanCreateUseCase(params) }
        }
        clanLeaderDTO = clanLeaderDTO.copy(clanID = clanDTO.id)
        clanMemberDTO = clanMemberDTO.copy(clanID = clanDTO.id)
        ClaimChunkUseCase.Params(clanLeaderDTO, clanLandDTO.copy(clanID = clanDTO.id)).also {
            val result = runBlocking { ClaimChunkUseCase(it) }
            clanLandDTO = result
        }
    }

    private fun updateFlag(value: Boolean, flagDTO: FlagDTO): FlagDTO {
        return SetClanFlagUseCase.Params(clanLeaderDTO, flagDTO.copy(enabled = value)).run {
            val param = this
            runBlocking { SetClanFlagUseCase(param) }
        }
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
        var blockBreakDTO = FlagDTO(clanID = clanDTO.id, flag = flag, enabled = false)
        // Free land
        assert(AstraClansAPI.isFlagEnabledForPlayer(noClanMemberDTO, freeLandDTO, flag) == null)
        assert(AstraClansAPI.isFlagEnabledForPlayer(noClanMemberDTO, freeLandDTO, flag) == null)
        assert(AstraClansAPI.isFlagEnabledForPlayer(clanLeaderDTO, freeLandDTO, flag) == null)
        assert(AstraClansAPI.isFlagEnabledForPlayer(clanLeaderDTO, freeLandDTO, flag) == null)
        // Clan leader on clan land
        blockBreakDTO = updateFlag(true, blockBreakDTO)
        assert(AstraClansAPI.isFlagEnabledForPlayer(clanLeaderDTO, clanLandDTO, flag) == null)
        blockBreakDTO = updateFlag(false, blockBreakDTO)
        assert(AstraClansAPI.isFlagEnabledForPlayer(clanLeaderDTO, clanLandDTO, flag) == null)
        // Custom player on clan land
        blockBreakDTO = updateFlag(true, blockBreakDTO)
        assert(AstraClansAPI.isFlagEnabledForPlayer(noClanMemberDTO, clanLandDTO, flag) == true)
        blockBreakDTO = updateFlag(false, blockBreakDTO)
        assert(AstraClansAPI.isFlagEnabledForPlayer(noClanMemberDTO, clanLandDTO, flag) == false)
        AstraClansAPI.playerStatusProvider = object : IPlayerStatusProvider {
            override fun isPlayerOnline(playerDTO: ClanMemberDTO): Boolean = false
            override fun isAnyMemberOnline(clanDTO: ClanDTO): Boolean = false
        }

        blockBreakDTO = updateFlag(true, blockBreakDTO)
        assert(AstraClansAPI.isFlagEnabledForPlayer(noClanMemberDTO, clanLandDTO, flag) == true)
        AstraClansAPI.playerStatusProvider = object : IPlayerStatusProvider {
            override fun isPlayerOnline(playerDTO: ClanMemberDTO): Boolean = true
            override fun isAnyMemberOnline(clanDTO: ClanDTO): Boolean = true
        }

        assert(AstraClansAPI.isFlagEnabledForPlayer(noClanMemberDTO, clanLandDTO, flag) == false)
    }

    @Test
    fun CheckCanBreak() {
        var flagDTO = FlagDTO(clanID = clanDTO.id, flag = FlagsEnum.BLOCK_BREAK_DENY, enabled = false)
        // TODO remake it
//        val canBreakClanLand: (ClanMemberDTO) -> Boolean? = {
//            AstraClansAPI.canBreak(it, clanLandDTO)
//        }
//        val canBreakFreeLand: (ClanMemberDTO) -> Boolean? = {
//            AstraClansAPI.canBreak(it, freeLandDTO)
//        }
//        // Test free land
//        assert(canBreakFreeLand(clanLeaderDTO) == null)
//        assert(canBreakFreeLand(noClanMemberDTO) == null)
//
//        // Clan leader own land
//        flagDTO = updateFlag(true, flagDTO)
//        assert(canBreakClanLand(clanLeaderDTO) == null)
//
//        flagDTO = updateFlag(false, flagDTO)
//        assert(canBreakClanLand(clanLeaderDTO) == null)
//
//        // No-clan Player
//        flagDTO = updateFlag(true, flagDTO)
//        assert(canBreakClanLand(noClanMemberDTO) == true)
//
//        flagDTO = updateFlag(false, flagDTO)
//        assert(canBreakClanLand(noClanMemberDTO) == false)

    }
}