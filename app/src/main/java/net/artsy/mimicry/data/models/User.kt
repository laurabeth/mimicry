package net.artsy.mimicry.data.models

import kotlinx.serialization.Serializable

@Serializable
data class User(var name: String, var email: String)
