package com.astrainteractive.astraclans.domain.api.use_cases

abstract class UseCase<out Type, in Params> where Type : Any {
    abstract suspend fun run(params: Params): Type?
    suspend operator fun invoke(params: Params) = run(params)
}