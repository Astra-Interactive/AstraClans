package com.astrainteractive.astraclans.domain.entities

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object PendingInvite : IntIdTable() {
    val minecraftName: Column<String> = text("minecraft_name")
    val minecraftUUID: Column<String> = text("minecraft_uuid")
    val clanID = reference("clan_id", Clan)
}

class PendingInviteDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PendingInviteDAO>(PendingInvite)

    var minecraftName by PendingInvite.minecraftName
    var minecraftUUID by PendingInvite.minecraftUUID
    var clanID by PendingInvite.clanID
}