package com.example.appdriesenmauro

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appdriesenmauro.databinding.ActivityCreateAccountBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.newSingleThreadContext

class CreateAccountActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding
    private var imageBitmap: Bitmap? = null
    private lateinit var mAuth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        if(currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = Firebase.auth

        binding.btnGetFotoLibrary.setOnClickListener {
            pickImage()
        }

        binding.btnTakeFoto.setOnClickListener{
            takeImage()
        }

        binding.btnCreatAccount.setOnClickListener{

            val email = binding.Txtemail.text.toString()
            val name = binding.plainTxtName.text.toString()
            val password = binding.txtPassword.text.toString()

            if (TextUtils.isEmpty(email)){
                val toast = "Please enter an email adress"
                Snackbar.make(binding.root, toast, Snackbar.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(name)){
                val toast = "Please enter a name"
                Snackbar.make(binding.root, toast, Snackbar.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(password)){
                val toast = "Please enter an password"
                Snackbar.make(binding.root, toast, Snackbar.LENGTH_SHORT).show()
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val toast = "User successfully created"
                        Snackbar.make(binding.root, toast, Snackbar.LENGTH_SHORT).show()

                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = mAuth.currentUser
                    } else {

                        val toast = "Failed to create user"
                        Snackbar.make(binding.root, toast, Snackbar.LENGTH_SHORT).show()

                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }





            //oude code voor het inloggen

            val userId = (0..1000).random()

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