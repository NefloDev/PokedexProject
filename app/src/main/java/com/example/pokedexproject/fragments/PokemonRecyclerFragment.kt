package com.example.pokedexproject.fragments

import Pokemon
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.example.pokedexproject.databinding.CardPokemonBinding
import com.example.pokedexproject.databinding.FragmentPokemonRecyclerBinding
import com.example.pokedexproject.entities.PokemonViewModel
import java.util.Locale


class PokemonRecyclerFragment : Fragment() {
    private lateinit var binding : FragmentPokemonRecyclerBinding
    private lateinit var viewModel: PokemonViewModel
    private lateinit var adapter : PokemonCardAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentPokemonRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = PokemonCardAdapter()
        binding.recycler.adapter = adapter
        viewModel = ViewModelProvider(requireActivity())[PokemonViewModel::class.java]
        viewModel.startSearch()

        viewModel.obtain().observe(viewLifecycleOwner){m ->
            if (m.isNotEmpty()){
                adapter.pokemonList.add(m[m.size-1])
                adapter.notifyItemInserted(adapter.pokemonList.size-1)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.stopThread()
        adapter.pokemonList.clear()
        binding.recycler.adapter = null
    }

    inner class PokemonCardAdapter() : RecyclerView.Adapter<PokemonCardAdapter.PokemonCardViewHolder>(){
        inner class PokemonCardViewHolder(val cardBinding : CardPokemonBinding) : ViewHolder(cardBinding.root) {}

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonCardViewHolder {
            return PokemonCardViewHolder(CardPokemonBinding.inflate(layoutInflater, parent, false))
        }

        var pokemonList : ArrayList<Pokemon> = ArrayList()

        override fun getItemCount(): Int {
            return pokemonList.size
        }

        override fun onBindViewHolder(holder: PokemonCardViewHolder, position: Int) {
            val p = pokemonList[position]

            Glide.with(requireActivity())
                .load(p.sprites.other.official_artwork.front_default)
                .placeholder(holder.cardBinding.progressBar.progressDrawable)
                .listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(e: GlideException?, model: Any?,
                                              target: Target<Drawable>?, isFirstResource: Boolean
                    ): Boolean {
                        holder.cardBinding.progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?,
                                                 target: Target<Drawable>?, dataSource: DataSource?,
                        isFirstResource: Boolean): Boolean {
                        holder.cardBinding.progressBar.visibility = View.GONE
                        return false
                    }

                })
                .into(holder.cardBinding.pokemonImage)

            Glide.with(requireActivity())
                .asBitmap()
                .load(p.sprites.other.official_artwork.front_default)
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(
                        bitmap: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap?>?
                    ) {
                        // Generate the Palette asynchronously
                        Palette.generateAsync(bitmap
                        ) { palette -> // Retrieve colors from the palette
                            val vibrantColor: Int = palette!!.getDominantColor(Color.WHITE)
                            val gradient = GradientDrawable()
                            gradient.gradientType = GradientDrawable.LINEAR_GRADIENT
                            gradient.orientation = GradientDrawable.Orientation.TOP_BOTTOM
                            gradient.colors = intArrayOf(vibrantColor, Color.WHITE)
                            gradient.cornerRadius = 30f
                            holder.cardBinding.root.background = gradient
                        }
                    }
                    override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
                })
            holder.cardBinding.pokemonName.text = p.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            }

        }
    }
}