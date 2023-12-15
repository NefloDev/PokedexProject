package com.example.pokedexproject.entities.common

import com.example.pokedexproject.entities.pokemon.Pokemon
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiClient {
    @GET("pokemon/{id}")
    fun getPokemon(@Path("id") id : String) : Call<Pokemon>
    @GET("pokemon/")
    fun getPokemonList(@Query("limit") limit : Int) : Call<DataList>
    @GET("item/")
    fun getItemList() : Call<DataList>
    @GET("berry/")
    fun getBerryList() : Call<DataList>
}