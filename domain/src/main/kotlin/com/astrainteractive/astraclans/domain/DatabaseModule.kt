package com.astrainteractive.astraclans.domain

import com.astrainteractive.astraclans.domain.entities.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

object DatabaseModule {


    var database: Database? = null
        private set

    private fun defaultDatabaseBuilder(path: String) =
        Database.connect("jdbc:sqlite:$path", "org.sqlite.JDBC").also {
            TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
            runBlocking {
                transaction(it) {
                    addLogger(StdOutSqlLogger)
                    val entities = buildList {
                        add(Clan)
                        add(Land)
                        add(ClanMember)
                        add(Flag)
                        add(PendingInvite)
                    }
                    entities.forEach(SchemaUtils::create)
                    entities.forEach(SchemaUtils::addMissingColumnsStatements)
                }
            }
        }

    fun createDatabase(
        path: String = "clans.db",
        databaseBuilder: (path: String) -> Database = { defaultDatabaseBuilder(it) }
    ) {
        database = databaseBuilder(path)
//        return database!!
    }
    fun close(){
        database?.connector?.let { it() }?.close()
    }
}