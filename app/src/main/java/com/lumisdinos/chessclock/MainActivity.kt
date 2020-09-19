package com.lumisdinos.chessclock

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import com.lumisdinos.chessclock.databinding.ActivityMainBinding
import com.lumisdinos.chessclock.dialogs.DialogListener
import com.lumisdinos.chessclock.dialogs.alertDialogToSetCustomTime
import com.lumisdinos.chessclock.ui.home.HomeFragment
import timber.log.Timber
import java.lang.Exception

class MainActivity : AppCompatActivity(), DialogListener {

    private val ACTION_SET_CUSTOM_TIME = "100"

    //private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var viewDataBinding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var customTimeMenuItem: MenuItem
    private lateinit var time15_10MenuItem: MenuItem
    private lateinit var time30minMenuItem: MenuItem


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        viewDataBinding.lifecycleOwner = this

        navigationView = viewDataBinding.root.findViewById(R.id.nav_view)
        drawerLayout = viewDataBinding.drawerLayout

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        appBarConfiguration = AppBarConfiguration(setOf(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navigationView.setupWithNavController(navController)
        initializeMenuItemsAndSetListeners()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.navHostFragment)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }


    private fun initializeMenuItemsAndSetListeners() {
        customTimeMenuItem = navigationView.menu.findItem(R.id.customTime)
        time15_10MenuItem = navigationView.menu.findItem(R.id.t15_10)
        time30minMenuItem = navigationView.menu.findItem(R.id.t30)

        customTimeMenuItem.setOnMenuItemClickListener {
            Timber.d("qwer setTimeMenuItem tOnMenuItemClick")
            drawerLayout.close()
            alertDialogToSetCustomTime(
                this,
                LayoutInflater.from(this),//inflater
                ACTION_SET_CUSTOM_TIME,//action
                this,
                getString(R.string.custom_time),//title
                getString(R.string.set_min_sec_inc),//message
                getString(R.string.ok),
                getString(R.string.cancel)
            ).show()
            true
        }

        time15_10MenuItem.setOnMenuItemClickListener {
            Timber.d("qwer time15_10MenuItem tOnMenuItemClick")
            drawerLayout.close()
            refreshSomeFragment(navController.currentDestination, getString(R.string._15_10))
            true
        }

        time30minMenuItem.setOnMenuItemClickListener {
            Timber.d("qwer time30minMenuItem tOnMenuItemClick")
            drawerLayout.close()
            refreshSomeFragment(navController.currentDestination, getString(R.string._30))
            true
        }
    }


    @SuppressLint("RestrictedApi")
    fun refreshSomeFragment(destination: NavDestination?, time: String) {
        if (destination == null) return
        try {
            val displayName = destination.displayName//com.lumisdinos.projection:id/newTable
            val index = displayName.indexOfLast { c -> c.toString() == "/" }

            if (index == -1) return
            val name = displayName.substring(index + 1)
            if (getString(R.string.nav_home).equals(name, ignoreCase = true)) {
                //refresh list row20
                val homeFragment = getCurrentFragment()
                if (homeFragment != null && homeFragment is HomeFragment) {
                    homeFragment.setTime(time)
                }
            }
        } catch (e: Exception) {
            Timber.d("qwer Exception: %s", e.message)
        }
    }


    private fun getCurrentFragment(): Fragment? {
        val currentNavHost = supportFragmentManager.findFragmentById(R.id.navHostFragment)
        val currentFragmentClassName =
            (navController.currentDestination as FragmentNavigator.Destination).className
        return currentNavHost?.childFragmentManager?.fragments?.filterNotNull()?.find {
            it.javaClass.name == currentFragmentClassName
        }
    }


    //  -- DialogListener --

    override fun onPositiveDialogClick(result: List<String>) {
        Timber.d("qwer onPositiveDialogClick action: %s", result[0])
    }

    override fun onNegativeDialogClick(result: List<String>) {
    }

    override fun onNeutralDialogClick(result: List<String>) {
    }


}
