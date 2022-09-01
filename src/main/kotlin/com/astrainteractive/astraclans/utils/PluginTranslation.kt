package com.astrainteractive.astraclans.utils

import com.astrainteractive.astralibs.FileManager
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

val Translation: PluginTranslation
    get() = PluginTranslation.instance


fun CommandSender.sendTranslationMessage(vararg args: Pair<String, Any>, msg: PluginTranslation.() -> String) {
    var msg = msg(PluginTranslation.instance)
    args.forEach { msg = msg.replace(it.first, it.second.toString()) }
    sendMessage(msg)
}

/**
 * All translation stored here
 */
class PluginTranslation(
    override val fileManager: FileManager = FileManager("translations.yml")
) : BaseTranslation() {
    /**
     * For better access better to create [instance]
     */
    companion object {
        internal lateinit var instance: PluginTranslation
    }


    override fun onCreate() {
        instance = this
    }


    //Database
    val dbSuccess = translationDetails {
        path = "database.success"
        default = "#18dbd1Успешно подключено к базе данных"
    }
    val dbFail: String = getHEXString("database.fail", "#db2c18Нет подключения к базе данных")

    //General
    val prefix: String = getHEXString("general.prefix", "#18dbd1[EmpireItems]")
    val reload: String = getHEXString("general.reload", "#dbbb18Перезагрузка плагина")
    val reloadComplete: String =
        getHEXString("general.reload_complete", "#42f596Перезагрузка успешно завершена")
    val noPermission: String =
        getHEXString("general.no_permission", "#db2c18У вас нет прав!")
    val notPlayer = translationDetails {
        path = "general.not_a_player"
        default = "Вы не игрок"
    }

    //Menu
    val menuTitle: String = getHEXString("menu.title", "#18dbd1Меню")
    val menuAddPlayer: String = getHEXString("menu.add_player", "#18dbd1Добавить игрока")
    val menuFirstPage: String = getHEXString("menu.first_page", "#dbbb18Вы на первой странице")
    val menuLastPage: String = getHEXString("menu.last_page", "#dbbb18Вы на последней странице")
    val menuPrevPage: String = getHEXString("menu.prev_page", "#18dbd1Пред. страницы")
    val menuNextPage: String = getHEXString("menu.next_page", "#18dbd1След. страница")
    val menuBack: String = getHEXString("menu.back", "#18dbd1Назад")
    val menuClose: String = getHEXString("menu.close", "#18dbd1Закрыть")

    //Custom
    val blockPlaced: String = getHEXString("custom.block_placed", "#18dbd1Блок поставлен!")
    val noPlayerName: String = getHEXString("custom.no_player_name", "#db2c18Вы не ввели имя игрока!")
    val damaged: String = getHEXString("custom.damaged", "#db2c18Вас продамажил игрок %player%!")
    val damageHint: String = getHEXString("custom.damage_hint", "<amount>")

    // Create clan
    val noClanTagProvided = translationDetails {
        path = "clan.no_tag_provided"
        default = "Вы не ввели тэг клана /aclan create <tag> <name>"
    }
    val noClanNameProvided = translationDetails {
        path = "clan.no_name_provided"
        default = "Вы не ввели имя клана /aclan create <tag> <name>"
    }
    val playerAlreadyInClan = translationDetails {
        path = "clan.player_already_in_clan"
        default = "У вас уже есть клан"
    }
    val errorInClanCreating = translationDetails {
        path = "clan.create_clan_error"
        default = "Не удалось создать клан"
    }
    val databaseError = translationDetails {
        path = "database.error"
        default = "Ошибка базы данных"
    }
    val notClanMember = translationDetails {
        path = "clan.not_clan_member"
        default = "Вы не состоите в клане"
    }
    val successClanCreate = translationDetails {
        path = "clan.create_clan_success"
        default = "Клан %tag% успешно создан"
    }
    val youAreNotLeader = translationDetails {
        path = "clan.you_are_not_leader"
        default = "Вы не лидер клана"
    }
    val chunkAlreadyClaimed = translationDetails {
        path = "clan.chunk_already_claimed"
        default = "Этот чанк уже занят"
    }
    val chunkClaimError = translationDetails {
        path = "clan.chunk_claim_error"
        default = "Не удалось захватить эти земли"
    }
    val chunkClaimSuccess = translationDetails {
        path = "clan.chunk_claim_success"
        default = "Земли захвачены"
    }
    val noFlagProvided = translationDetails {
        path = "clan.no_flag_provided"
        default = "Введите флаг"
    }
    val previous = translationDetails {
        path = "gui.back"
        default = "Раньше"
    }
    val next = translationDetails {
        path = "gui.next"
        default = "Дальше"
    }
    val back = translationDetails {
        path = "gui.back"
        default = "Назад"
    }

}