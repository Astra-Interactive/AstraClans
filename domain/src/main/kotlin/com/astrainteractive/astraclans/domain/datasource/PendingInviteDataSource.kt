package com.astrainteractive.astraclans.domain.datasource

import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.dto.PendingInviteDTO
import com.astrainteractive.astraclans.domain.dto.mapping.map
import com.astrainteractive.astraclans.domain.entities.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object PendingInviteDataSource {
    fun select(uuid: String): List<PendingInviteDTO> {
        val result = transaction {
            PendingInviteDAO.find(PendingInvite.minecraftUUID eq uuid).map { it.map() }
        }
        return result
    }

    fun insert(pendingInviteDTO: PendingInviteDTO): PendingInviteDTO {
        val result = transaction {
            PendingInviteDAO.new {
                this.clanID = EntityID(pendingInviteDTO.clanID, Clan)
                this.minecraftUUID = pendingInviteDTO.minecraftUUID
                this.minecraftName = pendingInviteDTO.minecraftName
            }.map()
        }
        return result
    }

    fun remove(pendingInviteDTO: PendingInviteDTO): PendingInviteDTO? {
        val result = transaction {
            PendingInviteDAO.findById(pendingInviteDTO.id)?.map()
        }
        return result
    }

    fun removeAll(uuid: String) {
        val result = transaction {
            PendingInviteDAO.find(PendingInvite.minecraftUUID eq uuid).forEach {
                it.delete()
            }
        }
        return result
    }
}