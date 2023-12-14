package com.example.pokedexproject.fragments

import com.example.pokedexproject.entities.pokemon.Pokemon
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.example.pokedexproject.R
import com.example.pokedexproject.databinding.CardPokemonBinding
import com.example.pokedexproject.databinding.FragmentPokemonRecyclerBinding
import com.example.pokedexproject.entities.pokemon.PokemonViewModel
import java.util.Locale

class PokemonRecyclerFragment : Fragment() {
    private lateinit var binding : FragmentPokemonRecyclerBinding
    private lateinit var viewModel: PokemonViewModel
    private lateinit var adapter : PokemonCardAdapter
    private lateinit var navController: NavController
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentPokemonRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loadingWidget.visibility = View.VISIBLE
        navController = Navigation.findNavController(view)
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
    }

    inner class PokemonCardAdapter : RecyclerView.Adapter<PokemonCardAdapter.PokemonCardViewHolder>(){
        inner class PokemonCardViewHolder(val cardBinding : CardPokemonBinding) : ViewHolder(cardBinding.root)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonCardViewHolder {
            return PokemonCardViewHolder(CardPokemonBinding.inflate(layoutInflater, parent, false))
        }
        var pokemonList : ArrayList<Pokemon> = ArrayList()
        override fun getItemCount(): Int {
            return pokemonList.size
        }

        override fun onBindViewHolder(holder: PokemonCardViewHolder, position: Int) {
            binding.loadingWidget.visibility = View.GONE
            holder.cardBinding.pokemonName.visibility = View.GONE
            val p = pokemonList[position]

            setImage(holder, p.sprites.other.official_artwork.front_default)
            setColor(p, holder)

            holder.cardBinding.pokemonName.text = p.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
            }

            holder.itemView.setOnClickListener{
                viewModel.select(p)
                navController.navigate(R.id.action_pokemonRecyclerFragment_to_pokemonDetailFragment)
            }
        }

        private fun setImage(holder : PokemonCardViewHolder, image : String){
            val progressBar = holder.cardBinding.progressBar.progressDrawable

            Glide.with(requireActivity()).load(image).placeholder(progressBar)
                .listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?,
                                              isFirstResource: Boolean): Boolean {
                        setVisibility()
                        return false
                    }
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?,
                                                 dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        setVisibility()
                        return false
                    }
                    private fun setVisibility(){
                        holder.cardBinding.progressBar.visibility = View.GONE
                        holder.cardBinding.pokemonName.visibility = View.VISIBLE
                    }
                }).into(holder.cardBinding.pokemonImage)
        }

        private fun setColor(p : Pokemon, holder : PokemonCardViewHolder){
            val image = p.sprites.other.official_artwork.front_default
            Glide.with(requireActivity()).asBitmap().load(image)
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(
                        bitmap: Bitmap, transition: com.bumptech.glide.request.transition.Transition<in Bitmap?>?) {
                        Palette.Builder(bitmap).generate { it?.let { palette ->
                            val dom = palette.getDominantColor(Color.WHITE)
                            val gradient = GradientDrawable()
                            gradient.colors = intArrayOf(dom, Color.WHITE)
                            gradient.cornerRadius = 35f
                            holder.cardBinding.root.background = gradient
                            p.bg = dom
                        } }
                }
                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }
    }
}