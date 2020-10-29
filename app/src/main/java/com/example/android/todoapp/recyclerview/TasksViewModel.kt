package com.example.android.todoapp.recyclerview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.todoapp.database.AppDatabaseDao
import com.example.android.todoapp.database.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TasksViewModel (val database :AppDatabaseDao, application: Application):AndroidViewModel(application) {
    // the tasks in database
    val tasks=database.getAllTasks()
    //selected task
    private val _navigateToTaskDetail =MutableLiveData<Long>()
    val navigateToTaskDetail
    get() = _navigateToTaskDetail

    //job
    val viewModelJob= Job()
    fun onTaskClicked(id : Long){
        _navigateToTaskDetail.value=id
    }
    fun onTaskDetailNavigateComplete(){
        _navigateToTaskDetail.value=null
    }
    fun getColor(task: Task):Long{
        val category=database.getCategory(task.categoryId)
        return  category!!.categoryColor
    }




}
