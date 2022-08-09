package com.khalil.poke.ui.adapters

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.khalil.poke.R
import com.khalil.poke.databinding.ListItemPokemonBinding
import com.khalil.poke.domain.model.PokemonResult
import com.khalil.poke.utils.PIC_URL

class PokemonAdapter(private val navigate: (PokemonResult, Int, String?) -> Unit) :
    PagingDataAdapter<PokemonResult, PokemonAdapter.ViewHolder>(
        PokemonDiffCallback()
    ) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = getItem(position)!!

        holder.bind(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ListItemPokemonBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    inner class ViewHolder(
        private val binding: ListItemPokemonBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        var dominantColor: Int = 0
        var picture: String? = ""

        fun bind(pokemonResult: PokemonResult) {
            binding.apply {
                pokemonItemTitle.text = pokemonResult.name.replaceFirstChar { it.titlecase() }
                loadImage(this, pokemonResult)

                root.setOnClickListener {
                    navigate.invoke(pokemonResult, dominantColor, picture)
                }
            }

        }

        private fun loadImage(binding: ListItemPokemonBinding, pokemonResult: PokemonResult) {

            val id = pokemonResult.url.substringAfter("pokemon").replace("/", "").toInt()
            picture = PIC_URL + "${id}.png"

            binding.apply {
                Glide.with(root)
                    .load(picture)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {

                            val drawable = resource as BitmapDrawable
                            val bitmap = drawable.bitmap
                            Palette.Builder(bitmap).generate {
                                it?.let { palette ->
                                    dominantColor = palette.getDominantColor(
                                        ContextCompat.getColor(
                                            root.context,
                                            R.color.white
                                        )
                                    )

                                    pokemonItemImage.setBackgroundColor(dominantColor)


                                }
                            }
                            return false
                        }

                    })
                    .into(pokemonItemImage)

            }
        }
    }

    private class PokemonDiffCallback : DiffUtil.ItemCallback<PokemonResult>() {
        override fun areItemsTheSame(oldItem: PokemonResult, newItem: PokemonResult): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PokemonResult, newItem: PokemonResult): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            2
        } else {
            1
        }
    }

}