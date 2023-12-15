package com.example.pokedexproject.entities.common

import com.example.pokedexproject.entities.items.Item
import com.example.pokedexproject.entities.pokemon.Pokemon
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//Client interface that declares functions for each use
interface PokeApiClient {
    //Retrieves a pokemon using an id
    //https://pokeapi.co/api/v2/pokemon/{id}
    @GET("pokemon/{id}")
    fun getPokemon(@Path("id") id : String) : Call<Pokemon>
    //Retrieves the list of pokemon with a limit of elements
    //https://pokeapi.co/api/v2/pokemon/?limit={limit}
    @GET("pokemon/")
    fun getPokemonList(@Query("limit") limit : Int) : Call<DataList>
    //Retrieves an item using an id
    //https://pokeapi.co/api/v2/item/{id}
    @GET("item/{id}")
    fun getItem(@Path("id") id : String) : Call<Item>
    //Retrieves the list of items with a limit of elements
    //https://pokeapi.co/api/v2/item/?limit={limit}
    @GET("item/")
    fun getItemList(@Query("limit") limit : Int) : Call<DataList>
}