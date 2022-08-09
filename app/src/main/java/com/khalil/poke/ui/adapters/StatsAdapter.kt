package com.khalil.poke.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khalil.poke.databinding.StatItemPokemonBinding
import com.khalil.poke.domain.model.Stats
import com.khalil.poke.utils.MAX_STATE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class StatsAdapter :
    RecyclerView.Adapter<StatsAdapter.CartViewHolder>() {
    private val stats = ArrayList<Stats>()

    fun setStats(newList: ArrayList<Stats>) {
        stats.clear()
        stats.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {

        return CartViewHolder(
            StatItemPokemonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        holder.bind(stats[position])

    }

    override fun getItemCount(): Int {
        return stats.size
    }

    class CartViewHolder(private val binding: StatItemPokemonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(stat: Stats) {
            binding.apply {

                statName.text = stat.stat.name.replaceFirstChar { it.titlecase() }

                if (stat.stat.name.contains("-")) {
                    val first =
                        stat.stat.name.substringBefore("-").replaceFirstChar { it.titlecase() }
                    val second =
                        stat.stat.name.substringAfter("-").replaceFirstChar { it.titlecase() }

                    "$first - $second".also { statName.text = it }
                }
                statCount.text = stat.base_stat.toString()

            }
        }
    }
}