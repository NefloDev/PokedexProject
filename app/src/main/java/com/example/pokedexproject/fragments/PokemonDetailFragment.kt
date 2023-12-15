package com.example.pokedexproject.fragments

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.example.pokedexproject.R
import com.example.pokedexproject.databinding.FragmentPokemonDetailBinding
import com.example.pokedexproject.databinding.PokemonTypeLabelBinding
import com.example.pokedexproject.entities.pokemon.PokemonViewModel
import java.util.Locale

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
        var adapter : PokemonTypeLabelAdapter

        viewModel.selected().observe(viewLifecycleOwner
        ) { value ->

            binding.detailName.text = value.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
            }

            binding.detailId.text = "#${"%04d".format(value.id)}"

            Glide.with(requireActivity()).asBitmap().load(value.sprites.other.official_artwork.front_default)
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(
                        bitmap: Bitmap, transition: com.bumptech.glide.request.transition.Transition<in Bitmap?>?) {
                        Palette.Builder(bitmap).generate { it?.let { palette ->
                            binding.cardLayout.backgroundTintList = ColorStateList.valueOf(palette.getDominantColor(Color.WHITE))
                        } }
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {}
                })

            adapter = PokemonTypeLabelAdapter(value.types.map { t -> t.type.name })
            binding.pokemonTypeRecycler.adapter = adapter

            setImage(view, value.sprites.other.official_artwork.front_default)

            val weightValText = "${value.weight/10}kg"
            val heightValText = "${value.height/10}m"
            binding.heightValue.text = heightValText
            binding.weightValue.text = weightValText

            val hp = value.stats.find {s -> s.stat.name == resources.getString(R.string.hp)}
            val hpValue = "${hp!!.base_stat}/255"
            val atk = value.stats.find {s -> s.stat.name == resources.getString(R.string.atk)}
            val atkValue = "${atk!!.base_stat}/255"
            val def = value.stats.find {s -> s.stat.name == resources.getString(R.string.def)}
            val defValue = "${def!!.base_stat}/255"
            val spd = value.stats.find {s -> s.stat.name == resources.getString(R.string.speed)}
            val spdValue = "${spd!!.base_stat}/255"
            val exp = value.base_experience
            val expValue = "${exp}/1000"

            binding.hpIndicator.setProgress(hp.base_stat, true)
            binding.hpValue.text = hpValue
            binding.atkIndicator.setProgress(atk.base_stat, true)
            binding.atkValue.text = atkValue
            binding.defIndicator.setProgress(def.base_stat, true)
            binding.defValue.text = defValue
            binding.spdIndicator.setProgress(spd.base_stat, true)
            binding.spdValue.text = spdValue
            binding.expIndicator.setProgress(exp, true)
            binding.expValue.text = expValue
        }

    }

    private fun setImage(view : View, image : String){
        Glide.with(view)
            .load(image)
            .listener(object : RequestListener<Drawable>{
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?,
                                          isFirstResource: Boolean): Boolean {
                    binding.loadingImage.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?,
                    dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    binding.loadingImage.visibility = View.GONE
                    return false
                }

            }).into(binding.pokemonDetailImage)
    }

    inner class PokemonTypeLabelAdapter(val typeList : List<String>)
        :Adapter<PokemonTypeLabelAdapter.PokemonTypeLabelViewHolder>(){
        inner class PokemonTypeLabelViewHolder(val binding: PokemonTypeLabelBinding)
            :ViewHolder(binding.root){}

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
        : PokemonTypeLabelViewHolder {
            return PokemonTypeLabelViewHolder(
                PokemonTypeLabelBinding.inflate(layoutInflater, parent, false))
        }
        override fun getItemCount(): Int {
            return typeList.size
        }

        override fun onBindViewHolder(holder: PokemonTypeLabelViewHolder, position: Int) {
            var text = 0
            var color = 0
            when (typeList[position]){
                resources.getString(R.string.bug) -> {text = R.string.bug;color = R.color.bug_type
                }
                resources.getString(R.string.dark) -> {text = R.string.dark;color =
                    R.color.dark_type
                }
                resources.getString(R.string.dragon) -> {text = R.string.dragon;color =
                    R.color.dragon_type
                }
                resources.getString(R.string.electric) -> {text = R.string.electric;color =
                    R.color.electric_type
                }
                resources.getString(R.string.fairy) -> {text = R.string.fairy;color =
                    R.color.fairy_type
                }
                resources.getString(R.string.fighting) -> {text = R.string.fighting;color =
                    R.color.fighting_type
                }
                resources.getString(R.string.fire) -> {text = R.string.fire;color =
                    R.color.fire_type
                }
                resources.getString(R.string.flying) -> {text = R.string.flying;color =
                    R.color.flying_type
                }
                resources.getString(R.string.ghost) -> {text = R.string.ghost;color =
                    R.color.ghost_type
                }
                resources.getString(R.string.grass) -> {text = R.string.grass;color =
                    R.color.grass_type
                }
                resources.getString(R.string.ground) -> {text = R.string.ground;color =
                    R.color.ground_type
                }
                resources.getString(R.string.ice) -> {text = R.string.ice;color = R.color.ice_type
                }
                resources.getString(R.string.normal) -> {text = R.string.normal;color =
                    R.color.normal_type
                }
                resources.getString(R.string.poison) -> {text = R.string.poison;color =
                    R.color.poison_type
                }
                resources.getString(R.string.psychic) -> {text = R.string.psychic;color =
                    R.color.psychic_type
                }
                resources.getString(R.string.rock) -> {text = R.string.rock;color =
                    R.color.rock_type
                }
                resources.getString(R.string.shadow) -> {text = R.string.shadow;color =
                    R.color.shadow_type
                }
                resources.getString(R.string.steel) -> {text = R.string.steel;color =
                    R.color.steel_type
                }
                resources.getString(R.string.unknown) -> {text = R.string.unknown;color =
                    R.color.unknown_type
                }
                resources.getString(R.string.water) -> {text = R.string.water;color =
                    R.color.water_type
                }
                else -> {text = R.string.unknown;color = R.color.unknown_type
                }
            }

            holder.binding.typeText.text = resources.getString(text)
            holder.binding.root.setCardBackgroundColor(
                resources.getColor(color, requireActivity().theme))
        }
    }
}