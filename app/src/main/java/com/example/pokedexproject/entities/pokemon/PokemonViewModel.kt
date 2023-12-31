package com.example.pokedexproject.entities.pokemon

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.pokedexproject.entities.common.DataList
import com.example.pokedexproject.entities.common.DataSimple
import com.example.pokedexproject.entities.common.DataSimpleList
import com.example.pokedexproject.entities.common.PokeApiClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokemonViewModel(application: Application) : AndroidViewModel(application) {
    private val listManager = DataSimpleList()
    val pokemonMutableLiveData = MutableLiveData<ArrayList<DataSimple>>()
    val selectedPokemon = MutableLiveData<Pokemon>()
    //Client that calls pokeapi using retrofit and GsonConverter
    private var apiClient = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(PokeApiClient::class.java)
    private var i = 0
    //Initialization of values
    fun initalize(){
        apiClient = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(PokeApiClient::class.java)
        i = 0
        listManager.reset(object : DataSimpleList.Callback {
            override fun whenFinished(pokemon: ArrayList<DataSimple>) {
                pokemonMutableLiveData.value!!.clear()
            }
        })
    }
    fun obtain() : MutableLiveData<ArrayList<DataSimple>>{
        return pokemonMutableLiveData
    }

    fun startSearch(){
        //1.- Get data from the base Pokemon url
        apiClient.getPokemonList(1).enqueue(object : Callback<DataList>{
            override fun onResponse(call: Call<DataList>, response: Response<DataList>) {
                if(response.isSuccessful && response.body()!=null){
                    //2.- Retrieve the list of all the pokemon in the api using "count" to get its limit
                    apiClient.getPokemonList(response.body()!!.count).enqueue(object : Callback<DataList>{
                        override fun onResponse(call: Call<DataList>, response: Response<DataList>) {
                            if (response.isSuccessful && response.body()!=null){
                                //3.- Each element in the list is added into the mutable list
                                while (i < response.body()!!.count){
                                    val data = response.body()!!.results[i]
                                    listManager.insert(data, object : DataSimpleList.Callback{
                                        override fun whenFinished(pokemon: ArrayList<DataSimple>) {
                                            pokemonMutableLiveData.value = pokemon
                                        }
                                    })
                                    i++;
                                }
                            }
                        }
                        override fun onFailure(call: Call<DataList>, t: Throwable) { Log.ERROR }
                    })
                }
            }
            override fun onFailure(call: Call<DataList>, t: Throwable) { Log.ERROR }
        })
    }

    fun select(name : String){
        //Retrieves the pokemon searched by its name
        apiClient.getPokemon(name).enqueue(object : Callback<Pokemon>{
            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                if (response.isSuccessful && response.body()!=null){
                    selectedPokemon.postValue(response.body()!!);
                }
            }
            override fun onFailure(call: Call<Pokemon>, t: Throwable) { Log.ERROR }

        })
    }
    fun selected() : MutableLiveData<Pokemon>{
        return selectedPokemon;
    }
}