package com.example.pokedexproject.entities

import Pokemon

class PokemonList {

    val pokemonList = ArrayList<Pokemon>()
    interface Callback{
        fun whenFinished(pokemon : ArrayList<Pokemon>)
    }
    fun obtain() : ArrayList<Pokemon>{
        return pokemonList
    }
    fun insert(pokemon : Pokemon, callback : Callback){
        pokemonList.add(pokemon)
        callback.whenFinished(pokemonList)
    }
    fun reset(callback: Callback){
        pokemonList.clear()
        callback.whenFinished(pokemonList)
    }

}