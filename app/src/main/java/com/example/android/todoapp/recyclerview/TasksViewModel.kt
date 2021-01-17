package com.example.android.todoapp.recyclerview

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.todoapp.database.AppDatabaseDao
import com.example.android.todoapp.database.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TasksViewModel (val database : AppDatabaseDao, application: Application):AndroidViewModel(application) {
    //the categories in database
    val categories=database.getAllCategories()
    // the tasks in database
    val tasks=database.getAllTasks()
    //tasks that has status with value 1 (they are done)
     val checkedTasks=database.getDoneTasks()
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


    suspend fun update(task: Task){
        withContext(Dispatchers.IO){
            database.update(task)
        }
    }
    fun addButtonAfterChecked(){
        viewModelScope.launch {
            for(task in checkedTasks.value!!){
                task.dequeueTime=System.currentTimeMillis()
                update(task)
            }
        }
    }
    fun updateTheDoneTasks(tasks:List<Task>?){
        if(tasks==null){
            Log.i("the last message","there is no tasks")
        }
        else{
            var time=System.currentTimeMillis()
            for (task in tasks!!){
                Log.i("the task","${task} is updated")
                task.status=1.toShort()
                task.dequeueTime=time
                updateTheTasks(task)
        }
        }
    }
    fun updateTheTasks (task: Task){
        viewModelScope.launch {
            update(task)
        }
    }



}
