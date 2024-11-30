package com.becerra.diego.poketinder.data.model

data class PokemonResponse(
    val name: String,
    val url: String,
    val weight: Int) {
    fun getPokemonId() = getIdPokemonFromUrl(url)

    fun getIdPokemonFromUrl(url: String): String = url.split("/").toTypedArray()[6]

    fun getPokemonImage(): String =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${getPokemonId()}.png"
}

