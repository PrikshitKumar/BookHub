package com.prikshitkumar.bookhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.prikshitkumar.bookhub.R
import com.prikshitkumar.bookhub.fragments.Dashboard
import com.prikshitkumar.bookhub.fragments.Favorite
import com.prikshitkumar.bookhub.fragments.Info
import com.prikshitkumar.bookhub.fragments.Profile
import com.prikshitkumar.bookhub.util.ConnectionManager

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinateLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView

    var previousMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeTheFields()
        setupToolbar()
        checkInternetConnectivity()
        openDashboard()

        val actionBarDrawerToggle= ActionBarDrawerToggle(this@MainActivity, drawerLayout, R.string.open_Drawer, R.string.close_Drawer)

        //Adding ClickListener for Hamburger Icon
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()  // When the Navigation Drawer is shown then, the Hamburger Icon changes to Back Arrow and vice versa

        // ClickListeners for items of Navigation Drawer
        navigationView.setNavigationItemSelectedListener {

            /* When we pressed the back button then our application's Activity gets back and close the App but we want to get back to the
            * previously selected fragment. For that we are using "BACK-STACK" like {transaction.addToBackStack("Dashboard")}, but we want
            * always to open the Dasboard Fragment when clicked back button otherwise close the application if current activity is Dashboard
            * For that we have to override the onBackPressed() method */

            if(previousMenuItem != null) {  // If some menuItem is checked and selected previously then we will uncheck that first in this block
                previousMenuItem?.isChecked = false
            }
            it.isCheckable = true   // isCheckable and isChecked are used for current selected MenuItem
            it.isChecked = true
            previousMenuItem = it    // Add the current seleted MenuItem to previousMenuItem variable for deselect that next time

            when(it.itemId) {
                R.id.id_Dashboard -> {
                    openDashboard()
                    drawerLayout.closeDrawers()
                }
                R.id.id_Favorites -> {
                    openFavorite()
                    drawerLayout.closeDrawers()
                }
                R.id.id_Profile -> {
                    openProfile()
                    drawerLayout.closeDrawers()
                }
                R.id.id_info -> {
                    openInfo()
                    drawerLayout.closeDrawers()
                }
            }
            // this is lambda representation that's why we use return statement with @setNavigationItemSelectedListener
            return@setNavigationItemSelectedListener true
        }
    }

    fun initializeTheFields() {
        drawerLayout = findViewById(R.id.id_drawerLayout)
        coordinateLayout = findViewById(R.id.id_coordinateLayout)
        toolbar = findViewById(R.id.id_toolbar)
        frameLayout = findViewById(R.id.id_frameLayout)
        navigationView = findViewById(R.id.id_navigationView)
    }

    fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {   // ClickListener for Hamburger Icon
        val id = item.itemId
        if(id == android.R.id.home) {  // android.R.id.home is the ID of Hamburger Icon
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.id_frameLayout)
        if(drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawers()
        }
        else {
            when(fragment) {
                !is Dashboard -> openDashboard() /* If current Fragment is not Dashboard Fragment and when we pressed the back button then open
            the Dashboard Fragment. Otherwise close the application.. */
                else -> super.onBackPressed()
            }
        }
    }

    fun openDashboard() {
        val fragment = Dashboard()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.id_frameLayout, fragment)
        transaction.commit()
        supportActionBar?.title = "Dashboard"
        navigationView.setCheckedItem(R.id.id_Dashboard) //Set the Dashboard Fragment as currently selected Layout and highlights it in Menu
    }

    fun openFavorite() {
        val fragment = Favorite()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.id_frameLayout, fragment)
        transaction.commit()
        supportActionBar?.title = "Favorite"
    }

    fun openProfile() {
        val fragment = Profile()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.id_frameLayout, fragment)
        transaction.commit()
        supportActionBar?.title = "Profile"
    }

    fun openInfo() {
        val fragment = Info()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.id_frameLayout, fragment)
        transaction.commit()
        supportActionBar?.title = "About App"
    }

    fun checkInternetConnectivity() {
        if(ConnectionManager().checkConnectivity(this@MainActivity)) {
            // Internet is available
        }
        else {
            // Internet is not available
            Toast.makeText(this, "Enable the Internet before accessing the App", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}