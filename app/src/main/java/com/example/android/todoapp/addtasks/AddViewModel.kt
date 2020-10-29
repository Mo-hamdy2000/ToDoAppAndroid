package com.example.android.todoapp.addtasks

import android.app.Application
import android.database.Cursor
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.android.todoapp.database.AppDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddViewModel(val database : AppDatabaseDao, application: Application): AndroidViewModel(application) {
    val category = database.getAllCategories()








}