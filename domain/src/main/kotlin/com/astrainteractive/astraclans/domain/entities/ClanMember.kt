package com.astrainteractive.astraclans.domain.entities

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object ClanMember : IntIdTable() {
    val minecraftName: Column<String> = text("minecraft_name").uniqueIndex()
    val minecraftUUID: Column<String> = text("minecraft_uuid").uniqueIndex()
    val clanID = reference("clan_id", Clan)
}

class ClanMemberDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ClanMemberDAO>(ClanMember)

    var minecraftName by ClanMember.minecraftName
    var minecraftUUID by ClanMember.minecraftUUID
    var clanID by ClanMember.clanID
}