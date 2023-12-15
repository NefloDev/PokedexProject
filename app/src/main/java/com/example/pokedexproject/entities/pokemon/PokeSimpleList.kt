package com.example.pokedexproject.entities.pokemon

import com.example.pokedexproject.entities.common.DataSimple

class PokeSimpleList {
    val pokemonList = ArrayList<DataSimple>()
    interface Callback{
        fun whenFinished(pokemon : ArrayList<DataSimple>)
    }
    fun obtain() : ArrayList<DataSimple>{
        return pokemonList
    }
    fun insert(pokemon : DataSimple, callback : Callback){
        pokemonList.add(pokemon)
        callback.whenFinished(pokemonList)
    }
    fun reset(callback: Callback){
        pokemonList.clear()
        callback.whenFinished(pokemonList)
    }
}