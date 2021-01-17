package com.example.android.todoapp.addtasks


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.todoapp.R
import com.example.android.todoapp.database.AppDatabase
import com.example.android.todoapp.database.Category
import com.example.android.todoapp.databinding.AddtaskfragentBinding
import dev.sasikanth.colorsheet.ColorSheet
import kotlinx.coroutines.*
class AddTaskFragment : Fragment() ,AdapterView.OnItemSelectedListener {

    val scope = CoroutineScope(Job() + Dispatchers.IO)

    companion object {
        private const val COLOR_SELECTED = "selectedColor"
        private const val NO_COLOR_OPTION = "noColorOption"
    }

    private lateinit var category: Category
    private lateinit var madapter: SpinnerAdapter

    private var selectedColor: Int = ColorSheet.NO_COLOR
    private var noColorOption = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: AddtaskfragentBinding =
            DataBindingUtil.inflate(inflater, R.layout.addtaskfragent, container, false)
        val spinner = binding.categroySpinner
        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getInstance(application).appDatabaseDao
        val viewModelFactory = AddViewModelFactory(dataSource, application)

        val addViewModel = ViewModelProvider(this, viewModelFactory).get(AddViewModel::class.java)
        binding.lifecycleOwner = this

        addViewModel.categories.observe(viewLifecycleOwner, Observer {
            madapter = SpinnerAdapter(this.requireActivity(), it)
            spinner.adapter = madapter
        })
        spinner.onItemSelectedListener = this


        binding.addButton.setOnClickListener {
            if (binding.editTextTitle.text.toString().trim().isEmpty()) {
                return@setOnClickListener
            }
            if (binding.editTextDescription.text.toString().trim().isEmpty()) {
                binding.editTextDescription.setText(" ")
            }
            addViewModel.getTaskData(
                0, binding.editTextTitle.text.toString(),
                binding.editTextDescription.text.toString(), 0.toShort(), System.currentTimeMillis(),
                0, category.categoryId
            )
            addViewModel.onAddTask()
        }


        binding.addCategoryButton.setOnClickListener {
        addViewModel.getCategoryData(binding.editTextCategory.text.toString(), selectedColor.toLong())
            addViewModel.onAddCategory()

    }

        val colors = resources.getIntArray(R.array.colors)
        selectedColor = savedInstanceState?.getInt(COLOR_SELECTED) ?: colors.first()


        noColorOption = savedInstanceState?.getBoolean(NO_COLOR_OPTION) ?: false
        binding.addColorButton.setOnClickListener{
            ColorSheet().cornerRadius(8).colorPicker(
                colors,noColorOption = noColorOption,selectedColor = selectedColor,
                listener = { color->
                    selectedColor = color


                }
            ).show(childFragmentManager)
        }
        binding.editTextCategory.addTextChangedListener(object  : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                var categoryName : String = binding.editTextCategory.text.toString().trim()
                binding.addCategoryButton.isEnabled = categoryName.isNotEmpty()
                binding.addColorButton.isEnabled = categoryName.isNotEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })

        return binding.root


    }




    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        category = p0?.getItemAtPosition(p2) as Category
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        category = p0?.getItemAtPosition(1) as Category
    }
}
