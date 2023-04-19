package com.example.appdriesenmauro

import android.content.Intent
import android.graphics.Bitmap

data class Activity(val title: String, val date: String, val context: String, val data: Intent?, val pfUser: Bitmap?, val postedUser: Int, var boolean: Boolean)