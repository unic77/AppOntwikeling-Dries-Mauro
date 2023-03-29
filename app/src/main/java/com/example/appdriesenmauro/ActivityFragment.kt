package com.example.appdriesenmauro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appdriesenmauro.databinding.FragmentActivityBinding
import com.google.android.material.snackbar.Snackbar

class ActivityFragment : Fragment(R.layout.fragment_activity) {

    private lateinit var binding : FragmentActivityBinding
    private var sampleActivityItems = ArrayList<Activity>()
    private var favoriteActivityItems = ArrayList<Activity>()
    private lateinit var adapter: ActivityAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentActivityBinding.inflate(layoutInflater)
        var main = activity as MainActivity
        adapter = ActivityAdapter(sampleActivityItems,this)
        binding.rvwAcivity.adapter = adapter
        binding.rvwAcivity.layoutManager = LinearLayoutManager(this.context)
        return binding.root
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
}