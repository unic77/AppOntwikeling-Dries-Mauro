package com.example.appdriesenmauro

import android.os.Bundle
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.appdriesenmauro.databinding.FragementActivityInfoBinding

class MoreInfoFragment(activity: Activity, user: User,activityFragment: ActivityFragment,mainActivity: MainActivity) : Fragment(R.layout.fragement__activity_info){
    private lateinit var binding : FragementActivityInfoBinding
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

        binding.btnRemove.setOnClickListener {
            mainActivity.switchTo(activityFragment);
            activityFragment.removeItem(activity)
        }
        binding.imvHeadFoto.setImageURI(activity.data?.data)
        binding.txtName.text = activity.title
        binding.txtMoreInfo.text = activity.context

        return binding.root
    }
}