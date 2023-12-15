package com.example.pokedexproject.entities.items

import com.example.pokedexproject.entities.common.DataSimple
import com.example.pokedexproject.entities.common.Sprites
import com.google.gson.annotations.SerializedName

//Data class of the object Item containing the attributes being used in this app
data class Item(
    val name : String,
    val id : Int,
    val cost : Int,
    val sprites : Sprites,
    @SerializedName("effect_entries")
    val effects : List<Effect>,
    @SerializedName("held_by_pokemon")
    val pokemons : List<Pok>,
    val category : DataSimple
)

data class Effect(
    val effect : String
)

data class Pok(
    val pokemon : DataSimple
)