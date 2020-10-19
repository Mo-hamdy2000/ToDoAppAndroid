package com.example.android.todoapp.database

import androidx.room.ColumnInfo


data class CategoryTest  (
    @ColumnInfo(name = "task_category")
    var categoryId: Long = 0L,
 //   var categoryColor: Long = 0L,
    @ColumnInfo(name = "count(*)")
      var numberOfTasks: Long = 0L
)