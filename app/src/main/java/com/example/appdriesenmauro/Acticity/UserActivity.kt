package com.example.appdriesenmauro.Acticity

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.appdriesenmauro.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class UserActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth;


    public override fun onStart() {
        super.onStart()
        mAuth = Firebase.auth
        if(mAuth.currentUser != null){
           val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = Firebase.auth

        binding.loginBtn.setOnClickListener{

            val connManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            val mobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            val email = binding.emailadress.text.toString()
            val password = binding.Passporttxt.text.toString()

            if (TextUtils.isEmpty(email)){
                Snackbar.make(binding.root, "enter an email address", Snackbar.LENGTH_SHORT).show()
            }
            else if (TextUtils.isEmpty(password)){
                Snackbar.make(binding.root, "enter a password", Snackbar.LENGTH_SHORT).show()
            }
            else if(!wifi!!.isConnected && !mobile!!.isConnected){
                Snackbar.make(binding.root, "Connect to the internet before creating an account", Snackbar.LENGTH_SHORT).show()
            }
            else {
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Snackbar.make(binding.root, "User successfully logged in", Snackbar.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)

                        } else {
                            Snackbar.make(binding.root, "Failed, make sure that email and password is correct", Snackbar.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        binding.txtViewVergotPassword.setOnClickListener{
            val intent = Intent(this, SupportActivity::class.java)
            startActivity(intent)
        }

        binding.createAcountBtn.setOnClickListener{
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }
    }
}