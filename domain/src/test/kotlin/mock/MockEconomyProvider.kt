package mock

import ru.astrainteractive.astralibs.utils.economy.IEconomyProvider
import java.util.*

object MockEconomyProvider:IEconomyProvider {
    override fun addMoney(uuid: UUID, amount: Double): Boolean = true

    override fun getBalance(uuid: UUID): Double? = Double.MAX_VALUE

    override fun takeMoney(uuid: UUID, amount: Double): Boolean = true

}