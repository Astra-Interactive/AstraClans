package com.astrainteractive.astraclans.domain

import com.astrainteractive.astraclans.domain.dto.LandDTO

fun LandDTO.isSame(another: LandDTO): Boolean {
    return this.x == another.x && this.worldName == another.worldName && this.z == another.z
}