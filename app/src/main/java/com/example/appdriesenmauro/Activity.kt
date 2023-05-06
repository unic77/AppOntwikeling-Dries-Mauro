package com.example.appdriesenmauro

import android.content.Intent
import android.graphics.Bitmap

data class Activity(val title: String, val date: String, val context: String, var data: Bitmap?, var pfUser: Bitmap?, val postedUser: String?, var boolean: Boolean, var dataForStorage:ByteArray?, var dataForPFStorage:ByteArray?)