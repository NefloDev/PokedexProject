package com.example.pokedexproject.entities.common

data class DataList(
    val count : Int,
    val next : String,
    val previous : String,
    val results : ArrayList<DataSimple>
)