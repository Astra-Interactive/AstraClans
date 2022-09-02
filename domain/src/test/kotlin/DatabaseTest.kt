import com.astrainteractive.astraclans.domain.DatabaseModule
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
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

}