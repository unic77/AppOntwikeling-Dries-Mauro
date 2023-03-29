package com.example.appdriesenmauro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
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

        setupActivityListFragment()
        setupMenuDrawer()
        setContentView(binding.root)
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
        var snak = Snackbar.make(binding.root, "dries is gay", Snackbar.LENGTH_LONG)
        snak.show()
        binding.drawerLayout.closeDrawer(Gravity.LEFT)
    }

    private fun goToHome() {
        binding.drawerLayout.closeDrawer(Gravity.LEFT)
        activityFragment.switchFiewToHome()
    }

    private fun gotToSupport(){
        binding.drawerLayout.closeDrawer(Gravity.LEFT)
        var snak = Snackbar.make(binding.root, "sopport not online", Snackbar.LENGTH_LONG)
        snak.show()
    }

    private fun goToFavourites(){
        activityFragment.switchFiewToFavorite()
        binding.drawerLayout.closeDrawer(Gravity.LEFT)
    }

    private fun goTOAddActivity(){
        /*var intent = Intent(this, AddActivityActivity::class.java)
        startActivity(intent)*/ // vragen of dit nu een activity is of een fragment

        var addActivityFragment = AddActivityActivity(activityFragment,this);
        switchTo(addActivityFragment)
        binding.drawerLayout.closeDrawer(Gravity.LEFT)
    }

   fun switchTo(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frmActivitieContainer, fragment)
            addToBackStack("Fragment_${fragment.id}")
            commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // we need to do this to respond correctly to clicks on menu items, otherwise it won't be caught
        if(menuBarToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}