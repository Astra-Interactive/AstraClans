package usacease

import REAL_DB
import com.astrainteractive.astraclans.domain.DatabaseModule
import com.astrainteractive.astraclans.domain.api.response.SetClanFlagsResponse
import com.astrainteractive.astraclans.domain.api.use_cases.SetClanFlagUseCase
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.dto.FlagDTO
import com.astrainteractive.astraclans.domain.dto.FlagsEnum
import kotlinx.coroutines.runBlocking

import kotlin.test.BeforeTest
import kotlin.test.Test

class SetClanFlagUseCase {
    @BeforeTest
    fun prepare() {
        DatabaseModule.createDatabase(REAL_DB)
    }

    @Test
    fun ChangeFlags() {
        val clanDTO = DTO.ClanDTO.run {
            ClanDataSource.insert(this)
        }
        val customMemberDTO = DTO.ClanMemberDTO
        val clanLeaderMemberDTO = ClanMemberDTO(
            minecraftName = clanDTO.leaderName, minecraftUUID = clanDTO.leaderUUID
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