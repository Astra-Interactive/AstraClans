package com.astrainteractive.astraclans.domain.datasource

import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.dto.mapping.map
import com.astrainteractive.astraclans.domain.entities.Clan
import com.astrainteractive.astraclans.domain.entities.ClanMember
import com.astrainteractive.astraclans.domain.entities.ClanMemberDAO
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object ClanMemberDataSource {
    fun select(uuid: String): ClanMemberDTO? {
        val result = transaction {
            ClanMemberDAO.find(ClanMember.minecraftUUID eq uuid)?.firstOrNull()?.map()
        }
        return result
    }

    fun deleteFromClan(clanDTO: ClanDTO) {
        val result = transaction {
            ClanMemberDAO.find(ClanMember.clanID eq clanDTO.id).forEach {
                it.delete()
            }
        }
        return result
    }

    fun delete(memberDTO: ClanMemberDTO): Unit? {
        val result = transaction {
            ClanMemberDAO.findById(memberDTO.id)?.delete()
        }
        return result
    }

    fun insert(clanDTO: ClanDTO, clanMemberDTO: ClanMemberDTO): ClanMemberDTO {
        val result = transaction {
            ClanMemberDAO.new {
                this.minecraftUUID = clanMemberDTO.minecraftUUID
                this.minecraftName = clanMemberDTO.minecraftName
                this.clanID = EntityID(clanDTO.id, Clan)
            }.map()
        }
        return result
    }
}