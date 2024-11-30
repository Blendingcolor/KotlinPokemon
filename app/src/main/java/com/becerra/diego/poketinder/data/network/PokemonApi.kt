package com.becerra.diego.poketinder.data.network

import com.becerra.diego.poketinder.data.model.PokemonListResponse
import retrofit2.Response
import retrofit2.http.GET

interface PokemonApi {

    @GET("/api/v2/pokemon")
    suspend fun getPokemons(): Response<PokemonListResponse>
}