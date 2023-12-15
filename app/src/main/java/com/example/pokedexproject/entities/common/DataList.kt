package com.example.pokedexproject.entities.common

//Data class of generic elements list
data class DataList(
    val count : Int,
    val results : ArrayList<DataSimple>
)