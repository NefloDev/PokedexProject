package com.example.pokedexproject.entities.pokemon

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokemonViewModel(application: Application) : AndroidViewModel(application) {
    private val listManager = PokemonList()
    val pokemonMutableLiveData = MutableLiveData<ArrayList<Pokemon>>()
    val selectedPokemon = MutableLiveData<Pokemon>()
    private lateinit var pokemonList : List<String>
    private var apiClient = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(PokeApiClient::class.java)
    private var thread : Job? = null
    private var finished = false
    private var i = 1
    private fun initalize(){
        apiClient = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(PokeApiClient::class.java)
        finished = false
        i = 1
        listManager.reset(object : PokemonList.Callback {
            override fun whenFinished(pokemon: ArrayList<Pokemon>) {
                pokemonMutableLiveData.value!!.clear()
            }

        })
    }
    fun obtain() : MutableLiveData<ArrayList<Pokemon>>{
        return pokemonMutableLiveData
    }

    fun startSearch(){
        thread = GlobalScope.launch{
            val countResponse = apiClient.getPokemonList().execute()
            if(countResponse.isSuccessful && countResponse.body()!=null){
                while(i <= countResponse.body()!!.count && !finished){
                    val response = apiClient.getPokemon(i).execute()
                    if (response.isSuccessful && response.body() != null){
                        listManager.insert(response.body()!!, object : PokemonList.Callback {
                            override fun whenFinished(pokemon: ArrayList<Pokemon>) {
                                pokemonMutableLiveData.postValue(pokemon)
                            }
                        })
                        i++
                    }else{
                        finished = true
                    }
                }
            }
        }
        thread!!.start()
    }

    fun stopThread(){
        runBlocking {
            finished = true
            thread!!.cancelAndJoin()
        }
        initalize()
    }

    fun select(element : Pokemon){
        selectedPokemon.value = element;
    }
    fun selected() : MutableLiveData<Pokemon>{
        return selectedPokemon;
    }

}