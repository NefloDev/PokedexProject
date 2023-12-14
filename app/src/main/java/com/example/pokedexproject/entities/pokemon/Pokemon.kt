package com.example.pokedexproject.entities.pokemon

import com.google.gson.annotations.SerializedName

data class Pokemon(
    val id: Int,
    val name: String,
    val species: Species,
    val sprites: Sprites,
    val stats: List<Stat>,
    val types: List<Type>,
    val height: Double,
    val weight: Double,
    val base_experience : Int,
    var bg : Int
)

data class Species(val name: String, val url: String)

data class Sprites(val other: Other)

data class Stat(val base_stat: Int, val stat: StatX)

data class Type(val slot: Int, val type: TypeX)

data class Other(
    @SerializedName("official-artwork")
    val official_artwork: OfficialArtwork
)

data class OfficialArtwork(val front_default: String)
data class StatX(val name: String)

data class TypeX(val name: String)