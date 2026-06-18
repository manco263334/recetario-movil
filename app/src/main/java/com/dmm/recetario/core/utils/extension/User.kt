package com.dmm.recetario.core.utils.extension

import com.dmm.recetario.domain.model.AnonymousUser
import com.dmm.recetario.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun User?.isNeitherNullNorAnonymous(): Boolean {
    contract {
        returns(true) implies (
            this@isNeitherNullNorAnonymous != null &&
            this@isNeitherNullNorAnonymous !is AnonymousUser
        )
    }

    return this != null && this !is AnonymousUser
}

suspend fun Flow<User?>.isNeitherNullNorAnonymous(): Boolean {
    val user = this.firstOrNull()
    return user.isNeitherNullNorAnonymous()
}