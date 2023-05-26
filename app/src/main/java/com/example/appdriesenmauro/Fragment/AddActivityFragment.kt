package com.example.appdriesenmauro.Fragment


import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.appdriesenmauro.classes.Activity
import com.example.appdriesenmauro.Acticity.MainActivity
import com.example.appdriesenmauro.R
import com.example.appdriesenmauro.classes.User
import com.example.appdriesenmauro.databinding.ActivityAddactivityBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.*


class AddActivityFragment(activityFragmentIn: ActivityFragment, private var mainActivity: MainActivity, private var user: User): Fragment(
    R.layout.activity_addactivity
) {

    private lateinit var binding: ActivityAddactivityBinding
    private var activityFragment = activityFragmentIn
    private var phEvent: Bitmap? = null
    private lateinit var date: String
    private var userId = user.user_ID
    private lateinit var mAuth: FirebaseAuth


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityAddactivityBinding.inflate(layoutInflater)

        mAuth = FirebaseAuth.getInstance()

        binding.btnAddImage.setOnClickListener{
            pickImage()
        }

        //https://www.youtube.com/watch?v=fj-4x2X9Lew&ab_channel=ProgrammingGuru
        val picker = binding.eventDatePicker
        val today = Calendar.getInstance()

        date = "${today.get(Calendar.DAY_OF_MONTH)}/${today.get(Calendar.MONTH)+1}/${today.get(Calendar.YEAR)}"

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

            val context = binding.txtContextEvent.text.toString()

            //foto van het event word opgeslagen als een BYTEARRAY en wnr we deze nodig hebben zetten we deze om naar een bitmap
            val stream = ByteArrayOutputStream()
            phEvent?.compress(Bitmap.CompressFormat.PNG, 90, stream)
            val image = stream.toByteArray()

            //profielfoto opslaan als byteArray
            val stream1 = ByteArrayOutputStream()
            user.pfBitmap?.compress(Bitmap.CompressFormat.PNG,90,stream1)
            val PFimage = stream1.toByteArray()

            val activity = Activity(name, date, context,phEvent,user.pfBitmap,userId,false,image,PFimage)


            activityFragment.addActivity(activity)

            //opslaan met behulp van Gson en Json
            val gson = Gson()
            val activityJson = gson.toJson(activity)

            //gemaakte activiteit word opgeslagen in stringvorm
            val fileOutputStream: FileOutputStream


            try {
                val fileName = name + mAuth.uid
                fileOutputStream = requireActivity().openFileOutput(fileName, Context.MODE_PRIVATE)
                fileOutputStream.write(activityJson.toByteArray())
                val toast = "Event Saved"
                Snackbar.make(binding.root, toast, Snackbar.LENGTH_SHORT).show()
            }
            catch (e: FileNotFoundException){
                e.printStackTrace()
            }
            catch (e: java.lang.Exception){
                e.printStackTrace()
            }

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
        phEvent = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, data2!!.data)
        binding.imageTopOfInfoPage.setImageBitmap(phEvent)
    }

    companion object{
        private val IMAGE_PICK_CODE = 1000
    }
}
