package com.example.appdriesenmauro

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
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import java.net.URI

class CreateAccountActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding
    private var imageBitmap: Bitmap? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var FStorage: StorageReference

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        //if(currentUser != null){
            //val intent = Intent(this, MainActivity::class.java)
            //startActivity(intent)
        //}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityCreateAccountBinding.inflate(layoutInflater)
            setContentView(binding.root)

            mAuth = Firebase.auth
            FStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://appmaurodries.appspot.com/profilePic")

            binding.btnGetFotoLibrary.setOnClickListener {
                pickImage()
            }

            binding.btnTakeFoto.setOnClickListener {
                takeImage()
            }

            binding.btnCreatAccount.setOnClickListener {

                val email = binding.Txtemail.text.toString()
                val password = binding.txtPassword.text.toString()

                if (TextUtils.isEmpty(email)) {
                    val toast = "Please enter an email adress"
                    Snackbar.make(binding.root, toast, Snackbar.LENGTH_SHORT).show()
                }
                if (TextUtils.isEmpty(password)) {
                    val toast = "Please enter an password"
                    Snackbar.make(binding.root, toast, Snackbar.LENGTH_SHORT).show()
                }

                if (password.count() <= 6) {


                    throw ownException("password must be higher than 6 char")

                }
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val toast = "User successfully created"
                            Snackbar.make(binding.root, toast, Snackbar.LENGTH_SHORT).show()
                            uploadProfileImage(Uri.parse(MediaStore.Images.Media.insertImage(applicationContext.contentResolver,imageBitmap,mAuth.currentUser?.uid,null)))
                            val intent = Intent(this, WaitActivity::class.java)
                            startActivity(intent)
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                        } else {

                            val toast = "Failed to create user"
                            Snackbar.make(binding.root, toast, Snackbar.LENGTH_SHORT).show()

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                this, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                //uploadProfileImage(Uri.parse(MediaStore.Images.Media.insertImage(applicationContext.contentResolver,imageBitmap,mAuth.currentUser?.uid,null)))




                //oude code voor het inloggen

                var compatence = false
                if (binding.cbCompentents.isChecked) {
                    compatence = true
                }

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }
        }finally {
            System.out.println("account has not been created!!")
        }
        /*
        catch(e: ownException){
            System.out.println("ik ben hier")
            val toast = "account has not been created,${e.message}"
            Snackbar.make(binding.root, toast, Snackbar.LENGTH_SHORT).show()
        }*/
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

    fun uploadProfileImage(photo: Uri){
        val filRef = FStorage.child(mAuth.currentUser?.uid + ".jpg")
        val uploadTask = filRef.putFile(photo)

        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            System.out.println("succes")
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation filRef.downloadUrl
        })
    }


}