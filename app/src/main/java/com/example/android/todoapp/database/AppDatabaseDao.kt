package com.example.android.todoapp.database
import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AppDatabaseDao {

    @Insert
    fun insert(task: Task)

    @Insert
    fun insertAll(tasks: List<Task>)

    @Insert
    fun insert(category: Category)

    @Insert
    fun insertAllCategories(categories: List<Category>)

    @Update
    fun update(task: Task)

    @Update
    fun update(category: Category)

    @Query("SELECT * from task_table WHERE task_id = :taskId")
    fun getTask(taskId: Long): Task?

    @Query("SELECT * from category_table WHERE category_id = :categoryId")
    fun getCategory(categoryId: Long): Category?

    @Query("DELETE FROM task_table")
    fun clearTasks()

    @Query("DELETE FROM category_table")
    fun clearCategories()

    @Query("SELECT * FROM task_table ORDER BY task_id DESC")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM category_table ORDER BY category_id DESC")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM category_table ORDER BY category_id DESC")
    fun getAllCategoriesPie(): List<Category>

    @Query("SELECT * FROM task_table Where task_category = :categoryId ORDER BY task_id DESC")
    fun getTasksWithCategory(categoryId: Long): LiveData<List<Task>>

    @Query("SELECT * FROM task_table Where task_listing_time >= :listingTime AND task_dequeue_time <= :dequeueTime ORDER BY task_id DESC")
    fun getTasksWithDateRange(listingTime: Long, dequeueTime: Long): LiveData<List<Task>>

    @Query("SELECT t.range, count(*) " +
            "from ( SELECT task_id, CASE " +
            "WHEN task_listing_time >= :monthStartTime AND task_listing_time < :monthStartTime + (604800*1000) THEN 1 " +
            "WHEN task_listing_time >= :monthStartTime + (604800*1000) AND task_listing_time < :monthStartTime + (2*604800*1000) THEN 2 " +
            "WHEN task_listing_time >= :monthStartTime + (2*604800*1000) AND task_listing_time < :monthStartTime + (3*604800*1000) THEN 3 " +
            "WHEN  task_listing_time >= :monthStartTime + (3*604800*1000) AND task_listing_time < :monthEndTime THEN 4 " +
            "ELSE 0 end as range from task_table Where task_listing_time >= :monthStartTime AND task_listing_time < :monthEndTime) t " +
            "GROUP BY t.range")
    fun getTasksCountInMonthWeeks(monthStartTime: Long ,monthEndTime: Long): List<CountRecord>

    @Query("SELECT t.range, count(*) " +
            "from (SELECT task_id, CASE " +
            "WHEN task_dequeue_time >= :monthStartTime AND task_dequeue_time < :monthStartTime + (604800*1000) THEN 1 " +
            "WHEN task_dequeue_time >= :monthStartTime + (604800*1000) AND task_dequeue_time < :monthStartTime + (2*604800*1000) THEN 2 " +
            "WHEN task_dequeue_time >= :monthStartTime + (2*604800*1000) AND task_dequeue_time < :monthEndTime + (3*604800*1000) THEN 3 " +
            "WHEN task_dequeue_time >= :monthEndTime + (3*604800*1000) AND task_dequeue_time < :monthEndTime THEN 4 " +
            "ELSE 0 end as range from task_table Where task_status = 1 AND task_dequeue_time >= :monthStartTime " +
            "AND task_dequeue_time < :monthEndTime) t " +
            "GROUP BY t.range")
    fun getTasksFinishedCountInMonthWeeks(monthStartTime: Long, monthEndTime: Long): List<CountRecord>

    @Query("SELECT task_category , count(*) FROM task_table WHERE task_dequeue_time BETWEEN :fromTime AND :toTime GROUP BY task_category ORDER BY task_category DESC ")
    fun getTasksByCategoryWithin(fromTime : Long,toTime : Long) : List<CategoryTest>


    @Query("SELECT :monthEndTime")
    fun getTest(monthEndTime: Long): Long

//    @Query("SELECT count(*) FROM task_table WHERE task_category = :taskCategory")
//    fun getTasksCountByCategory(taskCategory: Long): Long
    @Query("SELECT count(*) From task_table GROUP BY task_category ORDER BY task_category DESC")
    fun getCategoryTasksCount() : List<Int>
    @Query("SELECT * FROM task_table")
    fun getTasksInfo(): List<Task>


    @Query("SELECT * FROM category_table")
    fun getCategoriesInfo(): List<Category>
    //to get the tasks that are done and still appear to user in recyclerview
    @Query("SELECT * FROM task_table Where task_status=2 ")
    fun getDoneTasks(): LiveData<List<Task>>

}