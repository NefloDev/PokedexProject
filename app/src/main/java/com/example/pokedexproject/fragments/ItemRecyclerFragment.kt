package com.example.pokedexproject.fragments

import android.graphics.drawable.Drawable
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
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.pokedexproject.R
import com.example.pokedexproject.databinding.FragmentItemRecyclerBinding
import com.example.pokedexproject.databinding.ItemCardBinding
import com.example.pokedexproject.entities.common.DataSimple
import com.example.pokedexproject.entities.items.ItemViewModel

class ItemRecyclerFragment : Fragment() {
    private lateinit var binding : FragmentItemRecyclerBinding
    private lateinit var viewModel : ItemViewModel
    private lateinit var navController: NavController
    private lateinit var adapter : ItemCardAdapter
    private val spriteUrl =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentItemRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewModel = ViewModelProvider(requireActivity())[ItemViewModel::class.java]
        viewModel.startSearch()

        adapter = ItemCardAdapter()
        binding.itemRecycler.adapter = adapter

        viewModel.obtain().observe(viewLifecycleOwner){m ->
            if (m.isNotEmpty()){
                //When the list is updated and if it's not null,
                //it adds the new value into the adapter's list and notifies the insert
                adapter.addToList(m[m.size-1])
                adapter.notifyItemInserted(adapter.itemList.size-1)
            }
        }

        binding.itemSearch.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

        binding.itemSearch.setOnClickListener{
            binding.itemSearch.isIconified = false
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.initalize()
        adapter.itemList.clear()
    }

    inner class ItemCardAdapter : Adapter<ItemCardAdapter.ItemCardViewHolder>(), Filterable {
        inner class ItemCardViewHolder(val binding: ItemCardBinding) : ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCardViewHolder {
            return ItemCardViewHolder(ItemCardBinding.inflate(layoutInflater, parent, false))
        }
        var itemList = ArrayList<DataSimple>()
        var itemListFull = ArrayList<DataSimple>()
        override fun getItemCount(): Int {
            return itemList.size
        }

        fun addToList(element : DataSimple){
            itemList.add(element)
            itemListFull = ArrayList(itemList)
        }

        override fun onBindViewHolder(holder: ItemCardViewHolder, position: Int) {
            holder.binding.itemCardProgress.visibility = View.VISIBLE
            val item = itemList[position]
            val url = "${spriteUrl}${item.name}.png"
            val name = item.name.replaceFirstChar { c -> c.uppercase() }.replace("-", " ")
            holder.binding.itemName.text = name

            //Loads the item's sprite into the "itemImage" element using Glide
            //while it is loading, it will show the progress bar until is loaded or it fails
            //if it fails, it will show a warning image
            Glide.with(requireActivity())
                .load(url)
                .listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?,
                        isFirstResource: Boolean): Boolean {
                        holder.binding.itemCardProgress.visibility = View.GONE
                        holder.binding.itemImage.setImageDrawable(
                            requireActivity().getDrawable(R.drawable.warning))
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?,
                        dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        holder.binding.itemCardProgress.visibility = View.GONE
                        return false
                    }

                })
                .into(holder.binding.itemImage)

            //When the itemView is clicked, it selects an item by its name
            //then navigates to the Item Detail Fragment
            //and then it resets the state of the Searching bar
            holder.itemView.setOnClickListener {
                viewModel.select(item.name)
                navController.navigate(R.id.action_itemRecyclerFragment_to_itemDetailFragment)
                binding.itemSearch.clearFocus()
                binding.itemSearch.setQuery("", false)
                binding.itemSearch.isIconified = true
            }

        }

        override fun getFilter(): Filter {
            return itemFilter
        }

        private var itemFilter = object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                //Filters the list if the input is not null
                val filteredList : List<DataSimple> = if(constraint.isNullOrBlank()){
                    itemListFull
                }else{
                    val filterPattern = constraint.toString().lowercase().trim()
                    itemListFull.filter { i -> i.name.contains(filterPattern) }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                itemList.clear()
                itemList.addAll((results!!.values as List<DataSimple>))
                notifyDataSetChanged()
            }

        }
    }
}