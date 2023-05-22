package com.example.appdriesenmauro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class ActivityAdapter(itemsIn: List<Activity>, private val activityFragment: ActivityFragment, private val mainActivity: MainActivity): RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    inner class ActivityViewHolder(currentItemView: View): RecyclerView.ViewHolder(currentItemView)
        private lateinit var  view: ActivityAdapter.ActivityViewHolder
        private var items = itemsIn
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ActivityViewHolder {
            view = ActivityViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false))
            return view
        }

    fun filterList(filterList: ArrayList<Activity>) {
        items = filterList
        notifyDataSetChanged()
    }

    fun setItems(List: ArrayList<Activity>){
        items = List
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int){
        val currentActivityItem = items[position]
        holder.itemView.apply {
            findViewById<TextView>(R.id.nameEventTxt).text = currentActivityItem.title
            findViewById<TextView>(R.id.dateEventTxt).text = currentActivityItem.date
            findViewById<ImageView>(R.id.activityImageItem).setImageBitmap(currentActivityItem.phEvent)
            if(currentActivityItem.pfUser != null) {
                findViewById<ImageView>(R.id.ivProfilePicture).setImageBitmap(currentActivityItem.pfUser)
            }
            else{
                System.out.println("activity.adapter")
            }
            val switch = findViewById<Switch>(R.id.swAddToFavorite)
            findViewById<Button>(R.id.btnMoreInfo).setOnClickListener{
                val moreInFragment = MoreInfoFragment(currentActivityItem,activityFragment,mainActivity)
                mainActivity.switchTo(moreInFragment)
            }
            switch.setOnClickListener{
                when(switch.isChecked){
                    true -> {
                                activityFragment.addFavorite(currentActivityItem)
                                currentActivityItem.favorite = true
                                }
                    false -> {
                                activityFragment.removeFavorite(currentActivityItem)
                                currentActivityItem.favorite = false
                                }
                }
            }

            switch.isChecked = currentActivityItem.favorite
        }

    }
}