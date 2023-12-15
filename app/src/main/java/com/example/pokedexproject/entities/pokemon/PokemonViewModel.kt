package com.example.pokedexproject.entities.pokemon

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.pokedexproject.entities.common.DataList
import com.example.pokedexproject.entities.common.DataSimple
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
    private val listManager = PokeSimpleList()
    val pokemonMutableLiveData = MutableLiveData<ArrayList<DataSimple>>()
    val selectedPokemon = MutableLiveData<Pokemon>()
    private var apiClient = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(PokeApiClient::class.java)
    private var thread : Job? = null
    private var i = 0
    fun initalize(){
        apiClient = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(PokeApiClient::class.java)
        i = 0
        listManager.reset(object : PokeSimpleList.Callback {
            override fun whenFinished(pokemon: ArrayList<DataSimple>) {
                pokemonMutableLiveData.value!!.clear()
            }

        })
    }
    fun obtain() : MutableLiveData<ArrayList<DataSimple>>{
        return pokemonMutableLiveData
    }

    fun startSearch(){
        apiClient.getPokemonList(1).enqueue(object : Callback<DataList>{
            override fun onResponse(call: Call<DataList>, response: Response<DataList>) {
                if(response.isSuccessful && response.body()!=null){
                    apiClient.getPokemonList(response.body()!!.count).enqueue(object : Callback<DataList>{
                        override fun onResponse(call: Call<DataList>, response: Response<DataList>) {
                            if (response.isSuccessful && response.body()!=null){
                                while (i < response.body()!!.count){
                                    val data = response.body()!!.results[i]
                                    listManager.insert(data, object : PokeSimpleList.Callback{
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
        apiClient.getPokemon(name).enqueue(object : Callback<Pokemon>{
            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                if (response.isSuccessful && response.body()!=null){
                    selectedPokemon.postValue(response.body()!!);
                }
            }
            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                Log.ERROR
            }

        })
    }
    fun selected() : MutableLiveData<Pokemon>{
        return selectedPokemon;
    }
}