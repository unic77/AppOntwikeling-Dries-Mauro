package com.example.appdriesenmauro.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appdriesenmauro.classes.Activity
import com.example.appdriesenmauro.adapter.ActivityAdapter
import com.example.appdriesenmauro.Acticity.MainActivity
import com.example.appdriesenmauro.R
import com.example.appdriesenmauro.databinding.FragmentActivityBinding
import java.util.*
import kotlin.collections.ArrayList

class ActivityFragment() : Fragment(R.layout.fragment_activity) {

    private var sampleActivityItems = ArrayList<Activity>()
    private var favoriteActivityItems = ArrayList<Activity>()
    private lateinit var adapter: ActivityAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentActivityBinding.inflate(layoutInflater)
        val main = activity as MainActivity
        adapter = ActivityAdapter(sampleActivityItems,this,main)
        binding.rvwAcivity.adapter = adapter
        binding.rvwAcivity.layoutManager = LinearLayoutManager(this.context)
        val searchView = binding.idSV
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filter(newText)
                return false
            }
        })
        return binding.root
    }

    fun removeItem(activity: Activity){
        sampleActivityItems.remove(activity)
        favoriteActivityItems.remove(activity)
    }

    fun switchFiewToHome(){
        adapter.setItems(sampleActivityItems)
        adapter.notifyDataSetChanged()
    }

    fun switchFiewToFavorite(){
        adapter.setItems(favoriteActivityItems)
        adapter.notifyDataSetChanged()
    }

    fun addFavorite(activity: Activity){
        if(!favoriteActivityItems.contains(activity)) {
            favoriteActivityItems.add(activity)
        }
        else return
    }

    fun removeFavorite(activity: Activity){
        favoriteActivityItems.remove(activity)
    }

    fun addActivity(activity: Activity){
        sampleActivityItems.add(activity)
        adapter.notifyItemInserted(sampleActivityItems.size - 1)
    }

    fun addSavedActivity(activity: Activity){
        sampleActivityItems.add(activity)
    }

    private fun filter(text: String) {
        val filteredlist = ArrayList<Activity>()
        for (item in sampleActivityItems) {
            if (item.title.toLowerCase().contains(text.lowercase(Locale.getDefault()))) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            adapter.filterList(filteredlist)
        } else {
            adapter.filterList(filteredlist)
        }
    }
}