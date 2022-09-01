package com.astrainteractive.astraclans.domain.entities

import com.astrainteractive.astraclans.domain.dto.FlagsEnum
import com.astrainteractive.astraclans.domain.entities.Flag.uniqueIndex
import com.astrainteractive.astraclans.domain.entities.Land.uniqueIndex
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column


object Flag : IntIdTable() {
    val flag: Column<FlagsEnum> = enumeration<FlagsEnum>("protection_flag")
    val enabled: Column<Boolean> = bool("enabled")
    val clanID = reference("clan_id", Clan)

    init {
        uniqueIndex(flag, clanID)
    }
}

class FlagDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FlagDAO>(Flag)

    var flag by Flag.flag
    var enabled by Flag.enabled
    var clanID by Flag.clanID
}