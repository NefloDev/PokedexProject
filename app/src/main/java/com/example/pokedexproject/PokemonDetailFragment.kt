package com.example.pokedexproject

import Pokemon
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.pokedexproject.databinding.FragmentPokemonDetailBinding
import com.example.pokedexproject.entities.PokemonViewModel

class PokemonDetailFragment : Fragment() {
    private lateinit var binding : FragmentPokemonDetailBinding
    private lateinit var viewModel: PokemonViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[PokemonViewModel::class.java]

        viewModel.selectedPokemon.observe(viewLifecycleOwner, object : Observer<Pokemon>{
            override fun onChanged(value: Pokemon) {
                Glide.with(view)
                    .load(value.sprites.other.official_artwork.front_default)
                    .into(binding.imageView2)
            }

        })

    }

}