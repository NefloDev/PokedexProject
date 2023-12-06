package com.example.pokedexproject.entities

import java.net.URI

class Pokemon (
    val name : String,
    val id : Int,
    val weight : Int,
    val height : Int,
    val types : List<String>,
    val image : URI,
    val hp : Int,
    val atk : Int,
    val def : Int,
    val spd : Int,
    val baseXp : Int){}