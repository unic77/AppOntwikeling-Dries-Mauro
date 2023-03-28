package com.example.appdriesenmauro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appdriesenmauro.databinding.ActivityCreateAccountBinding

class CreateAccountActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}