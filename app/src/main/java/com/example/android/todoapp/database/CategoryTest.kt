package com.example.android.todoapp.database

import androidx.room.ColumnInfo


data class CategoryTest  (
    @ColumnInfo(name = "task_category")
    var taskCategory : Long = 0L,
    @ColumnInfo(name = "count(*)")
    var tasksCount: Long = 0L
)