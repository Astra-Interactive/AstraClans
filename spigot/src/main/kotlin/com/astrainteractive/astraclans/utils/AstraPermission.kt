package com.astrainteractive.astraclans.utils

sealed class AstraPermission(override val value: String) : IPermission {
    object Reload : AstraPermission("astra_template.reload")
    object Damage : AstraPermission("astra_template.damage")
    object CreateClan : AstraPermission("astra_clans.create_clan")
}

