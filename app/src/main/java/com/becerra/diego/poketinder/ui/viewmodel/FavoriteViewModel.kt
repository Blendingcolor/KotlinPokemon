package com.becerra.diego.poketinder.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.becerra.diego.poketinder.data.database.PokemonDatabase
import com.becerra.diego.poketinder.data.database.entities.MyPokemonEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel: ViewModel() {

    private val POKEMON_DATABASE_NAME = "pokemon_database"
    val myPokemonList = MutableLiveData<List<MyPokemonEntity>>()

    fun getMyPokemons(
        context: Context
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val myPokemons = getRoomDatabase(context).getPokemonDao().getAllPokemons()
            myPokemonList.postValue(myPokemons)
        }
    }

    fun deleteAllPokemon(
        context: Context
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            getRoomDatabase(context).getPokemonDao().deleteTable()
            myPokemonList.postValue(emptyList())
        }
    }

    private fun getRoomDatabase(context: Context) = Room.databaseBuilder(
        context,
        PokemonDatabase::class.java,
        POKEMON_DATABASE_NAME
    ).build()
}

