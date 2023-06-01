package com.example.appdriesenmauro.Acticity

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
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


class CreateAccountActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding
    private var pfUser: Bitmap? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var FStorage: StorageReference

    public override fun onStart() {
        super.onStart()
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

        FStorage = FirebaseStorage.getInstance()
            .getReferenceFromUrl("gs://appmaurodries.appspot.com/profilePic")

        binding.btnGetFotoLibrary.setOnClickListener {
            pickImage()
        }

        binding.btnTakeFoto.setOnClickListener {
            takeImage()
        }

        binding.btnCreatAccount.setOnClickListener {

            val connManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            val mobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            val email = binding.Txtemail.text.toString()
            val password = binding.txtPassword.text.toString()

            if (pfUser == null) {
                makeToast("pick a photo")
            }
            else if (TextUtils.isEmpty(email)) {
                makeToast("enter an email address")
            }
            else if (TextUtils.isEmpty(password)) {
                makeToast("enter an password address")
            }
            else if (password.count() < 6) {
                makeToast("password must be more than 6 letters")
            }
            else if (!wifi!!.isConnected || !mobile!!.isConnected) {
                makeToast("Connect to the internet before creating an account")
            }
            else {
                //wifi of data connected
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            uploadProfileImage(
                                Uri.parse(
                                    MediaStore.Images.Media.insertImage(
                                        applicationContext.contentResolver,
                                        pfUser,
                                        mAuth.currentUser?.uid,
                                        null
                                    )
                                ),
                                mAuth
                            )
                            val intent = Intent(this, WaitActivity::class.java)
                            startActivity(intent)
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            makeToast("Authentication failed.")
                        }
                    }
            }
        }
    }


    private fun takeImage(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }
    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == IMAGE_PICK_CODE) {
            val dataUri = data?.data
            if(dataUri != null){
                pfUser = MediaStore.Images.Media.getBitmap(contentResolver,dataUri)
                binding.iVProfileFoto.setImageBitmap(pfUser)
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            pfUser = data?.extras?.get("data") as Bitmap
            if(pfUser != null){
                binding.iVProfileFoto.setImageBitmap(pfUser)
            }
        }
    }

    companion object{
        private val IMAGE_PICK_CODE = 1000
        private val REQUEST_IMAGE_CAPTURE = 1002
    }

    private fun makeToast(toastString: String){
        val toast = toastString
        Snackbar.make(binding.root, toast, Snackbar.LENGTH_SHORT).show()
    }

    private fun uploadProfileImage(photo: Uri,mAuth: FirebaseAuth){
        val filRef = FStorage.child(mAuth.currentUser?.uid + ".jpg")
        val uploadTask = filRef.putFile(photo)

        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation filRef.downloadUrl
        })
    }


}