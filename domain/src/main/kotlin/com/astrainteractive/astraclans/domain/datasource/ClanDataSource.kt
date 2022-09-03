package com.astrainteractive.astraclans.domain.datasource

import com.astrainteractive.astraclans.domain.DatabaseModule
import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.mapping.map
import com.astrainteractive.astraclans.domain.entities.Clan
import com.astrainteractive.astraclans.domain.entities.ClanDAO
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction

object ClanDataSource {
    fun insert(clanDTO: ClanDTO): ClanDTO {
        val result = transaction {
            ClanDAO.new {
                this.leaderName = clanDTO.leaderName
                this.leaderUUID = clanDTO.leaderUUID
                this.clanName = clanDTO.clanName
                this.clanTAG = clanDTO.clanTag
            }.map()
        }
        return result
    }

    fun update(clanDTO: ClanDTO): ClanDTO? {
        val result = transaction {
            ClanDAO.findById(clanDTO.id)?.apply {
                this.leaderName = clanDTO.leaderName
                this.leaderUUID = clanDTO.leaderUUID
                this.clanName = clanDTO.clanName
                this.clanTAG = clanDTO.clanTag
            }?.map()
        }
        return result
    }

    fun selectAll(): List<ClanDTO> {
        val result = transaction {
            ClanDAO.all().map { it.map() }
        }
        return result
    }

    fun selectByTag(tag: String): ClanDTO? {
        val result = transaction {
            ClanDAO.find(Clan.clanTAG eq tag).firstOrNull()?.map()
        }
        return result
    }

    fun selectByID(id: Int): ClanDTO? {
        val result = transaction {
            ClanDAO.findById(id)?.map()
        }
        return result
    }

    fun select(clanDTO: ClanDTO): ClanDTO {
        return selectByID(clanDTO.id) ?: clanDTO
    }

    fun select(leaderUUID: String): ClanDTO? {
        val result = transaction {
            ClanDAO.find(Clan.leaderUUID eq leaderUUID).map {
                it.map()
            }.firstOrNull()
        }
        return result
    }

    fun delete(clanDTO: ClanDTO): Boolean {
        val result = transaction {
            ClanDAO.findById(clanDTO.id)?.delete()
        }
        return result != null
    }

}