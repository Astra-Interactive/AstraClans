package com.astrainteractive.astraclans.domain.dto.mapping

const val NOT_EXISTS_ID = -1

interface Mapper<I, O> {
    fun map(it: I): O
}