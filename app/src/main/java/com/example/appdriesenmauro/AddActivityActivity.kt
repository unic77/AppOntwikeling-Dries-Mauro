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
import com.google.android.material.snackbar.Snackbar
import java.util.*


class AddActivityActivity(activityFragmentIn: ActivityFragment, mainActivity: MainActivity,user: User): Fragment(R.layout.activity_addactivity) {

    private lateinit var binding: ActivityAddactivityBinding
    private var activityFragment = activityFragmentIn
    private var data: Intent? = null
    private lateinit var date: String
    private var mainActivity = mainActivity
    private var userId = user.user_ID
    private var user = user

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

        //https://www.youtube.com/watch?v=fj-4x2X9Lew&ab_channel=ProgrammingGuru
        val picker = binding.eventDatePicker
        val today = Calendar.getInstance()

        date = "${today.get(Calendar.DAY_OF_MONTH)}/${today.get(Calendar.MONTH)}/${today.get(Calendar.YEAR)}"

        picker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)){
                view, year, month, day ->
            val month = month+1
            val toast = "You have selected : $day/$month/$year"
            date = "$day/$month/$year"

            Snackbar.make(binding.root, toast, Snackbar.LENGTH_SHORT).show()
        }

        binding.btnPublishEvent.setOnClickListener{
            var name = binding.txtNameEvent.text.toString()

            val datePicker = binding.eventDatePicker
            val today = Calendar.getInstance()
            datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)

            ) { view3, year, month, day ->
                val month = month + 1
                date = "$day/$month/$year"

            }

            var context = binding.txtContextEvent.text.toString()

            var favorite = false

            var activity = Activity(name, date, context,data,user.pfBitmap,userId,favorite)
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
    }
}
