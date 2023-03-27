package com.example.appdriesenmauro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.FieldPosition

class ActivityAdapter(val items: List<Activity>): RecyclerView.Adapter<ActivityAdapter.ActivyViewHolder>() {
    inner class ActivyViewHolder(currentItemView: View): RecyclerView.ViewHolder(currentItemView)

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ActivyViewHolder {
        val view = ActivyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false))
        return view
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ActivyViewHolder, position: Int){
        val currentActivityItem = items[position]
        holder.itemView.apply {
            findViewById<TextView>(R.id.nameEventTxt).text = currentActivityItem.title
            findViewById<TextView>(R.id.dateEventTxt).text = currentActivityItem.date
            findViewById<ImageView>(R.id.activityImageItem).setImageURI(currentActivityItem.data?.data)
        }


    }
}