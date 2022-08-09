package com.khalil.poke.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khalil.poke.R
import com.khalil.poke.databinding.FragmentPokemonListBinding
import com.khalil.poke.domain.model.PokemonResult
import com.khalil.poke.ui.adapters.LoadingStateAdapter
import com.khalil.poke.ui.adapters.PokemonAdapter
import com.khalil.poke.ui.viewmodels.PokemonListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("ClickableViewAccessibility")
class PokemonListFragment : Fragment(R.layout.fragment_pokemon_list) {

    private var hasInitiatedInitialCall = false
    private lateinit var binding: FragmentPokemonListBinding
    private val viewModel: PokemonListViewModel by viewModels()
    private var job: Job? = null

    private val adapter =
        PokemonAdapter { pokemonResult: PokemonResult, dominantColor: Int, picture: String? ->
            navigate(
                pokemonResult,
                dominantColor,
                picture
            )
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPokemonListBinding.bind(view)

        setAdapter()
        setRefresh()

        binding.scrollUp.setOnClickListener {
            lifecycleScope.launch {
                binding.pokemonList.scrollToPosition(0)
                delay(100)
                binding.scrollUp.toggle(false)
            }
        }

    }

    fun View.toggle(show: Boolean) {
        val transition: Transition = Slide(Gravity.BOTTOM)
        transition.duration = 200
        transition.addTarget(this)
        TransitionManager.beginDelayedTransition(this.parent as ViewGroup?, transition)
        this.isVisible = show
    }

    private fun setRefresh() {

        binding.swipeRefreshLayout.setOnRefreshListener {
            startFetchingPokemon( true)

            hideSoftKeyboard()

        }
    }


    private fun startFetchingPokemon(shouldSubmitEmpty: Boolean) {

        //collecting flow then setting to adapter
        job?.cancel()
        job = lifecycleScope.launch {
            if (shouldSubmitEmpty) adapter.submitData(PagingData.empty())
            viewModel.getPokemons().collectLatest {
                adapter.submitData(it)
            }
        }
    }


    private fun hideSoftKeyboard() {
        val view = requireActivity().currentFocus

        view?.let {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }


    }

    private fun setAdapter() {

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = adapter.getItemViewType(position)
                return if (viewType == 1) 1
                else 2
            }
        }
        binding.pokemonList.layoutManager = gridLayoutManager
        binding.pokemonList.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter { retry() }
        )

        binding.pokemonList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val scrolledPosition =
                    (recyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()

                if (scrolledPosition != null) {
                    if (scrolledPosition >= 1) {
                        binding.scrollUp.toggle(true)
                    } else {
                        binding.scrollUp.toggle(false)
                    }
                }

            }
        })

        if (!hasInitiatedInitialCall) startFetchingPokemon( false); hasInitiatedInitialCall =
            true
        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading && adapter.snapshot().isEmpty()
            ) {
                binding.progressCircular.isVisible = true
                binding.textError.isVisible = false


            } else {
                binding.progressCircular.isVisible = false
                binding.swipeRefreshLayout.isRefreshing = false

                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error

                    else -> null
                }
                if (adapter.snapshot().isEmpty()) {
                    error?.let {
                        binding.textError.visibility = View.VISIBLE
                        binding.textError.setOnClickListener {
                            adapter.retry()
                        }
                    }

                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.blue)
    }

    private fun retry() {
        adapter.retry()
    }

    private fun navigate(pokemonResult: PokemonResult, dominantColor: Int, picture: String?) {
        binding.root.findNavController()
            .navigate(
                PokemonListFragmentDirections.toPokemonStatsFragment(
                    pokemonResult,
                    picture, dominantColor
                )
            )
    }

}