package net.artsy.mimicry.data.models

import kotlinx.serialization.Serializable


@Serializable
data class Artwork(val title: String, val id: String, val image: Image) {}

@Serializable
data class Image(val aspectRatio: Int, val url: String) {}

@Serializable
data class ArtworkEdge(val node: Artwork)

@Serializable
data class ArtworksForUser(val edges: List<ArtworkEdge>)

@Serializable
data class Artist(val name: String) {

}
