package com.example.appdriesenmauro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appdriesenmauro.databinding.FragmentActivityBinding
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList

class ActivityFragment(user: User) : Fragment(R.layout.fragment_activity) {

    private lateinit var binding : FragmentActivityBinding
    private var sampleActivityItems = ArrayList<Activity>()
    private var favoriteActivityItems = ArrayList<Activity>()
    private lateinit var adapter: ActivityAdapter
    private val user = user

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentActivityBinding.inflate(layoutInflater)
        val main = activity as MainActivity
        adapter = ActivityAdapter(sampleActivityItems,this,main,user)
        binding.rvwAcivity.adapter = adapter
        binding.rvwAcivity.layoutManager = LinearLayoutManager(this.context)
        val SearchView = binding.idSV
        SearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener {
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
        //i
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
            val toast = "no event found with that name"
            Snackbar.make(binding.root,toast, Snackbar.LENGTH_SHORT).show()
        } else {
            adapter.filterList(filteredlist)
        }
    }
}