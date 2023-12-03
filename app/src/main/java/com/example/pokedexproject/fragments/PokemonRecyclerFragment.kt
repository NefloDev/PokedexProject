package com.example.pokedexproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pokedexproject.databinding.FragmentPokemonRecyclerBinding

class PokemonRecyclerFragment : Fragment() {
    private lateinit var binding : FragmentPokemonRecyclerBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentPokemonRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }
}