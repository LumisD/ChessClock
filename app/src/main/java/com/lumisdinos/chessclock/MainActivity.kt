package com.lumisdinos.chessclock

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import com.lumisdinos.chessclock.data.Constants._1
import com.lumisdinos.chessclock.data.Constants._10
import com.lumisdinos.chessclock.data.Constants._10secTest
import com.lumisdinos.chessclock.data.Constants._15_10
import com.lumisdinos.chessclock.data.Constants._1_1
import com.lumisdinos.chessclock.data.Constants._20
import com.lumisdinos.chessclock.data.Constants._2_1
import com.lumisdinos.chessclock.data.Constants._3
import com.lumisdinos.chessclock.data.Constants._30
import com.lumisdinos.chessclock.data.Constants._3_2
import com.lumisdinos.chessclock.data.Constants._45_45
import com.lumisdinos.chessclock.data.Constants._5
import com.lumisdinos.chessclock.data.Constants._5_5
import com.lumisdinos.chessclock.data.Constants._60
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

    private lateinit var time_15_10MenuItem: MenuItem
    private lateinit var time_5_5MenuItem: MenuItem
    private lateinit var time_3_2MenuItem: MenuItem
    private lateinit var time_2_1MenuItem: MenuItem
    private lateinit var time_1_1MenuItem: MenuItem
    private lateinit var time_45_45MenuItem: MenuItem

    private lateinit var time_60minMenuItem: MenuItem
    private lateinit var time_30minMenuItem: MenuItem
    private lateinit var time_20minMenuItem: MenuItem
    private lateinit var time_10minMenuItem: MenuItem
    private lateinit var time_5minMenuItem: MenuItem
    private lateinit var time_3minMenuItem: MenuItem
    private lateinit var time_1minMenuItem: MenuItem


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

        time_15_10MenuItem = navigationView.menu.findItem(R.id.t_15_10)
        time_5_5MenuItem = navigationView.menu.findItem(R.id.t_5_5)
        time_3_2MenuItem = navigationView.menu.findItem(R.id.t_3_2)
        time_2_1MenuItem = navigationView.menu.findItem(R.id.t_2_1)
        time_1_1MenuItem = navigationView.menu.findItem(R.id.t_1_1)
        time_45_45MenuItem = navigationView.menu.findItem(R.id.t_45_45)

        time_60minMenuItem = navigationView.menu.findItem(R.id.t_60)
        time_30minMenuItem = navigationView.menu.findItem(R.id.t_30)
        time_20minMenuItem = navigationView.menu.findItem(R.id.t_20)
        time_10minMenuItem = navigationView.menu.findItem(R.id.t_10)
        time_5minMenuItem = navigationView.menu.findItem(R.id.t_5)
        time_3minMenuItem = navigationView.menu.findItem(R.id.t_3)
        time_1minMenuItem = navigationView.menu.findItem(R.id.t_1)

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

        time_15_10MenuItem.setOnMenuItemClickListener {
            Timber.d("qwer time15_10MenuItem tOnMenuItemClick")
            drawerLayout.close()
            refreshSomeFragment(navController.currentDestination, _15_10)
            true
        }
        time_5_5MenuItem.setOnMenuItemClickListener {
            Timber.d("qwer time_5_5MenuItem tOnMenuItemClick")
            drawerLayout.close()
            refreshSomeFragment(navController.currentDestination, _5_5)
            true
        }
        time_3_2MenuItem.setOnMenuItemClickListener {
            Timber.d("qwer time_3_2MenuItem tOnMenuItemClick")
            drawerLayout.close()
            refreshSomeFragment(navController.currentDestination, _3_2)
            true
        }
        time_2_1MenuItem.setOnMenuItemClickListener {
            Timber.d("qwer time_2_1MenuItem tOnMenuItemClick")
            drawerLayout.close()
            refreshSomeFragment(navController.currentDestination, _2_1)
            true
        }
        time_1_1MenuItem.setOnMenuItemClickListener {
            Timber.d("qwer time_1_1MenuItem tOnMenuItemClick")
            drawerLayout.close()
            refreshSomeFragment(navController.currentDestination, _1_1)
            true
        }
        time_45_45MenuItem.setOnMenuItemClickListener {
            Timber.d("qwer time_45_45MenuItem tOnMenuItemClick")
            drawerLayout.close()
            refreshSomeFragment(navController.currentDestination, _45_45)
            true
        }

        time_60minMenuItem.setOnMenuItemClickListener {
            Timber.d("qwer time_60minMenuItem tOnMenuItemClick")
            drawerLayout.close()
            refreshSomeFragment(navController.currentDestination, _60)
            true
        }
        time_30minMenuItem.setOnMenuItemClickListener {
            Timber.d("qwer time30minMenuItem tOnMenuItemClick")
            drawerLayout.close()
            refreshSomeFragment(navController.currentDestination, _30)
            true
        }
        time_20minMenuItem.setOnMenuItemClickListener {
            Timber.d("qwer time_20minMenuItem tOnMenuItemClick")
            drawerLayout.close()
            refreshSomeFragment(navController.currentDestination, _20)
            true
        }
        time_10minMenuItem.setOnMenuItemClickListener {
            Timber.d("qwer time_10minMenuItem tOnMenuItemClick")
            drawerLayout.close()
            refreshSomeFragment(navController.currentDestination, _10)
            true
        }
        time_5minMenuItem.setOnMenuItemClickListener {
            Timber.d("qwer time_5minMenuItem tOnMenuItemClick")
            drawerLayout.close()
            refreshSomeFragment(navController.currentDestination, _5)
            true
        }
        time_3minMenuItem.setOnMenuItemClickListener {
            Timber.d("qwer time_3minMenuItem tOnMenuItemClick")
            drawerLayout.close()
            refreshSomeFragment(navController.currentDestination, _3)
            true
        }
        time_1minMenuItem.setOnMenuItemClickListener {
            Timber.d("qwer time_1minMenuItem tOnMenuItemClick")
            drawerLayout.close()
            refreshSomeFragment(navController.currentDestination, _1)
            true
        }
    }


    fun openDrawer() {
        drawerLayout.open()
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
                    homeFragment.setChosenTimeControl(time)
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
        when(result[0]) {
            ACTION_SET_CUSTOM_TIME -> {
                refreshSomeFragment(navController.currentDestination, result[1])
            }
            else -> {}
        }
    }

    override fun onNegativeDialogClick(result: List<String>) {
    }

    override fun onNeutralDialogClick(result: List<String>) {
    }


}
