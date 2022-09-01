import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.dto.LandDTO
import java.util.*
import kotlin.random.Random

const val IN_MEMORY = "file:test?mode=memory&cache=shared"
const val REAL_DB = "C:\\Users\\makee\\Desktop\\data.db"

object DTO {
    val ClanDTO: ClanDTO
        get() = ClanDTO(
            leaderUUID = randomize(),
            leaderName = randomize(),
            clanName = randomize(),
            clanTag = randomize()
        )
    val ClanMemberDTO: ClanMemberDTO
        get() = ClanMemberDTO(
            minecraftName = randomize(),
            minecraftUUID = randomize(),
        )

    val LandDTO: LandDTO
        get() = LandDTO(
            x = randomize(),
            z = randomize(),
            worldName = randomize()
        )

}


inline fun <reified T> randomize(): T = when (T::class.java) {
    Int::class.java -> Random.nextInt(Int.MAX_VALUE) as T
    Long::class.java -> Random.nextInt(Int.MAX_VALUE).toLong() as T
    String::class.java -> UUID.randomUUID().toString() as T
    Double::class.java -> Random.nextDouble(Double.MAX_VALUE) as T
    Float::class.java -> Random.nextDouble(Double.MAX_VALUE).toFloat() as T
    Char::class.java -> UUID.randomUUID().toString()[0] as T
    Boolean::class.java -> Random.nextBoolean() as T
    java.lang.Boolean::class.java -> Random.nextBoolean() as T
    java.lang.Integer::class.java -> Random.nextInt(Int.MAX_VALUE) as T
    else -> throw Exception("Unknown type ${T::class.java}")
}