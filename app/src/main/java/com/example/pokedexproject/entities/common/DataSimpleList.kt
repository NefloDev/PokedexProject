package com.example.pokedexproject.entities.common

//Class that manages an arraylist of DataSimple objects, using a callback meant to update another list
class DataSimpleList {
    private val elementList = ArrayList<DataSimple>()
    interface Callback{
        fun whenFinished(items : ArrayList<DataSimple>)
    }
    fun obtain() : ArrayList<DataSimple>{ return elementList }
    fun insert(element : DataSimple, callback : Callback){
        elementList.add(element)
        callback.whenFinished(elementList)
    }
    fun reset(callback: Callback){
        elementList.clear()
        callback.whenFinished(elementList)
    }
}