package com.example.appdriesenmauro.Fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.appdriesenmauro.classes.Activity
import com.example.appdriesenmauro.Acticity.MainActivity
import com.example.appdriesenmauro.R
import com.example.appdriesenmauro.databinding.FragementActivityInfoBinding
import com.google.firebase.auth.FirebaseAuth
import java.io.File

class MoreInfoFragment(
    private var activity: Activity,
    private var activityFragment: ActivityFragment,
    private val mainActivity: MainActivity
) : Fragment(R.layout.fragement_activity_info){
    private lateinit var builder : AlertDialog.Builder
    private lateinit var mAuth: FirebaseAuth;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth = FirebaseAuth.getInstance()
         val binding = FragementActivityInfoBinding.inflate(layoutInflater)
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

                    deleteFile()
                }
                .setNegativeButton("Don't delete!"){dialogInterface,it ->
                }
                .show()
        }

        if(activity.phEvent != null) {
            binding.imvHeadFoto.setImageBitmap(activity.phEvent)
        }

        binding.txtName.text = activity.title
        binding.txtMoreInfo.text = activity.context
        val date = "date: ${activity.date}"
        binding.txtDate.text =  date

        return binding.root
    }
    private fun deleteFile(){
        val filename = activity.title + mAuth.uid.toString()
        val file = File("/data/data/com.example.appdriesenmauro/files/" + filename)
        if (file.exists()) file.delete()
    }
}