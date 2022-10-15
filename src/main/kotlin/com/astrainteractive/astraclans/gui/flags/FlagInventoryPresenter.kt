package com.astrainteractive.astraclans.gui.flags

import com.astrainteractive.astraclans.commands.clan.ClanCommandController
import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.dto.FlagDTO
import com.astrainteractive.astraclans.domain.dto.FlagsEnum
import com.astrainteractive.astraclans.modules.translation.sendTranslationMessage
import com.astrainteractive.astraclans.utils.toDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.astrainteractive.astralibs.architecture.Presenter
import ru.astrainteractive.astralibs.async.BukkitMain
import ru.astrainteractive.astralibs.async.PluginScope
import ru.astrainteractive.astralibs.menu.IPlayerHolder
import ru.astrainteractive.astralibs.utils.uuid


class FlagInventoryPresenter(private val playerMenuUtility: IPlayerHolder, override val viewState: IFlagView) :
    Presenter<IFlagView>() {
    val playerClan = AstraClansAPI.getPlayerClan(playerMenuUtility.player.toDTO())
    val clanCommandController: ClanCommandController = ClanCommandController
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

    override fun onBinded() {
        if (playerClan == null) {
            playerMenuUtility.player.sendTranslationMessage { notClanMember }
            viewState.close()
        } else if (playerClan.leaderUUID != playerMenuUtility.player.uuid) {
            playerMenuUtility.player.sendTranslationMessage { youAreNotLeader }
            viewState.close()
        }
        viewState.showFlags(flagList)

    }

    fun onFlagClicked(index: Int, page: Int, maxPerPage: Int) {
        val i = index + maxPerPage * page
        val flag = _flagList.values.elementAtOrNull(i)?.let {
            it.copy(enabled = !it.enabled)
        } ?: return
        PluginScope.launch {
            val result =
                clanCommandController.setFlag(playerMenuUtility.player, flag.flag, flag.enabled) ?: return@launch
            _flagList[flag.flag] = result

            PluginScope.launch(Dispatchers.BukkitMain) { viewState.showFlags(flagList) }
        }
    }

}