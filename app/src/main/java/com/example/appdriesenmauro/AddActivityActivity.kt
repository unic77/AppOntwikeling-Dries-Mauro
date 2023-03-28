package com.example.appdriesenmauro

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.appdriesenmauro.databinding.ActivityAddactivityBinding


class AddActivityActivity(activityFragmentIn: ActivityFragment, mainActivity: MainActivity): Fragment(R.layout.activity_addactivity) {

    private lateinit var binding: ActivityAddactivityBinding
    private var activityFragment = activityFragmentIn
    private var data: Intent? = null
    private lateinit var date: String
    private var mainActivity = mainActivity

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityAddactivityBinding.inflate(layoutInflater)


        binding.btnAddImage.setOnClickListener{
            pickImage()
        }

        binding.btnPublishEvent.setOnClickListener{
            var name = binding.txtNameEvent.text.toString()

            date = "10/20/4"
            /*val datePicker = binding.eventDatePicker
            val today = Calendar.getInstance()
            datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)

            ) { view3, year, month, day ->
                val month = month + 1
                date = "$day/$month/$year"

            }*/

            var context = binding.txtContextEvent.text.toString()

            var activity = Activity(name, date, context,data)
            activityFragment.addActivity(activity)

            mainActivity.switchTo(activityFragment)
        }
        return binding.root
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data2: Intent?) {
        //https://www.youtube.com/watch?v=gd300jxLEe0&ab_channel=AtifPervaiz
        data = data2
        binding.imageTopOfInfoPage.setImageURI(data2?.data)
    }

    companion object{
        private val IMAGE_PICK_CODE = 1000
        private val PREMISION_CODE = 1001
    }
}