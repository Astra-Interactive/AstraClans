package com.astrainteractive.astraclans.utils

import com.astrainteractive.astraclans.modules.TranslationProvider
import org.bukkit.command.CommandSender
import ru.astrainteractive.astralibs.file_manager.FileManager
import ru.astrainteractive.astralibs.utils.BaseTranslation

fun CommandSender.sendTranslationMessage(vararg args: Pair<String, Any>, msg: PluginTranslation.() -> String) {
    var msg = msg(TranslationProvider.value)
    args.forEach { msg = msg.replace(it.first, it.second.toString()) }
    sendMessage(msg)
}

/**
 * All translation stored here
 */
class PluginTranslation: BaseTranslation() {
    override val translationFile: FileManager = FileManager("translations.yml")

    object Colors {
        const val negative = "#ca1e1b"
        const val positive = "#2aca1b"
        const val default = "#1b6aca"
    }
    //General
    val prefix: String = translationValue("general.prefix", "${Colors.positive}[EmpireItems]")
    val reload: String = translationValue("general.reload", "${Colors.default}Перезагрузка плагина")
    val reloadComplete: String =
        translationValue("general.reload_complete", "${Colors.positive}Перезагрузка успешно завершена")
    val noPermission: String =
        translationValue("general.no_permission", "${Colors.negative}У вас нет прав!")
    val notPlayer = translationValue(
        path = "general.not_a_player",
        default = "${Colors.negative}Вы не игрок"
    )
    val playerNotOnline = translationValue(
        path = "general.player_not_online",
        default = "${Colors.negative}Игрок не в сети"
    )
    val playerAlreadyInvited = translationValue(
        path = "general.player_already_invited",
        default = "${Colors.negative}Игрок уже приглашен"
    )
    val youNotInvited = translationValue(
        path = "general.not_invited",
        default = "${Colors.negative}Вы не приглашены"
    )
    val joinedClan = translationValue(
        path = "general.joined_clan",
        default = "${Colors.positive}Вы вступили в клан %clan%"
    )

    val playerInvited = translationValue(
        path = "general.player_invited",
        default = "${Colors.positive}Игрок %player% приглашен в клан"
    )
    val clanNotFound = translationValue(
        path = "general.clan_not_found",
        default = "${Colors.negative}Клан %clan% не найден"
    )

    // Create clan
    val noClanTagProvided = translationValue(
        path = "clan.no_tag_provided",
        default = "${Colors.negative}Вы не ввели тэг клана /aclan create <tag> <name>"
    )
    val noClanNameProvided = translationValue(
        path = "clan.no_name_provided",
        default = "${Colors.negative}Вы не ввели имя клана /aclan create <tag> <name>"
    )
    val playerAlreadyInClan = translationValue(
        path = "clan.player_already_in_clan",
        default = "${Colors.negative}У вас уже есть клан"
    )
    val notEnoguhMoney = translationValue(
        path = "clan.not_enough_money",
        default = "${Colors.negative}Недостаточно денег"
    )
    val errorInClanCreating = translationValue(
        path = "clan.create_clan_error",
        default = "${Colors.negative}Не удалось создать клан"
    )
    val databaseError = translationValue(
        path = "database.error",
        default = "${Colors.negative}Ошибка базы данных"
    )
    val notClanMember = translationValue(
        path = "clan.not_clan_member",
        default = "${Colors.negative}Вы не состоите в клане"
    )
    val successDisband = translationValue(
        path = "clan.disband_success",
        default = "${Colors.positive}Клан успешно распущен"
    )
    val successLeave = translationValue(
        path = "clan.leave_success",
        default = "${Colors.positive}Вы покинули клан"
    )
    val leaderCantLeaveClan = translationValue(
        path = "clan.leader_cant_leave",
        default = "${Colors.negative}Лидер не может покинуть клан"
    )
    val youAreLeader = translationValue(
        path = "clan.you_leader",
        default = "${Colors.negative}Вы лидер клана. Это действие вам недоступно"
    )
    val successClanCreate = translationValue(
        path = "clan.create_clan_success",
        default = "${Colors.positive}Клан %tag% успешно создан"
    )
    val youAreNotLeader = translationValue(
        path = "clan.you_are_not_leader",
        default = "${Colors.negative}Вы не лидер клана"
    )

    val flagChanged = translationValue(
        path = "clan.flag_changed",
        default = "${Colors.positive}Флаг %flag% изменен на %value%"
    )
    val chunkAlreadyClaimed = translationValue(
        path = "clan.chunk_already_claimed",
        default = "${Colors.negative}Этот чанк уже занят"
    )
    val chunkClaimError = translationValue(
        path = "clan.chunk_claim_error",
        default = "${Colors.negative}Не удалось захватить эти земли"
    )
    val chunkClaimSuccess = translationValue(
        path = "clan.chunk_claim_success",
        default = "${Colors.positive}Земли захвачены"
    )
    val noFlagProvided = translationValue(
        path = "clan.no_flag_provided",
        default = "${Colors.negative}Введите флаг"
    )
    val previous = translationValue(
        path = "gui.back",
        default = "${Colors.default}Раньше"
    )
    val next = translationValue(
        path = "gui.next",
        default = "${Colors.default}Дальше"
    )
    val back = translationValue(
        path = "gui.back",
        default = "${Colors.default}Назад"
    )

    // Flags

    val flagTrue = translationValue(
        path = "clan.flag.values.true",
        default = "${Colors.positive}Вкл."
    )

    val flagFalse = translationValue(
        path = "clan.flag.values.false",
        default = "${Colors.negative}Откл."
    )

    val flagIsEnabled = translationValue(
        path = "clan.flag.values.value",
        default = "${Colors.default}Значение: "
    )

    val flagBlockBreak = translationValue(
        path = "clan.flag.block_break",
        default = "${Colors.default}Запремт ломать блоки"
    )
    val flagBlockPlace = translationValue(
        path = "clan.flag.block_place",
        default = "${Colors.default}Запрет ставить блоки"
    )
    val flagBlockIgnite = translationValue(
        path = "clan.flag.block_ignite",
        default = "${Colors.default}Запрет огня"
    )
    val flagCreatureSpawn = translationValue(
        path = "clan.flag.creature_spawn",
        default = "${Colors.default}Запрет спавна животных"
    )
    val flagBlockGrowDeny = translationValue(
        path = "clan.flag.block_grow",
        default = "${Colors.default}Запрет роста"
    )
    val flagBlockExplode = translationValue(
        path = "clan.flag.block_explode",
        default = "${Colors.default}Запрет взрывов"
    )
    val flagBlockPiston = translationValue(
        path = "clan.flag.block_piston",
        default = "${Colors.default}Запрет прошней"
    )
    val flagBlockDamage = translationValue(
        path = "clan.flag.block_damage",
        default = "${Colors.default}Запрет урона блокам"
    )
    val flagBlockInteract = translationValue(
        path = "clan.flag.block_interact",
        default = "${Colors.default}Запрет взаимодействия с блоками"
    )
    val flagBlockForm = translationValue(
        path = "clan.flag.block_form",
        default = "${Colors.default}Запрет формы блоков"
    )
    val flagBlockFertilize = translationValue(
        path = "clan.flag.block_fertilize",
        default = "${Colors.default}Запрет Fertilize"
    )
    val flagSignChange = translationValue(
        path = "clan.flag.sign_change",
        default = "${Colors.default}Запрет смены табличек"
    )
    val flagBucketEmpty = translationValue(
        path = "clan.flag.bucket_empty",
        default = "${Colors.default}Запрет опустошения ведра"
    )
    val flagBucketFill = translationValue(
        path = "clan.flag.bucket_fill",
        default = "${Colors.default}Запрет наполнения ведра"
    )
    val flagHangingPlace = translationValue(
        path = "clan.flag.hanging_place",
        default = "${Colors.default}Запрет вешанья"
    )
    val flagHangingBreak = translationValue(
        path = "clan.flag.hanging_break",
        default = "${Colors.default}Запрет снимания повешенных объектов"
    )

}