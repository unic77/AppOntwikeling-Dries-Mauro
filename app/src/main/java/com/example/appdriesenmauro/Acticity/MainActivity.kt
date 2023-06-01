package com.example.appdriesenmauro.Acticity

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.appdriesenmauro.Fragment.ActivityFragment
import com.example.appdriesenmauro.Fragment.AddActivityFragment
import com.example.appdriesenmauro.R
import com.example.appdriesenmauro.classes.Activity
import com.example.appdriesenmauro.classes.User
import com.example.appdriesenmauro.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var menuBarToggle: ActionBarDrawerToggle
    private lateinit var activityFragment: ActivityFragment
    private lateinit var user: User


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        checkPermission()

        val mAuth = FirebaseAuth.getInstance()

        val headerView : View = binding.navView.getHeaderView(0)

        val pfBinding = headerView.findViewById<ImageView>(R.id.profileFoto)
        val emailBinding = headerView.findViewById<TextView>(R.id.txtVNameUser)
        val logOutButton = headerView.findViewById<Button>(R.id.btnLogOut)

        logOutButton.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
            mAuth.signOut()
        }

        if(mAuth.currentUser != null) {
            val storage = Firebase.storage
            val localFile = File.createTempFile(mAuth.currentUser!!.uid, ".jpg")
            val pathReference =
                storage.getReference("profilePic/" + mAuth.currentUser!!.uid + ".jpg")

            emailBinding.text = mAuth.currentUser?.email

            pathReference.getFile(localFile).addOnSuccessListener {
                val userPfp = BitmapFactory.decodeFile(localFile.absolutePath)
                pfBinding.setImageBitmap(userPfp)
                localFile.delete()
                user = User(mAuth.currentUser!!.uid, userPfp)

                val broadcastInent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(localFile))
                this.applicationContext.sendBroadcast(broadcastInent)

                setup()
            }.addOnFailureListener {
                mAuth.signOut()
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
            }
        }
        else{
            logOutButton.text = "log in"
            setup()
            val mnuAddActivity = binding.navView.menu.findItem(R.id.mnuAddActivity)
            mnuAddActivity.isVisible = false
        }
    }

    private fun checkPermission() {
        val permissionWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val permissionRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if(permissionWrite != PackageManager.PERMISSION_GRANTED || permissionRead != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 100)
            Log.d(TAG,"requesting permission")
        }
    }

    private fun setup(){
        activityFragment = ActivityFragment()
        readSavedEvents()
        setupActivityListFragment()
        setupMenuDrawer()
        setContentView(binding.root)
    }

    private fun readSavedEvents(){
        val files: Array<String>? = fileList()

        if (files != null) {
            for (item in files) {

                val fileInputStream : FileInputStream? = openFileInput(item)
                val inputStreamReader = InputStreamReader(fileInputStream)
                val bufferedReader = BufferedReader(inputStreamReader)

                val stringBuilder: StringBuilder = StringBuilder()
                var text: String? = null

                while (run {
                        text = bufferedReader.readLine()
                        text
                    } != null){
                    stringBuilder.append(text)
                }

                val jsonString = stringBuilder.toString()
                val gson = Gson()


                val opgeslagenEvent = gson.fromJson(jsonString, Activity::class.java)

                opgeslagenEvent.phEvent = BitmapFactory.decodeByteArray(opgeslagenEvent.phEventForStorage, 0 ,opgeslagenEvent.phEventForStorage!!.size)
                opgeslagenEvent.pfUser = BitmapFactory.decodeByteArray(opgeslagenEvent.pfUserForStorage, 0, opgeslagenEvent.pfUserForStorage!!.size)

                activityFragment.addSavedActivity(opgeslagenEvent)
            }
        }
    }

    private fun setupActivityListFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frmActivitieContainer,activityFragment)
            commit()
        }
    }

    private fun setupMenuDrawer() {
        menuBarToggle = ActionBarDrawerToggle(this,binding.drawerLayout,
            R.string.open_menu,
            R.string.close_menu
        )
        binding.drawerLayout.addDrawerListener(menuBarToggle)

        menuBarToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.mnuHome -> goToHome()
                R.id.mnuFavourites -> goToFavourites()
                R.id.mnuAddActivity -> goTOAddActivity()
                R.id.mnuGoToSupportPage -> gotToSupport()
            }
            true
        }

    }

    private fun goToHome() {
        binding.drawerLayout.closeDrawer(Gravity.LEFT)
        activityFragment.switchFiewToHome()
    }

    private fun gotToSupport(){
        binding.drawerLayout.closeDrawer(Gravity.LEFT)
        val intent = Intent(this, SupportActivity::class.java);
        startActivity(intent)
    }

    private fun goToFavourites(){
        activityFragment.switchFiewToFavorite()
        binding.drawerLayout.closeDrawer(Gravity.LEFT)
    }

    private fun goTOAddActivity(){

        val addActivityFragment = AddActivityFragment(activityFragment,this,user);
        switchTo(addActivityFragment)
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

   fun switchTo(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frmActivitieContainer, fragment)
            addToBackStack("Fragment_${fragment.id}")
            commit()
        }
   }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(menuBarToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}