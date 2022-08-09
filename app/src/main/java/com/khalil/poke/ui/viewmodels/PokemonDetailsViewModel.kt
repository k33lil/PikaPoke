package com.khalil.poke.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.khalil.poke.data.api.NetworkResource
import com.khalil.poke.domain.usecases.PokeDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val getSinglePokemon: PokeDetailsUseCase
) : ViewModel() {

    suspend fun getSinglePokemon(url: String) = flow {
        val id = url.substringAfter("pokemon").replace("/", "").toInt()
        emit(NetworkResource.Loading)
        emit(getSinglePokemon(id))
    }

}