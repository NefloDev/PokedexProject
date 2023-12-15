package com.example.pokedexproject.entities.common

import com.google.gson.annotations.SerializedName

//Data class of Sprites, that can contain a default url or other kind
data class Sprites(
    val default : String,
    val other: Other
)

data class Other(
    @SerializedName("official-artwork")
    val official_artwork: OfficialArtwork
)
data class OfficialArtwork(val front_default: String)