package com.example.appdriesenmauro

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.appdriesenmauro.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson


class UserActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth;


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = Firebase.auth
        val currentUser = mAuth.currentUser
        //if(currentUser != null){
           //val intent = Intent(this, MainActivity::class.java)
            //startActivity(intent)
        //}
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = Firebase.auth



        binding.loginBtn.setOnClickListener{

            val email = binding.emailadress.text.toString()
            val name = binding.Usernametxt.text.toString()
            val password = binding.Passporttxt.text.toString()

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

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val toast = "User successfully logged in"
                        Snackbar.make(binding.root, toast, Snackbar.LENGTH_SHORT).show()

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)

                    } else {

                        val toast = "Failed, make sure that email and password is correct, first time use create account"
                        Snackbar.make(binding.root, toast, Snackbar.LENGTH_SHORT).show()

                    }
                }


        }

        binding.createAcountBtn.setOnClickListener{
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }
    }
}