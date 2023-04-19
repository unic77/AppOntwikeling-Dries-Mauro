package com.example.appdriesenmauro

import android.app.AlertDialog
import android.os.Bundle
import android.provider.CalendarContract.Instances
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.appdriesenmauro.databinding.FragementActivityInfoBinding
import java.time.Instant

class MoreInfoFragment(activity: Activity, user: User,activityFragment: ActivityFragment,mainActivity: MainActivity) : Fragment(R.layout.fragement__activity_info){
    private lateinit var binding : FragementActivityInfoBinding
    private lateinit var builder : AlertDialog.Builder
    private var activityFragment = activityFragment
    private val mainActivity = mainActivity
    private var activity = activity
    private val user = user

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragementActivityInfoBinding.inflate(layoutInflater)

        if(user.user_ID == activity.postedUser) {
            binding.btnRemove.visibility = View.VISIBLE
        }
        else{
            binding.btnRemove.visibility = View.INVISIBLE
        }

        builder = AlertDialog.Builder(mainActivity)

        binding.btnRemove.setOnClickListener {

            builder.setTitle("Alert!")
                .setMessage("Are you sure you want to delete this event? You cannot recover the event once deleted")
                .setCancelable(true)
                .setPositiveButton("Yes"){dialogInterface,it ->
                    mainActivity.switchTo(activityFragment);
                    activityFragment.removeItem(activity)
                }
                .setNegativeButton("pls not DADDY"){dialogInterface,it ->
                }
                .show()
        }
        binding.imvHeadFoto.setImageURI(activity.data?.data)
        binding.txtName.text = activity.title
        binding.txtMoreInfo.text = activity.context
        val date = "date: ${activity.date}"
        binding.txtDate.text =  date

        return binding.root
    }
}