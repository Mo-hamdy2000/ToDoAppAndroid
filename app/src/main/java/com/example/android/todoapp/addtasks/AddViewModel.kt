package com.example.android.todoapp.addtasks

import android.accounts.AuthenticatorDescription
import android.app.Application
import android.database.Cursor
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.todoapp.database.AppDatabaseDao
import com.example.android.todoapp.database.Category
import com.example.android.todoapp.database.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddViewModel(val database : AppDatabaseDao, application: Application): AndroidViewModel(application) {
    val categories = database.getAllCategories()

    private lateinit var category: Category
    private lateinit var task:Task

    fun getTaskData(id: Long , title : String ,description: String , taskStatus : Short ,addTime : Long , dequeTime : Long , categoryID : Long  ){
            task= Task(id , title , description , taskStatus , addTime , dequeTime , categoryID)
    }

    fun getCategoryData (title : String , colorID: Long ){
        category= Category(0, title , colorID)
    }

    private suspend fun insert(task : Task){
        withContext(Dispatchers.IO){
            database.insert(task)
        }
    }
    private suspend fun insert(category : Category){
        withContext(Dispatchers.IO){
            database.insert(category)
        }
    }

    //will be used in fragment
    fun onAddTask(){
        viewModelScope.launch {
            insert(task)
        }

    }
    fun onAddCategory(){
        viewModelScope.launch {
            insert(category)
        }
    }



}