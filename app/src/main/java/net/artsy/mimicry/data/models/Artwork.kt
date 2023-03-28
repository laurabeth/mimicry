package net.artsy.mimicry.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Artwork(val title: String, val id: String) {}

@Serializable
data class ArtworkEdge(val node: Artwork)

@Serializable
data class ArtworksForUser(val edges: Array<ArtworkEdge>)
