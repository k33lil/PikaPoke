package com.khalil.poke.domain.usecases

import com.khalil.poke.domain.repositories.PokemonRepository
import javax.inject.Inject

class PokemonListUseCase @Inject constructor(
    private val repository: PokemonRepository,
) {

    operator fun invoke() = repository.getPokemonList()
}