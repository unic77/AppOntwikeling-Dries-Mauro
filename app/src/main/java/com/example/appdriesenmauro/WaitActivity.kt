package com.example.appdriesenmauro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

class WaitActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wait)
        Handler().postDelayed(Runnable {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }, 1000)
    }
}