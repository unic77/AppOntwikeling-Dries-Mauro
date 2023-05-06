package com.example.appdriesenmauro

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.appdriesenmauro.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
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
    private lateinit var mAuth: FirebaseAuth
    private lateinit var storage: FirebaseStorage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        mAuth = FirebaseAuth.getInstance()

        //byteArray omzetten naar bitmap

        var userPfp: Bitmap? = null

        storage = Firebase.storage
        val localFile = File.createTempFile(mAuth.currentUser!!.uid,".jpg")
        val pathRefrenc = storage.getReference("profilePic/"+mAuth.currentUser!!.uid+".jpg")

        /*pathRefrenc.getFile(localFile).onSuccessTask {
            
        }*/
        userPfp = BitmapFactory.decodeFile(localFile.absolutePath)


        val headerView : View = binding.navView.getHeaderView(0)

        val pfBinding = headerView.findViewById<ImageView>(R.id.profileFoto)
        val emailBinding = headerView.findViewById<TextView>(R.id.txtVNameUser)
        val logOutButton = headerView.findViewById<Button>(R.id.btnLogOut)

        pfBinding.setImageBitmap(userPfp)

        emailBinding.setText(mAuth.currentUser?.email)

        //pfBinding.setImageBitmap(user.pfBitmap)

        user = User(mAuth.currentUser!!.uid,true,userPfp)
        activityFragment = ActivityFragment(user)

        logOutButton.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
            mAuth.signOut()
        }


        pathRefrenc.getFile(localFile).addOnSuccessListener {
            userPfp = BitmapFactory.decodeFile(localFile.absolutePath)
            pfBinding.setImageBitmap(userPfp)
            user = User(mAuth.currentUser!!.uid,true,userPfp)
            activityFragment = ActivityFragment(user)


            readSavedEvents()
            setupActivityListFragment()
            setupMenuDrawer()
            setContentView(binding.root)
        }
    }

    private fun readSavedEvents(){
        var files: Array<String>? = fileList()
        if (files != null) {
            for (item in files) {

                var fileInputStream : FileInputStream? = null
                fileInputStream = openFileInput(item)
                var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
                val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)

                val stringBuilder: StringBuilder = StringBuilder()
                var text: String? = null

                while ({text = bufferedReader.readLine(); text}() != null){
                    stringBuilder.append(text)
                }

                var jsonString = stringBuilder.toString()
                val gson = Gson()

                var opgeslagenEvent = gson.fromJson(jsonString,Activity::class.java)

                System.out.print("Print hier de byteArray: ")
                System.out.println(opgeslagenEvent.dataForStorage)

                opgeslagenEvent.data = BitmapFactory.decodeByteArray(opgeslagenEvent.dataForStorage, 0 ,opgeslagenEvent.dataForStorage!!.size)
                opgeslagenEvent.pfUser = BitmapFactory.decodeByteArray(opgeslagenEvent.dataForPFStorage, 0, opgeslagenEvent.dataForPFStorage!!.size)

                activityFragment.addSavedActivity(opgeslagenEvent)
            }
        }
        System.out.println("Ik geraak hier!!!")
    }

    private fun setupActivityListFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frmActivitieContainer,activityFragment)
            commit()
        }
    }

    private fun setupMenuDrawer() {
        menuBarToggle = ActionBarDrawerToggle(this,binding.drawerLayout,R.string.open_menu, R.string.close_menu)
        binding.drawerLayout.addDrawerListener(menuBarToggle)

        menuBarToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.btnLogOut -> goToProfile()
                R.id.mnuHome -> goToHome()
                R.id.mnuFavourites -> goToFavourites()
                R.id.mnuAddActivity -> goTOAddActivity()
                R.id.mnuGoToSupportPage -> gotToSupport()
            }
            true
        }

    }


    private fun goToProfile() {
        val snak = Snackbar.make(binding.root, "dries is gay", Snackbar.LENGTH_LONG)
        snak.show()
        binding.drawerLayout.closeDrawer(Gravity.LEFT)
    }

    private fun goToHome() {
        binding.drawerLayout.closeDrawer(Gravity.LEFT)
        activityFragment.switchFiewToHome()
    }

    private fun gotToSupport(){
        binding.drawerLayout.closeDrawer(Gravity.LEFT)
        val snak = Snackbar.make(binding.root, "support not online", Snackbar.LENGTH_LONG)
        snak.show()
    }

    private fun goToFavourites(){
        activityFragment.switchFiewToFavorite()
        binding.drawerLayout.closeDrawer(Gravity.LEFT)
    }

    private fun goTOAddActivity(){

        val addActivityFragment = AddActivityActivity(activityFragment,this,user);
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