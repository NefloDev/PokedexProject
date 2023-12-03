package com.example.pokedexproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pokedexproject.databinding.FragmentBagBinding

class BagFragment : Fragment() {
    private lateinit var binding : FragmentBagBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentBagBinding.inflate(inflater, container, false)
        return binding.root
    }
}