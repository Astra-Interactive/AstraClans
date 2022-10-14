package com.astrainteractive.astraclans.gui.flags

import com.astrainteractive.astraclans.commands.clan.ClanCommandController
import com.astrainteractive.astraclans.config.translation.sendTranslationMessage
import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.dto.FlagDTO
import com.astrainteractive.astraclans.domain.dto.FlagsEnum
import com.astrainteractive.astraclans.utils.toDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.astrainteractive.astralibs.async.BukkitMain
import ru.astrainteractive.astralibs.async.PluginScope
import ru.astrainteractive.astralibs.di.Injector.inject
import ru.astrainteractive.astralibs.menu.AstraPlayerMenuUtility
import ru.astrainteractive.astralibs.utils.uuid


class FlagInventoryPresenter(private val playerMenuUtility: AstraPlayerMenuUtility, private val view: IFlagView) {
    val playerClan = AstraClansAPI.getPlayerClan(playerMenuUtility.player.toDTO())
    val clanCommandController: ClanCommandController = inject()!!
    private val _flagList: MutableMap<FlagsEnum, FlagDTO> = playerClan?.flags?.associateBy {
        it.flag
    }?.toMutableMap()?.apply {
        FlagsEnum.values().forEach {
            if (!this.containsKey(it))
                this[it] = FlagDTO(clanID = playerClan.id, flag = it, enabled = false)
        }
    } ?: mutableMapOf()
    val flagList: List<FlagDTO>
        get() = _flagList.values.toList()

    init {
        if (playerClan == null) {
            playerMenuUtility.player.sendTranslationMessage { notClanMember }
            view.close()
        } else if (playerClan.leaderUUID != playerMenuUtility.player.uuid) {
            playerMenuUtility.player.sendTranslationMessage { youAreNotLeader }
            view.close()
        }
        view.showFlags(flagList)
    }

    fun onFlagClicked(index: Int, page: Int, maxPerPage: Int) {
        val i = index + maxPerPage * page
        val flag = _flagList.values.elementAtOrNull(i)?.let {
            it.copy(enabled = !it.enabled)
        } ?: return
        PluginScope.launch {
            val result = clanCommandController.setFlag(playerMenuUtility.player, flag.flag, flag.enabled) ?: return@launch
            _flagList[flag.flag] = result

            PluginScope.launch(Dispatchers.BukkitMain) { view.showFlags(flagList) }
        }
    }

}