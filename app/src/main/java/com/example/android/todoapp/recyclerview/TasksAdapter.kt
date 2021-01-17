package com.example.android.todoapp.recyclerview


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.todoapp.database.AppDatabaseDao
import com.example.android.todoapp.database.Category
import com.example.android.todoapp.database.Task
import com.example.android.todoapp.databinding.ListItemTaskBinding
import kotlinx.coroutines.*

class TasksAdapter (private val clickListener :TaskListener,val database: AppDatabaseDao): ListAdapter<Task, TasksAdapter.ViewHolder>(TaskDiffCallback()){
    lateinit var categories : List<Category>
    class ViewHolder private constructor(val binding: ListItemTaskBinding):RecyclerView.ViewHolder(binding.root) {

        private val scope = CoroutineScope(Dispatchers.IO+ Job())

        fun bind(clickListener:TaskListener,item: Task,category : Category?,database: AppDatabaseDao){

          binding.task=item
          binding.clickListener=clickListener
          binding.category=category
          binding.checkBox.setOnCheckedChangeListener(null)
          binding.checkBox.isChecked = item.status!=0.toShort()
          binding.checkBox.setOnClickListener{
            if(item.status == 2.toShort()){
               item.status = 0.toShort()
            }
            else if (item.status==0.toShort())
            {
               item.status = 2.toShort()
            }
            scope.launch {
                  database.update(item)
            }
          }
          binding.executePendingBindings()
            if(item.dequeueTime != 0.toLong()){
                binding.root.visibility=View.GONE
                binding.theCard.visibility=View.GONE
                binding.checkBox.visibility=View.GONE
                binding.DescriptionText.visibility=View.GONE
                binding.TitleText.visibility=View.GONE
                binding.theConstraint.visibility=View.GONE
            }else{
                binding.root.visibility=View.VISIBLE
                binding.theCard.visibility=View.VISIBLE
                binding.checkBox.visibility=View.VISIBLE
                binding.DescriptionText.visibility=View.VISIBLE
                binding.TitleText.visibility=View.VISIBLE
                binding.theConstraint.visibility=View.VISIBLE
            }
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


        val resultCategory = categories.find { Category ->
            Category.categoryId==item.categoryId
        }
        holder.bind( clickListener,item , resultCategory,database)
    }

}

class TaskListener(val clickListener : (taskId : Long) -> Unit){
    fun onClick(task : Task)=clickListener(task.taskId)
}