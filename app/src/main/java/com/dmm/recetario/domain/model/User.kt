package com.dmm.recetario.domain.model

open class User (
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val phone: String?,
    val username: String?,
    val icon: String?,

    val recipes: List<String>?
) {
    override fun toString(): String = "User(id=$id, name=$name, email=$email, role=$role, phone=$phone, username=$username, icon=$icon, recipes=$recipes)"
}

class AnonymousUser : User (
    id = "No ID",
    name = "Anónimo",
    email = "No email",
    role = "No role",
    phone = null,
    username = null,
    icon = null,
    recipes = null
)