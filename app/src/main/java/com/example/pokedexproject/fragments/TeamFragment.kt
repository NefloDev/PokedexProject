package com.example.pokedexproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pokedexproject.databinding.FragmentTeamBinding

class TeamFragment : Fragment() {
    private lateinit var binding : FragmentTeamBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentTeamBinding.inflate(inflater, container, false)
        return binding.root
    }
}