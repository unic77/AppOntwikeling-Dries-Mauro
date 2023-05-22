package com.example.appdriesenmauro

import android.graphics.Bitmap

data class Activity(val title: String, val date: String, val context: String, var phEvent: Bitmap?, var pfUser: Bitmap?, val postedUser: String?, var favorite: Boolean, var phEventForStorage:ByteArray?, var pfUserForStorage:ByteArray?)