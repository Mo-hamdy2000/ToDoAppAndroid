package com.example.android.todoapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="category_table")
data class Category  (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    var categoryId: Long = 0L,

    @ColumnInfo(name = "category_title")
    var categoryTitle: String,

    @ColumnInfo(name = "category_color")
    var categoryColor: Long = 0L
//    @ColumnInfo(name = "number_of_tasks")
//    var numberOfTasks: Long = 0L
)