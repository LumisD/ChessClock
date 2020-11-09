package com.lumisdinos.chessclock

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.lumisdinos.chessclock.common.Event
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


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewDataBinding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    private val _navigationItemSelected = MutableLiveData<Event<String>>()
    val navigationItemSelected: LiveData<Event<String>> = _navigationItemSelected


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
                time = getString(R.string.custom_time)
            }

        }
        _navigationItemSelected.value = Event(time)
        return true
    }


    fun openDrawer() {
        drawerLayout.open()
    }

}
