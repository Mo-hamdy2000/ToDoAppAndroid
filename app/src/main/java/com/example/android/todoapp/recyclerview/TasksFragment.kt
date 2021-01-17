package com.example.android.todoapp.recyclerview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.android.todoapp.R
import com.example.android.todoapp.database.AppDatabase
import com.example.android.todoapp.database.Category
import com.example.android.todoapp.database.Task
import com.example.android.todoapp.databinding.TasksrecylerviewBinding
import kotlinx.coroutines.*

class TasksFragment : Fragment() {
    val scope= CoroutineScope(Job()+Dispatchers.IO)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : TasksrecylerviewBinding=DataBindingUtil.inflate(inflater, R.layout.tasksrecylerview,container,false)

        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getInstance(application).appDatabaseDao


        val viewModelFactory = TasksViewModelFactory(dataSource,application)

        val tasksViewModel = ViewModelProvider(this,viewModelFactory).get(TasksViewModel::class.java)

        binding.lifecycleOwner=this
     
        val adapter = TasksAdapter(TaskListener { taskId ->tasksViewModel.onTaskClicked(taskId)  },dataSource)
        tasksViewModel.categories.observe(viewLifecycleOwner, Observer {
            adapter.categories=it
        })


        binding.TasksList.adapter=adapter


        tasksViewModel.tasks.observe(viewLifecycleOwner, Observer {
          it?.let{

             adapter.submitList(it)

           }
        })
        // to store the value kept updated from live data
        var tasksChecked : List<Task>?=null
        tasksViewModel.checkedTasks.observe(viewLifecycleOwner, Observer {
            it?.let{
                tasksChecked=it
            }
        })
        binding.tasksAreDoneButton.setOnClickListener{
            Log.i("9th message", "element are selected")
            Log.i("test","${tasksChecked} empty or not")
            tasksViewModel.updateTheDoneTasks(tasksChecked)

        }


        binding.RoundedButton.setOnClickListener {v->
            v.findNavController().navigate(R.id.action_tasksFragment_to_addTaskFragment)
            Log.i("2nd messgae","i completed it")
        }

        return binding.root
    }

}
