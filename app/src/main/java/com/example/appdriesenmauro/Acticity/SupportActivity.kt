package com.example.appdriesenmauro.Acticity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appdriesenmauro.databinding.ActivitySupportBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class SupportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val view = binding.root

        binding.btnResetPassword.setOnClickListener {

            val eMail = binding.txtPassWordReset.text.toString()

            if(eMail != ""){
                sendResetPasswordEmail(eMail,view)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                val mAuth = FirebaseAuth.getInstance()

                if(mAuth.currentUser != null){
                    mAuth.signOut()
                }
            }
            else{
                Snackbar.make(view,"no email address given",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendResetPasswordEmail(eMail:String,view: View){
        val mAuth = FirebaseAuth.getInstance()
        mAuth.sendPasswordResetEmail(eMail)
            .addOnSuccessListener {
                Snackbar.make(view,"reset password email has been sent",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Snackbar.make(view,"email could not be sent, is the email correct?",Toast.LENGTH_SHORT).show()
            }
    }
}