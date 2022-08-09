package com.khalil.poke.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.khalil.poke.data.api.PokeApi
import com.khalil.poke.domain.model.PokemonResult
import java.io.IOException

class PokeDataSource(private val pokemonApi: PokeApi) :
    PagingSource<Int, PokemonResult>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonResult> {
        val offset = params.key ?: 0

        val loadSize = 100
        return try {
            val Pokedata = pokemonApi.getPokemonList(loadSize, offset)

            LoadResult.Page(
                data = Pokedata.results,
                prevKey = if (offset == 0) null else offset - loadSize,
                nextKey = if (Pokedata.next == null) null else offset + loadSize
            )
        } catch (t: Throwable) {
            var exception = t

            if (t is IOException) {
                exception = IOException("Please check your connection")
            }
            LoadResult.Error(exception)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, PokemonResult>): Int? {
        return state.anchorPosition
    }
}