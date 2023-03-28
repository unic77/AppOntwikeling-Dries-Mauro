package com.example.appdriesenmauro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appdriesenmauro.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var menuBarToggle: ActionBarDrawerToggle
    private var activityFragment = ActivityFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setupLoginActivity()
        setupActivityListFragment()
        setupMenuDrawer()
        setContentView(binding.root)
    }

private fun setupLoginActivity(){
    UserActivity()
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
                R.id.mnuAddActivity -> goTOAddActivity()
                R.id.mnuFavourites -> goToFavourites()
                R.id.mnuGoToSupportPage -> gotToSupport()
            }
            true
        }

    }

    private fun gotToSupport(){
        var snak = Snackbar.make(binding.root, "sopport not online", Snackbar.LENGTH_LONG)
        snak.show()
    }

    private fun goToFavourites(){
        var snak = Snackbar.make(binding.root, "favourites not online", Snackbar.LENGTH_LONG)
        snak.show()
    }

    private fun goTOAddActivity(){
        var addActivityFragment = AddActivityFragment(activityFragment,this);
        switchTo(addActivityFragment)
    }

   fun switchTo(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frmActivitieContainer, fragment)
            addToBackStack("Fragment_${fragment.id}")
            commit()
        }
    }
}