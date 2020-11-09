package com.lumisdinos.chessclock

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.lumisdinos.chessclock.data.Constants._1
import com.lumisdinos.chessclock.data.Constants._10
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


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    DialogListener {

    private val ACTION_SET_CUSTOM_TIME = "100"

    private lateinit var viewDataBinding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityMainBinding.inflate(layoutInflater)

        navigationView = viewDataBinding.navView
        drawerLayout = viewDataBinding.drawerLayout

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        navigationView.setupWithNavController(navController)
        navigationView.setNavigationItemSelectedListener(this)

        val view = viewDataBinding.root
        setContentView(view)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.close()
        val time: String

        when (item.itemId) {

            R.id.t_15_10 -> { time = _15_10 }
            R.id.t_5_5 -> { time = _5_5 }
            R.id.t_3_2 -> { time = _3_2 }
            R.id.t_2_1 -> { time = _2_1 }
            R.id.t_1_1 -> { time = _1_1 }
            R.id.t_45_45 -> { time = _45_45 }
            R.id.t_60 -> { time = _60 }
            R.id.t_30 -> { time = _30 }
            R.id.t_20 -> { time = _20 }
            R.id.t_10 -> { time = _10 }
            R.id.t_5 -> { time = _5 }
            R.id.t_3 -> { time = _3 }
            R.id.t_1 -> { time = _1 }

            else -> {//customTime
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
                time = ""
            }

        }
        refreshSomeFragment(navController.currentDestination, time)
        return true
    }


    fun openDrawer() {
        drawerLayout.open()
    }


    @SuppressLint("RestrictedApi")
    fun refreshSomeFragment(destination: NavDestination?, time: String) {
        if (time.isEmpty() || destination == null) return
        try {
            val displayName = destination.displayName//com.lumisdinos.projection:id/newTable
            val index = displayName.indexOfLast { c -> c.toString() == "/" }

            if (index == -1) return
            val name = displayName.substring(index + 1)
            if (getString(R.string.nav_home).equals(name, ignoreCase = true)) {
                val homeFragment = getCurrentFragment()
                if (homeFragment != null && homeFragment is HomeFragment) {
                    homeFragment.setChosenTimeControl(time)
                }
            }
        } catch (e: Exception) {
            Timber.d("Exception: %s", e.message)
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
        when (result[0]) {
            ACTION_SET_CUSTOM_TIME -> {
                refreshSomeFragment(navController.currentDestination, result[1])
            }
            else -> { }
        }
    }

    override fun onNegativeDialogClick(result: List<String>) {
    }

    override fun onNeutralDialogClick(result: List<String>) {
    }


}
