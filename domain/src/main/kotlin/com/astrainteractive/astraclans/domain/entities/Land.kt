package com.astrainteractive.astraclans.domain.entities

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object Land : IntIdTable() {
    val x: Column<Int> = integer("x")
    val z: Column<Int> = integer("z")
    val worldName: Column<String> = text("world_name")
    val clanID = reference("clan_id", Clan)
}

class LandDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<LandDAO>(Land)

    var x by Land.x
    var z by Land.z
    var worldName by Land.worldName
    var clanID by Land.clanID
}