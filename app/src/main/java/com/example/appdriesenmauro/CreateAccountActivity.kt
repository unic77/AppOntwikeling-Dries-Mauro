package com.example.appdriesenmauro

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.example.appdriesenmauro.databinding.ActivityCreateAccountBinding
import com.google.gson.Gson
import kotlinx.coroutines.newSingleThreadContext

class CreateAccountActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding
    private lateinit var imageBitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetFotoLibrary.setOnClickListener {
            pickImage()
        }

        binding.btnTakeFoto.setOnClickListener{
            takeImage()
        }

        binding.btnCreatAccount.setOnClickListener{

            val name = binding.plainTxtName.text.toString()
            val userId = (0..1000).random()
            val password = binding.txtPassword.text.toString()
            var compatence = false
            if(binding.cbCompentents.isChecked){
                compatence = true
            }

            val user = User(name,userId,password,compatence,imageBitmap);

            val gson = Gson()

            val userJson = gson.toJson(user)
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("thisUser",userJson)
            startActivity(intent)
        }
    }

    private fun takeImage(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE)
    }
    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?,) {

        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == IMAGE_PICK_CODE) {
            val dataUri = data?.data
            imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver,dataUri)
            binding.iVProfileFoto.setImageBitmap(imageBitmap)
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap
            binding.iVProfileFoto.setImageBitmap(imageBitmap)
        }
    }

    companion object{
        private val IMAGE_PICK_CODE = 1000
        private val REQUEST_IMAGE_CAPTURE = 1002
    }
}