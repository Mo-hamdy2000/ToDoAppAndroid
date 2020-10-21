package com.example.android.todoapp


import android.os.Bundle
import android.view.View
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.android.todoapp.database.AppDatabase
import com.example.android.todoapp.database.AppDatabaseDao
import com.example.android.todoapp.database.Category
import com.example.android.todoapp.database.Task
import com.example.android.todoapp.tracker.DatePickerFragment
import com.example.android.todoapp.tracker.TrackerViewModel
import kotlinx.coroutines.*
import com.example.android.todoapp.databinding.ActivityMainBinding

import dev.sasikanth.colorsheet.ColorSheet
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tasksrecylerview.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawer: DrawerLayout
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var selectedColor: Int = ColorSheet.NO_COLOR
    companion object {
        fun getApp() {
            return
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        drawer = binding.drawerLayout
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        val bottomNav = binding.bottomNavigationView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.tasksFragment, R.id.tracker), drawer_layout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        bottomNav.setupWithNavController(navController)
        drawer.addDrawerListener(toggle)
        toggle.syncState()


    }


    override fun onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)

        } else {
            super.onBackPressed()
        }
    }

    /*uiScope.launch {
            insertCat(Category(1, "Home", 0), dataSource)
            insertCat(Category(2, "Work", 0), dataSource)
        }*/


    val cal = Calendar.getInstance()

/*
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.add(Calendar.DAY_OF_MONTH, 1)
        val task1 = Task(0, "Shopping", "go to Carrefour", 1, cal.timeInMillis, 0, 1)
        cal.add(Calendar.DAY_OF_MONTH, 3)
        task1.dequeueTime = cal.timeInMillis
        uiScope.launch {
            insert(task1, dataSource)
        }

        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.add(Calendar.DAY_OF_MONTH, 1)
        cal.add(Calendar.WEEK_OF_MONTH, 1)
        val task2 = Task(0, "Eating", "go to chicken chester", 2, cal.timeInMillis, 0, 1)
        val task3 = Task(0, "Meeting", "Company meeting", 1, cal.timeInMillis, 0, 2)
        cal.add(Calendar.DAY_OF_MONTH, 2)
        task3.dequeueTime = cal.timeInMillis
        uiScope.launch {
            insert(task2, dataSource)
            insert(task3, dataSource)
        }

        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.add(Calendar.DAY_OF_MONTH, 1)
        cal.add(Calendar.WEEK_OF_MONTH, 2)
        val task4 = Task(0, "Work out", "go to GYM", 1, cal.timeInMillis, 0, 1)
        cal.add(Calendar.DAY_OF_MONTH, 2)
        task4.dequeueTime = cal.timeInMillis
        uiScope.launch {
            insert(task4, dataSource)
        }

        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.add(Calendar.DAY_OF_MONTH, 1)
        cal.add(Calendar.WEEK_OF_MONTH, 3)
        val task5 = Task(0, "Procrastination", "Just relax", 1, cal.timeInMillis, 0, 1)
        cal.add(Calendar.DAY_OF_MONTH, 1)
        task5.dequeueTime = cal.timeInMillis
        uiScope.launch {
            insert(task5, dataSource)
        }*/

    /*cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.add(Calendar.MONTH, 1)
        uiScope.launch {
            //get1(dataSource)
            get(cal.timeInMillis, dataSource)
        }*/
    override fun onStop() {
        super.onStop()
        viewModelJob.cancel()
    }

    private suspend fun insert(task: Task, database: AppDatabaseDao) {
        withContext(Dispatchers.IO) {
            println(task.categoryId)
            database.insert(task)
        }
    }

    private suspend fun get1(database: AppDatabaseDao) {
        withContext(Dispatchers.IO) {
            println("Offff")
            val values = database.getAllTasks()
            println(values.toString())
            /*for (value in values.value!!.iterator()) {
                println(value)
            }*/
        }
    }

    private suspend fun get(database: AppDatabaseDao) {
        withContext(Dispatchers.IO) {
            println("Offff")
            val tasks = database.getTasksInfo()
            for (task in tasks) {
                println(task.taskId.toString() + " " + task.title + " " + task.listingTime + " " + task.status + " " + task.categoryId)
            }
        }
    }

        private suspend fun insertCat(category: Category, database: AppDatabaseDao) {
            withContext(Dispatchers.IO) {
                database.insert(category)
            }
        }

        private suspend fun clear(database: AppDatabaseDao) {
            withContext(Dispatchers.IO) {
                database.clearTasks()
            }
        }
    }