package com.example.pokedexproject.entities.pokemon

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiClient {
    @GET("pokemon/{id}")
    fun getPokemon(@Path("id") id : Int) : Call<Pokemon>
    @GET("pokemon/")
    fun getPokemonList() : Call<PokeList>
}