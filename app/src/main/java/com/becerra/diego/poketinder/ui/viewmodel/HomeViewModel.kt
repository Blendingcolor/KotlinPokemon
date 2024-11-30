package com.becerra.diego.poketinder.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.becerra.diego.poketinder.data.database.PokemonDatabase
import com.becerra.diego.poketinder.data.database.entities.MyPokemonEntity
import com.becerra.diego.poketinder.data.model.PokemonResponse
import com.becerra.diego.poketinder.data.network.PokemonApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModel: ViewModel() {

    val pokemonList = MutableLiveData<List<PokemonResponse>>()

    val isLoading = MutableLiveData<Boolean>()

    val errorApi = MutableLiveData<String>()

    private val POKEMON_DATABASE_NAME = "pokemon_database"

    init {
        getAllPokemons()
    }

    private fun getAllPokemons() {
        isLoading.postValue(true)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call = getRetrofit().create(PokemonApi::class.java).getPokemons()
                if(call.isSuccessful) {
                    call.body()?.let {
                        isLoading.postValue(false)
                        pokemonList.postValue(it.results)
                    }
                }
            } catch (e: Exception) {
                errorApi.postValue(e.message)
                isLoading.postValue(false)
            }
        }
    }

    fun savePokemon(pokemonResponse: PokemonResponse, context: Context) {
        val myPokemon = MyPokemonEntity(
            name = pokemonResponse.name,
            image = pokemonResponse.getPokemonImage(),
            idPokemon = pokemonResponse.getPokemonId()
        )

        viewModelScope.launch {
            getRoomDatabase(context).getPokemonDao().insert(myPokemon)
        }
    }

    private fun getRoomDatabase(context: Context) = Room.databaseBuilder(
        context,
        PokemonDatabase::class.java,
        POKEMON_DATABASE_NAME
    ).build()

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
