package com.example.pokedexproject.fragments

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.icu.text.ListFormatter.Width
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.SearchView.OnQueryTextListener
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
import com.example.pokedexproject.entities.common.DataSimple
import com.example.pokedexproject.entities.pokemon.PokemonViewModel
import java.lang.StringBuilder
import java.util.Locale

class PokemonRecyclerFragment : Fragment() {
    private lateinit var binding : FragmentPokemonRecyclerBinding
    private lateinit var viewModel: PokemonViewModel
    private lateinit var adapter : PokemonCardAdapter
    private lateinit var navController: NavController
    private val spritesUrl =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork"
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
                adapter.addToList(m[m.size-1])
                adapter.notifyItemInserted(adapter.pokemonList.size-1)
            }
        }

        binding.search.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

        })

        binding.search.setOnClickListener{
            binding.search.isIconified = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.initalize()
        adapter.pokemonList.clear()
    }

    inner class PokemonCardAdapter : RecyclerView.Adapter<PokemonCardAdapter.PokemonCardViewHolder>(), Filterable{
        inner class PokemonCardViewHolder(val cardBinding : CardPokemonBinding) : ViewHolder(cardBinding.root)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonCardViewHolder {
            return PokemonCardViewHolder(CardPokemonBinding.inflate(layoutInflater, parent, false))
        }
        var pokemonList = ArrayList<DataSimple>()
        var pokemonListFull = ArrayList<DataSimple>()
        override fun getItemCount(): Int {
            return pokemonList.size
        }

        fun addToList(element : DataSimple){
            pokemonList.add(element)
            pokemonListFull = ArrayList(pokemonList)
        }

        override fun onBindViewHolder(holder: PokemonCardViewHolder, position: Int) {
            binding.loadingWidget.visibility = View.GONE
            holder.cardBinding.pokemonName.visibility = View.GONE
            val p = pokemonList[position]
            val urlminus = p.url.substring(0, p.url.length-1)
            val image = "$spritesUrl${urlminus.substring(urlminus.lastIndexOf("/"))}.png"
            var name = p.name.replaceFirstChar {it.uppercase() }
            name = name.replace("-", " ")

            setImage(holder, image)
            setColor(image, holder)

            holder.cardBinding.pokemonName.text = name

            //When the itemView is clicked, it selects a pokemon by its name
            //then navigates to the Pokemon Detail Fragment
            //and then it resets the state of the Searching bar
            holder.itemView.setOnClickListener{
                viewModel.select(p.name)
                navController.navigate(R.id.action_pokemonRecyclerFragment_to_pokemonDetailFragment)
                binding.search.clearFocus()
                binding.search.setQuery("", false)
                binding.search.isIconified = true
            }
        }

        private fun setImage(holder : PokemonCardViewHolder, image : String){
            val progressBar = holder.cardBinding.progressBar.progressDrawable

            //Loads the pokemon's sprite into the "pokemonImage" element using Glide
            //while it is loading, it will show the progress bar until is loaded or it fails
            //if it fails, it will show a warning image
            Glide.with(requireActivity()).load(image).placeholder(progressBar)
                .listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?,
                                              isFirstResource: Boolean): Boolean {
                        setVisibility()
                        holder.cardBinding.pokemonImage.setImageDrawable(
                            requireActivity().getDrawable(R.drawable.warning))
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

        private fun setColor(image : String, holder : PokemonCardViewHolder){
            //Loads the pokemon's sprite as a bitmap into a CustomTarget of Bitmap type element using Glide
            //this object is an Interface that will set a gradient with the dominant color
            //extracted by "Palette" to a White color
            Glide.with(requireActivity()).asBitmap().load(image)
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(
                        bitmap: Bitmap, transition: com.bumptech.glide.request.transition.Transition<in Bitmap?>?) {
                        Palette.Builder(bitmap).generate { it?.let { palette ->
                            val gradient = GradientDrawable()
                            gradient.colors = intArrayOf(palette.getDominantColor(Color.WHITE), Color.WHITE)
                            gradient.cornerRadius = 35f
                            holder.cardBinding.root.background = gradient
                        } }
                }
                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }

        override fun getFilter(): Filter {
            return pokemonFilter
        }

        private var pokemonFilter = object : Filter(){

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                //Filters the list if the input is not null
                val filteredList : List<DataSimple> = if(constraint.isNullOrBlank()){
                    pokemonListFull
                }else{
                    val filterPattern = constraint.toString().lowercase().trim()
                    pokemonListFull.filter { p -> p.name.contains(filterPattern) }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                pokemonList.clear()
                pokemonList.addAll((results!!.values as List<DataSimple>))
                notifyDataSetChanged()
            }

        }
    }
}