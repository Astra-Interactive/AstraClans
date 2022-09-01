package com.astrainteractive.astraclans.domain.datasource

import com.astrainteractive.astraclans.domain.dto.LandDTO
import com.astrainteractive.astraclans.domain.dto.mapping.map
import com.astrainteractive.astraclans.domain.entities.Clan
import com.astrainteractive.astraclans.domain.entities.Land
import com.astrainteractive.astraclans.domain.entities.LandDAO
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

object LandDataSource {
    fun insert(landDTO: LandDTO): LandDTO {
        val result = transaction {
            LandDAO.new {
                this.clanID = EntityID(landDTO.clanID, Clan)
                this.x = landDTO.x
                this.z = landDTO.z
                this.worldName = landDTO.worldName
            }.map()
        }
        return result
    }

    fun select(worldName:String,x:Int,z:Int): LandDTO? {
        val result = transaction {
            LandDAO.find {
                (Land.x eq x).and(Land.z eq z).and(Land.worldName eq worldName)
            }.firstOrNull()?.map()
        }
        return result
    }

    fun delete(landDTO: LandDTO): Boolean {
        val result = transaction {
            LandDAO.findById(landDTO.id)?.delete()
        }
        return result != null
    }

}