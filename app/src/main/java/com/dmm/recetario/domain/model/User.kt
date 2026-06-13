package com.dmm.recetario.domain.model

open class User (
    val id: String,
    val role: String,
    val name: String,
    val icon: String?,
    val email: String,
    val phone: String?,
    val username: String?,

    val recipes: List<String>?
) {
    override fun toString(): String =
        "User(id=$id, name=$name, email=$email, role=$role, phone=$phone, username=$username, " +
                "icon=$icon, recipes=$recipes)"
}

class AnonymousUser : User (
    id = "",
    role = "",
    name = "",
    email = "",
    icon = null,
    phone = null,
    recipes = null,
    username = null
)