package com.example.appdriesenmauro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class ActivityAdapter(val itemsIn: List<Activity>, val activityFragment: ActivityFragment): RecyclerView.Adapter<ActivityAdapter.ActivyViewHolder>() {
    inner class ActivyViewHolder(currentItemView: View): RecyclerView.ViewHolder(currentItemView)
        private lateinit var  view: ActivityAdapter.ActivyViewHolder
        private var items = itemsIn
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ActivyViewHolder {
        view = ActivyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false))
        return view
    }

    fun setItems(List: ArrayList<Activity>){
        items = List
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ActivyViewHolder, position: Int){
        val currentActivityItem = items[position]
        holder.itemView.apply {
            findViewById<TextView>(R.id.nameEventTxt).text = currentActivityItem.title
            findViewById<TextView>(R.id.dateEventTxt).text = currentActivityItem.date
            findViewById<ImageView>(R.id.activityImageItem).setImageURI(currentActivityItem.data?.data)
            var switch = findViewById<Switch>(R.id.swAddToFavorite)
            switch.setOnClickListener{
                when(switch.isChecked){
                    true -> activityFragment.addFavorite(currentActivityItem)
                    false -> activityFragment.removeFavorite(currentActivityItem)
                }
            }
        }

    }
}