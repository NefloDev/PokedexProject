package com.example.pokedexproject.entities

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiClient {
    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") id : String)
    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") id : Int)
}