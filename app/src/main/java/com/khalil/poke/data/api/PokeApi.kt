package com.khalil.poke.data.api

import com.khalil.poke.domain.model.PokeDetailsResponse
import com.khalil.poke.domain.model.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {
    @GET("pokemon/")
    suspend fun getPokemonList(
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?
    ): PokemonListResponse

    @GET("pokemon/{id}/")
    suspend fun getPokeDetails(
        @Path("id") id: Int
    ): PokeDetailsResponse
}