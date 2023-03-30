package net.artsy.mimicry.data.models

import kotlinx.serialization.Serializable

@Serializable
data class MetaphysicsResponse(val data: MetaphysicsData)

@Serializable
data class MetaphysicsData(val me: User, val artworksForUser: ArtworksForUser)
