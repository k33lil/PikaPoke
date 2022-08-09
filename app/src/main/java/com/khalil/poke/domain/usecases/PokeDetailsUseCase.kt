package com.khalil.poke.domain.usecases

import com.khalil.poke.domain.repositories.PokemonRepository
import javax.inject.Inject

class PokeDetailsUseCase @Inject constructor(
    private val repository: PokemonRepository,
) {

    suspend operator fun invoke(id: Int) = repository.getPokeDetails(id)
}