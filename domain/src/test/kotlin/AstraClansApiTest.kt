import com.astrainteractive.astraclans.domain.DatabaseModule
import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.api.use_cases.ClaimChunkUseCase
import com.astrainteractive.astraclans.domain.api.use_cases.ClanCreateUseCase
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test

class AstraClansApiTest {
    var clanLeaderDTO = DTO.ClanMemberDTO
    var clanDTO = DTO.ClanDTO.copy(leaderName = clanLeaderDTO.minecraftName, leaderUUID = clanLeaderDTO.minecraftUUID)
    var clanLandDTO = DTO.LandDTO
    val freeLandDTO = DTO.LandDTO

    @BeforeTest
    fun prepare() {
        DatabaseModule.createDatabase(REAL_DB)
        val clanDTO = ClanCreateUseCase.Params(clanDTO.clanTag, clanDTO.clanName, clanLeaderDTO).run {
            val params = this
            runBlocking { ClanCreateUseCase(params) }
        }
        clanLeaderDTO = clanLeaderDTO.copy(clanID = clanDTO.id)
        ClaimChunkUseCase.Params(clanLeaderDTO, clanLandDTO.copy(clanID = clanDTO.id)).also {
            val result = runBlocking { ClaimChunkUseCase(it) }
            clanLandDTO = result
        }
    }

    @Test
    fun GetChunkClanTest() {
        var clanOnChunkID = AstraClansAPI.getChunkClan(clanLandDTO)?.id
        assert(clanOnChunkID == clanDTO.id)
        assert(clanOnChunkID == clanLandDTO.clanID)
        clanOnChunkID = AstraClansAPI.getChunkClan(freeLandDTO)?.id
        assert(clanOnChunkID == null)
    }

    @Test
    fun GetPlayerCland() {
        val playerClanID = AstraClansAPI.getPlayerClan(clanLeaderDTO)?.id
        assert(playerClanID == clanLeaderDTO.clanID)
        assert(clanDTO.id == playerClanID)
    }
}