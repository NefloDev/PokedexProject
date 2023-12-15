package com.example.pokedexproject.entities.items

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedexproject.entities.common.DataList
import com.example.pokedexproject.entities.common.DataSimple
import com.example.pokedexproject.entities.common.DataSimpleList
import com.example.pokedexproject.entities.common.PokeApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class ItemViewModel(application: Application) : AndroidViewModel(application) {
    private val listManager = DataSimpleList()
    val itemMutableLiveData = MutableLiveData<ArrayList<DataSimple>>()
    val selectedItem = MutableLiveData<Item>()
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
            override fun whenFinished(items: ArrayList<DataSimple>) {
                itemMutableLiveData.value!!.clear()
            }

        })
    }
    fun obtain() : MutableLiveData<ArrayList<DataSimple>> {
        return itemMutableLiveData
    }

    fun startSearch(){
        //1.- Get data from the base Item url
        apiClient.getItemList(1).enqueue(object : Callback<DataList> {
            override fun onResponse(call: Call<DataList>, response: Response<DataList>) {
                if(response.isSuccessful && response.body()!=null){
                    //2.- Retrieve the list of all the item in the api using "count" to get its limit
                    apiClient.getItemList(response.body()!!.count).enqueue(object : Callback<DataList> {
                        override fun onResponse(call: Call<DataList>, response: Response<DataList>) {
                            if (response.isSuccessful && response.body()!=null){
                                while (i < response.body()!!.count){
                                    //3.- Each element in the list is added into the mutable list
                                    val data = response.body()!!.results[i]
                                    listManager.insert(data, object : DataSimpleList.Callback{
                                        override fun whenFinished(items: ArrayList<DataSimple>) {
                                            itemMutableLiveData.value = items
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
        //Retrieves the item searched by its name
        apiClient.getItem(name).enqueue(object : Callback<Item> {
            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                if (response.isSuccessful && response.body()!=null){
                    selectedItem.postValue(response.body()!!);
                }
            }
            override fun onFailure(call: Call<Item>, t: Throwable) { Log.ERROR }
        })
    }
    fun selected() : MutableLiveData<Item> {
        return selectedItem;
    }
}