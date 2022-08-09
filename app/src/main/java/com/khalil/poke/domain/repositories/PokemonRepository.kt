package com.khalil.poke.domain.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.khalil.poke.data.api.PokeApi
import com.khalil.poke.data.datasource.PokeDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepository @Inject constructor(private val pokemonApi: PokeApi) : BaseRepository() {

    fun getPokemonList() = Pager(
        config = PagingConfig(enablePlaceholders = false, pageSize = 25),
        pagingSourceFactory = {
            PokeDataSource(pokemonApi)
        }
    ).flow

    suspend fun getPokeDetails(id: Int) = safeApiCall {
        pokemonApi.getPokeDetails(id)

    }


}