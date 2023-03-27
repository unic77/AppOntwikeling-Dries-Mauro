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

    private var adapter= ActivityAdapter(sampleActivityItems)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentActivityBinding.inflate(layoutInflater)
        var main = activity as MainActivity

        binding.rvwAcivity.adapter = adapter
        binding.rvwAcivity.layoutManager = LinearLayoutManager(this.context)

        return binding.root
    }

    fun addActivity(activity: Activity){
        sampleActivityItems.add(activity)
        adapter.notifyItemInserted(sampleActivityItems.size - 1)
    }
}