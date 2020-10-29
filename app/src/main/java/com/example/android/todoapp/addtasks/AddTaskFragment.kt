package com.example.android.todoapp.addtasks

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.android.todoapp.R
import com.example.android.todoapp.database.AppDatabase
import com.example.android.todoapp.database.AppDatabaseDao
import com.example.android.todoapp.database.Category
import com.example.android.todoapp.database.Task
import com.example.android.todoapp.databinding.AddtaskfragentBinding
import com.example.android.todoapp.recyclerview.TasksViewModel
import com.example.android.todoapp.recyclerview.TasksViewModelFactory
import kotlinx.android.synthetic.main.addtaskfragent.view.*
import kotlinx.coroutines.*

class AddTaskFragment : Fragment() ,AdapterView.OnItemSelectedListener{
    val scope= CoroutineScope(Job() + Dispatchers.IO)
    private lateinit var category: Category
    private lateinit var madapter:SpinnerAdapter
    private lateinit  var cursor: Cursor
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        scope.async {

        }
        val binding : AddtaskfragentBinding=DataBindingUtil.inflate(inflater,R.layout.addtaskfragent,container,false)
        val spinner =binding.categroySpinner
        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getInstance(application).appDatabaseDao
        val viewModelFactory = AddViewModelFactory(dataSource,application)

        val addViewModel = ViewModelProvider(this,viewModelFactory).get(AddViewModel::class.java)
        binding.lifecycleOwner=this

        addViewModel.category.observe(viewLifecycleOwner, Observer {
            madapter= SpinnerAdapter(this.requireActivity(),it)
            spinner.adapter=madapter
        })
        spinner.onItemSelectedListener=this
        binding.addButton.setOnClickListener { view ->
            if(binding.editTextTitle.text.toString().trim().length==0){
                return@setOnClickListener
            }
            if(binding.editTextDescription.text.toString().trim().length==0){
                binding.editTextDescription.setText(" ")
            }
            val task= Task(0,binding.editTextTitle.text.toString(),binding.editTextDescription.text.toString(),0,2132,3123,category.categoryId)
            scope.launch {
                dataSource.insert(task)
            }
        }

        return binding.root


    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        category= p0?.getItemAtPosition(p2) as Category
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        category=p0?.getItemAtPosition(1)as Category
    }


}