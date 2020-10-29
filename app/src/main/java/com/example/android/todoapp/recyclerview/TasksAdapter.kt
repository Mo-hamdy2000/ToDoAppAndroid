package com.example.android.todoapp.recyclerview


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.todoapp.database.AppDatabaseDao
import com.example.android.todoapp.database.Category
import com.example.android.todoapp.database.Task
import com.example.android.todoapp.databinding.ListItemTaskBinding

class TasksAdapter (private val clickListener :TaskListener, private val databaseDao: AppDatabaseDao): ListAdapter<Task, TasksAdapter.ViewHolder>(TaskDiffCallback()){


    private val categories=databaseDao.getAllCategories()
    private  lateinit var category:Category
    class ViewHolder private constructor(val binding: ListItemTaskBinding):RecyclerView.ViewHolder(binding.root) {
      fun bind(clickListener:TaskListener,item: Task,category : Category?){
          val color=category?.categoryColor
          binding.task=item
          binding.clickListener=clickListener
            if(color != null){
          binding.theCard.setCardBackgroundColor(color.toInt())
            }
          Log.i("3rd messgae","i completed it")
          binding.executePendingBindings()
    }
        companion object{
            fun from(parent: ViewGroup):ViewHolder{
                val layoutInflater=LayoutInflater.from(parent.context)
                val binding=ListItemTaskBinding.inflate(layoutInflater,parent,false)
                return  ViewHolder(binding)
            }
        }



    }
    class TaskDiffCallback :DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.taskId==newItem.taskId
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {

            return oldItem==newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return  ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=getItem(position)
        /*it may throw  a null exception in some exceptions but could be handled using elvis
        * operator
        * */
        //category= categories.value!![item.categoryId.toInt()]
        holder.bind( clickListener,item ,null )
    }

}
class TaskListener(val clickListener : (taskId : Long) -> Unit){
    fun onClick(task : Task)=clickListener(task.taskId)
}