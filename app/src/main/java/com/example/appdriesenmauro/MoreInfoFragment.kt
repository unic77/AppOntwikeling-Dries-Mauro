package com.example.appdriesenmauro

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.appdriesenmauro.databinding.FragementActivityInfoBinding
import com.google.firebase.auth.FirebaseAuth
import java.io.File

class MoreInfoFragment(activity: Activity, user: User,activityFragment: ActivityFragment,mainActivity: MainActivity) : Fragment(R.layout.fragement__activity_info){
    private lateinit var binding : FragementActivityInfoBinding
    private lateinit var builder : AlertDialog.Builder
    private lateinit var mAuth: FirebaseAuth;
    private var activityFragment = activityFragment
    private val mainActivity = mainActivity
    private var activity = activity
    private val user = user

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth = FirebaseAuth.getInstance()
        binding = FragementActivityInfoBinding.inflate(layoutInflater)
        //i
        if( mAuth.currentUser?.uid == activity.postedUser) {
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
                .setPositiveButton("Yes, delete the event"){dialogInterface,it ->
                    mainActivity.switchTo(activityFragment);
                    activityFragment.removeItem(activity)
                    /*
                    val file = File()
                    if (file.exists()) file.delete()
                    deletefile()*/
                }
                .setNegativeButton("Don't delete!!!"){dialogInterface,it ->
                }
                .show()
        }

        if(activity.data != null) {
            binding.imvHeadFoto.setImageBitmap(activity.data)
        }

        binding.txtName.text = activity.title
        binding.txtMoreInfo.text = activity.context
        val date = "date: ${activity.date}"
        binding.txtDate.text =  date

        return binding.root
    }
    /*private fun deletefile(){
        var files: Array<String>? = fileList()
        if (files != null){
            for (item in files) {
                var x = activity.title + mAuth.uid
                if (item == x){
                    item.
                }
            }
        }
    }*/
}