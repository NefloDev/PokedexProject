package com.example.pokedexproject.entities.pokemon

import com.example.pokedexproject.entities.common.DataSimple
import com.example.pokedexproject.entities.common.Sprites
//Data class of the object Pokemon containing the attributes being used in this app
data class Pokemon(
    val id: Int,
    val name: String,
    val sprites: Sprites,
    val stats: List<Stat>,
    val types: List<PokemonType>,
    val height: Double,
    val weight: Double,
    val base_experience : Int,
    var bg : Int?
)

data class Stat(val base_stat: Int, val stat: DataSimple)

data class PokemonType(val slot: Int, val type: DataSimple)
