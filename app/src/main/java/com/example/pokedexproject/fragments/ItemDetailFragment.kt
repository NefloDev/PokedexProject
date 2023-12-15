package com.example.pokedexproject.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.pokedexproject.R
import com.example.pokedexproject.databinding.FragmentItemDetailBinding
import com.example.pokedexproject.entities.items.ItemViewModel

class ItemDetailFragment : Fragment() {
    private lateinit var binding : FragmentItemDetailBinding
    private lateinit var navController: NavController
    private lateinit var itemViewModel: ItemViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        itemViewModel = ViewModelProvider(requireActivity())[ItemViewModel::class.java]

        binding.backButton.setOnClickListener {
            //Returns to the previous fragment
            navController.popBackStack()
        }

        itemViewModel.selected().observe(viewLifecycleOwner){
            item ->
            //When the selected mutablelivedata changes triggers all of this:

            //Sets the name of the item with a leading uppercase and replacing the dashes for spaces
            binding.itemDetailName.text = item.name.replaceFirstChar { c -> c.uppercase() }
                .replace("-", " ")
            //Sets the id of the item with a format of 3 numbers, it will have leading zeros if its
            //below 100
            binding.itemDetailId.text = "#${"%03d".format(item.id)}"
            //Sets the cost of the item
            binding.itemDetailCost.text = "${item.cost}¥"
            //Sets the category of the item with a leading uppercase and replacing the dashes for spaces
            binding.itemDetailCategory.text = item.category.name.replaceFirstChar { c -> c.uppercase() }
                .replace("-", " ")

            //If the list of effects is not null or empty, it fills a ListView with the effects
            //mapped to strings, else it will hide the "effectTitle" element
            if (!item.effects.isNullOrEmpty()){
                binding.effectsTitle.visibility = View.VISIBLE
                binding.effectList.adapter =  ArrayAdapter(requireActivity(),
                    R.layout.list_text_layout, item.effects.map { e -> e.effect
                        .replace("\n", " ")
                        .replace("\\s+".toRegex(), " ")})
            }else{
                binding.effectsTitle.visibility = View.GONE
            }
            //If the list of pokemon that can hold the item is not null or empty, it fills a ListView
            //with the pokemon mapped to strings, else it will hide the "holdableTitle" element
            if (!item.pokemons.isNullOrEmpty()){
                binding.holdableTitle.visibility = View.VISIBLE
                binding.holdableList.adapter = ArrayAdapter(requireActivity(),
                    R.layout.list_text_layout, item.pokemons.map { p -> p.pokemon.name
                        .replaceFirstChar { c -> c.uppercase() }
                        .replace("-", " ") })
            }else{
                binding.holdableTitle.visibility = View.GONE
            }

            //Loads the item's sprite into the "itemDetailImage" element using Glide
            //while it is loading, it will show the progress bar until is loaded or it fails
            //if it fails, it will show a warning image
            Glide.with(requireActivity())
                .load(item.sprites.default)
                .listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?,
                        isFirstResource: Boolean): Boolean {
                        binding.itemDetailImageProgress.visibility = View.GONE
                        binding.itemDetailImage.setImageDrawable(
                            requireActivity().getDrawable(R.drawable.warning))
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?,
                        dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        binding.itemDetailImageProgress.visibility = View.GONE
                        return false
                    }

                })
                .into(binding.itemDetailImage)
        }
    }
}