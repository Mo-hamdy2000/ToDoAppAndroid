package com.example.android.todoapp.addtasks

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.todoapp.database.AppDatabaseDao
import com.example.android.todoapp.recyclerview.TasksViewModel
import java.lang.IllegalArgumentException

class AddViewModelFactory(
    private val dataSource : AppDatabaseDao,
    private val application: Application
): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddViewModel::class.java)){
            return AddViewModel(dataSource,application) as T
        }
        throw IllegalArgumentException("unknown viewModel class")
    }
}