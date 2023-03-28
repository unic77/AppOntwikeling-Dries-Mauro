package com.example.appdriesenmauro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
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
                R.id.mnuHome -> goToHome()
                R.id.mnuFavourites -> goToFavourites()
                R.id.mnuAddActivity -> goTOAddActivity()
                R.id.mnuGoToSupportPage -> gotToSupport()
            }
            true
        }

    }

    private fun goToHome() {
        activityFragment.switchFiewToHome()
    }

    private fun gotToSupport(){
        var snak = Snackbar.make(binding.root, "sopport not online", Snackbar.LENGTH_LONG)
        snak.show()
    }

    private fun goToFavourites(){
        activityFragment.switchFiewToFavorite()
    }

    private fun goTOAddActivity(){
        /*var intent = Intent(this, AddActivityActivity::class.java)
        startActivity(intent)*/ // vragen of dit nu een activity is of een fragment

        var addActivityFragment = AddActivityActivity(activityFragment,this);
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