package com.astrainteractive.astraclans.config.translation

import com.astrainteractive.astralibs.file_manager.FileManager
import org.bukkit.command.CommandSender

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

    object Colors {
        const val negative = "#ca1e1b"
        const val positive = "#2aca1b"
        const val default = "#1b6aca"
    }

    override fun onCreate() {
        instance = this
    }

    //General
    val prefix: String = getHEXString("general.prefix", "${Colors.positive}[EmpireItems]")
    val reload: String = getHEXString("general.reload", "${Colors.default}Перезагрузка плагина")
    val reloadComplete: String =
        getHEXString("general.reload_complete", "${Colors.positive}Перезагрузка успешно завершена")
    val noPermission: String =
        getHEXString("general.no_permission", "${Colors.negative}У вас нет прав!")
    val notPlayer = translationDetails {
        path = "general.not_a_player"
        default = "${Colors.negative}Вы не игрок"
    }
    val playerNotOnline = translationDetails {
        path = "general.player_not_online"
        default = "${Colors.negative}Игрок не в сети"
    }
    val playerAlreadyInvited = translationDetails {
        path = "general.player_already_invited"
        default = "${Colors.negative}Игрок уже приглашен"
    }
    val youNotInvited = translationDetails {
        path = "general.not_invited"
        default = "${Colors.negative}Вы не приглашены"
    }
    val joinedClan = translationDetails {
        path = "general.joined_clan"
        default = "${Colors.positive}Вы вступили в клан %clan%"
    }

    val playerInvited = translationDetails {
        path = "general.player_invited"
        default = "${Colors.positive}Игрок %player% приглашен в клан"
    }
    val clanNotFound = translationDetails {
        path = "general.clan_not_found"
        default = "${Colors.negative}Клан %clan% не найден"
    }

    // Create clan
    val noClanTagProvided = translationDetails {
        path = "clan.no_tag_provided"
        default = "${Colors.negative}Вы не ввели тэг клана /aclan create <tag> <name>"
    }
    val noClanNameProvided = translationDetails {
        path = "clan.no_name_provided"
        default = "${Colors.negative}Вы не ввели имя клана /aclan create <tag> <name>"
    }
    val playerAlreadyInClan = translationDetails {
        path = "clan.player_already_in_clan"
        default = "${Colors.negative}У вас уже есть клан"
    }
    val errorInClanCreating = translationDetails {
        path = "clan.create_clan_error"
        default = "${Colors.negative}Не удалось создать клан"
    }
    val databaseError = translationDetails {
        path = "database.error"
        default = "${Colors.negative}Ошибка базы данных"
    }
    val notClanMember = translationDetails {
        path = "clan.not_clan_member"
        default = "${Colors.negative}Вы не состоите в клане"
    }
    val successDisband = translationDetails {
        path = "clan.disband_success"
        default = "${Colors.positive}Клан успешно распущен"
    }
    val successLeave = translationDetails {
        path = "clan.leave_success"
        default = "${Colors.positive}Вы покинули клан"
    }
    val leaderCantLeaveClan = translationDetails {
        path = "clan.leader_cant_leave"
        default = "${Colors.negative}Лидер не может покинуть клан"
    }
    val successClanCreate = translationDetails {
        path = "clan.create_clan_success"
        default = "${Colors.positive}Клан %tag% успешно создан"
    }
    val youAreNotLeader = translationDetails {
        path = "clan.you_are_not_leader"
        default = "${Colors.negative}Вы не лидер клана"
    }

    val flagChanged = translationDetails {
        path = "clan.flag_changed"
        default = "${Colors.positive}Флаг %flag% изменен на %value%"
    }
    val chunkAlreadyClaimed = translationDetails {
        path = "clan.chunk_already_claimed"
        default = "${Colors.negative}Этот чанк уже занят"
    }
    val chunkClaimError = translationDetails {
        path = "clan.chunk_claim_error"
        default = "${Colors.negative}Не удалось захватить эти земли"
    }
    val chunkClaimSuccess = translationDetails {
        path = "clan.chunk_claim_success"
        default = "${Colors.positive}Земли захвачены"
    }
    val noFlagProvided = translationDetails {
        path = "clan.no_flag_provided"
        default = "${Colors.negative}Введите флаг"
    }
    val previous = translationDetails {
        path = "gui.back"
        default = "${Colors.default}Раньше"
    }
    val next = translationDetails {
        path = "gui.next"
        default = "${Colors.default}Дальше"
    }
    val back = translationDetails {
        path = "gui.back"
        default = "${Colors.default}Назад"
    }

    // Flags

    val flagTrue = translationDetails(
        path = "clan.flag.values.true",
        default = "${Colors.positive}Вкл."
    )

    val flagFalse = translationDetails(
        path = "clan.flag.values.false",
        default = "${Colors.negative}Откл."
    )

    val flagIsEnabled = translationDetails(
        path = "clan.flag.values.value",
        default = "${Colors.default}Значение: "
    )

    val flagBlockBreak = translationDetails(
        path = "clan.flag.block_break",
        default = "${Colors.default}Запремт ломать блоки"
    )
    val flagBlockPlace = translationDetails(
        path = "clan.flag.block_place",
        default = "${Colors.default}Запрет ставить блоки"
    )
    val flagBlockIgnite = translationDetails(
        path = "clan.flag.block_ignite",
        default = "${Colors.default}Запрет огня"
    )
    val flagCreatureSpawn = translationDetails(
        path = "clan.flag.creature_spawn",
        default = "${Colors.default}Запрет спавна животных"
    )
    val flagBlockGrowDeny = translationDetails(
        path = "clan.flag.block_grow",
        default = "${Colors.default}Запрет роста"
    )
    val flagBlockExplode = translationDetails(
        path = "clan.flag.block_explode",
        default = "${Colors.default}Запрет взрывов"
    )
    val flagBlockPiston = translationDetails(
        path = "clan.flag.block_piston",
        default = "${Colors.default}Запрет прошней"
    )
    val flagBlockDamage = translationDetails(
        path = "clan.flag.block_damage",
        default = "${Colors.default}Запрет урона блокам"
    )
    val flagBlockInteract = translationDetails(
        path = "clan.flag.block_interact",
        default = "${Colors.default}Запрет взаимодействия с блоками"
    )
    val flagBlockForm = translationDetails(
        path = "clan.flag.block_form",
        default = "${Colors.default}Запрет формы блоков"
    )
    val flagBlockFertilize = translationDetails(
        path = "clan.flag.block_fertilize",
        default = "${Colors.default}Запрет Fertilize"
    )
    val flagSignChange = translationDetails(
        path = "clan.flag.sign_change",
        default = "${Colors.default}Запрет смены табличек"
    )
    val flagBucketEmpty = translationDetails(
        path = "clan.flag.bucket_empty",
        default = "${Colors.default}Запрет опустошения ведра"
    )
    val flagBucketFill = translationDetails(
        path = "clan.flag.bucket_fill",
        default = "${Colors.default}Запрет наполнения ведра"
    )
    val flagHangingPlace = translationDetails(
        path = "clan.flag.hanging_place",
        default = "${Colors.default}Запрет вешанья"
    )
    val flagHangingBreak = translationDetails(
        path = "clan.flag.hanging_break",
        default = "${Colors.default}Запрет снимания повешенных объектов"
    )

}