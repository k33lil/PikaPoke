package com.khalil.poke.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.khalil.poke.domain.model.PokemonResult
import com.khalil.poke.domain.usecases.PokemonListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonUseCase: PokemonListUseCase,
) :
    ViewModel() {

    private var currentResult: Flow<PagingData<PokemonResult>>? = null
    fun getPokemons(): Flow<PagingData<PokemonResult>> {
        val newResult: Flow<PagingData<PokemonResult>> =
            getPokemonUseCase().cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
    }
}