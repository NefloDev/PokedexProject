package com.example.pokedexproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pokedexproject.databinding.FragmentItemRecyclerBinding

class ItemRecyclerFragment : Fragment() {
    private lateinit var binding : FragmentItemRecyclerBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentItemRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }
}