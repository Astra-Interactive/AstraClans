package com.astrainteractive.astraclans.utils

import com.astrainteractive.astraclans.domain._PluginConfig
import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import github.scarsz.discordsrv.DiscordSRV
import github.scarsz.discordsrv.dependencies.jda.api.Permission
import github.scarsz.discordsrv.dependencies.jda.api.entities.IPermissionHolder
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member
import github.scarsz.discordsrv.dependencies.jda.api.entities.PermissionOverride
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.util.*

class DiscordController(
    private val discordSRV: DiscordSRV,
    private val pluginConfig: _PluginConfig
) {
    private fun findClanChannel(clanDTO: ClanDTO): TextChannel? {
        return discordSRV.mainGuild.textChannels.firstOrNull {
            it.name == clanDTO.clanTag
        }
    }

    private val clanChatMemberPermissions: EnumSet<Permission>?
        get() = EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE, Permission.MESSAGE_ATTACH_FILES)

    private suspend fun UUID.asDiscordMember(): Member? {
        val discordID = discordSRV.accountLinkManager.getDiscordId(this)
        return discordSRV.mainGuild.getMemberById(discordID)
    }

    private suspend fun createClanTextChannel(clanDTO: ClanDTO): TextChannel? {
        if (findClanChannel(clanDTO) != null) return null
        val guild = discordSRV.mainGuild
        val category = guild.getCategoryById(pluginConfig.discord.clanChat.categoryID)
        return guild.createTextChannel(clanDTO.clanTag, category).complete()
    }

    private suspend fun hideClanChannelFromMember(clanDTO: ClanDTO, member: IPermissionHolder): PermissionOverride? {
        val clanTextChannel = findClanChannel(clanDTO) ?: return null
        return clanTextChannel.createPermissionOverride(member)
            .setAllow(emptyList())
            .setDeny(Permission.VIEW_CHANNEL)
            .complete()
    }

    private suspend fun showClanChannelToMember(
        channel: TextChannel,
        member: IPermissionHolder
    ): PermissionOverride? {
        return channel.createPermissionOverride(member).setAllow(clanChatMemberPermissions).complete()
    }

    private suspend fun allowMembersViewClanChannel(clanDTO: ClanDTO) {
        val clanTextChannel = findClanChannel(clanDTO) ?: return
        clanDTO.discordMembers().map {
            coroutineScope {
                async { showClanChannelToMember(clanTextChannel, it) }
            }
        }.awaitAll()
    }

    private suspend fun ClanDTO.discordMembers(): List<Member> {
        return this.clanMember
            .map { it.minecraftUUID }
            .toMutableList()
            .apply { add(leaderUUID) }
            .mapNotNull { uuid -> UUID.fromString(uuid).asDiscordMember() }
    }

    suspend fun onClanCreated(clanDTO: ClanDTO) {
        createClanTextChannel(clanDTO)
        hideClanChannelFromMember(clanDTO, discordSRV.mainGuild.publicRole)
        allowMembersViewClanChannel(clanDTO)
    }

    suspend fun onClanDisbanded(clanDTO: ClanDTO) {
        findClanChannel(clanDTO)?.delete()?.complete()
    }

    suspend fun onMemberJoined(clanDTO: ClanDTO, clanMemberDTO: ClanMemberDTO): PermissionOverride? {
        val clanTextChannel = findClanChannel(clanDTO) ?: return null
        val member = UUID.fromString(clanMemberDTO.minecraftUUID).asDiscordMember() ?: return null
        return showClanChannelToMember(clanTextChannel, member)
    }

    suspend fun onMemberLeave(clanDTO: ClanDTO, uuid: UUID): PermissionOverride? {
        val member = uuid.asDiscordMember() ?: return null
        return hideClanChannelFromMember(clanDTO, member)
    }
}