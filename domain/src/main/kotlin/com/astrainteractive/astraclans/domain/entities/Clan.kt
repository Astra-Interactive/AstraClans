package com.astrainteractive.astraclans.domain.entities

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.and

object Clan : IntIdTable() {
    val leaderName: Column<String> = text("minecraft_name")
    val leaderUUID: Column<String> = text("minecraft_uuid")
    val clanName: Column<String> = text("clan_name")
    val clanTAG: Column<String> = text("clan_tag")
    init {
        uniqueIndex(leaderName, leaderUUID)
    }
}
fun aaa(){

    ClanDAO.find {
        Clan.clanName.eq("aaa").and(Clan.clanTAG.eq("Tag"))
    }
}
class ClanDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ClanDAO>(Clan)

    var leaderName by Clan.leaderName
    var leaderUUID by Clan.leaderUUID
    var clanName by Clan.clanName
    var clanTAG by Clan.clanTAG
    val lands by LandDAO referrersOn Land.clanID
    val members by ClanMemberDAO referrersOn ClanMember.clanID
    val protectionFlags by FlagDAO referrersOn Flag.clanID
}