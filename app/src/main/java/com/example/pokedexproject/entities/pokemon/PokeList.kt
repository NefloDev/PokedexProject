package com.example.pokedexproject.entities.pokemon

data class PokeList(
    val count : Int,
    val next : String,
    val previous : String,
    val results : ArrayList<PokemonSimple>
)