package com.example.android.todoapp.tracker

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.android.todoapp.R
import com.example.android.todoapp.database.AppDatabase
import com.example.android.todoapp.database.AppDatabaseDao
import com.example.android.todoapp.databinding.TrackerFragmentBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.coroutines.*
import java.util.*


class TrackerFragment : Fragment() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)
    private lateinit var viewModel: TrackerViewModel
    private lateinit var binding : TrackerFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(inflater, R.layout.tracker_fragment,container,false)
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).appDatabaseDao
        viewModel = TrackerViewModel(application, dataSource)
        uiScope.launch {
            getCategories(dataSource)
        }
        binding.datePickButton.setOnClickListener {
            val newFragment = DatePickerFragment(viewModel)
            newFragment.show(parentFragmentManager, "datePicker")
        }
        binding.lifecycleOwner=this
        viewModel.isUpdated.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (!it) {
                uiScope.launch {
                    get(binding)
                    getCategories(dataSource)
                }
            }
        })

        return binding.root
    }

    private suspend fun get(binding: TrackerFragmentBinding){
        withContext(Dispatchers.IO) {

            /*val valuesAdded = database.getTasksCountInMonthWeeks(timeSec)
            val tempEntries = Array<Int>(4) {0}
            for (value in valuesAdded) {
                if (value.week == 1)
                    tempEntries[0]  = value.count
                else if (value.week == 2)
                    tempEntries[1]  = value.count
                else if (value.week == 3)
                    tempEntries[2]  = value.count
                else if (value.week == 4)
                    tempEntries[3]  = value.count
            }
            val valuesFinished = database.getTasksFinishedCountInMonthWeeks(timeSec)
            val tempEntries2 = Array<Int>(4) {0}
            for (value in valuesFinished) {
                if (value.week == 1)
                    tempEntries2[0]  = value.count
                else if (value.week == 2)
                    tempEntries2[1]  = value.count
                else if (value.week == 3)
                    tempEntries2[2]  = value.count
                else if (value.week == 4)
                    tempEntries2[3]  = value.count
            }*/

            val linesData = viewModel.getLinesData()

            val entries1 = linesData[0].mapIndexed { index, arrayList ->
                Entry(index.toFloat()+1.0f, linesData[0][index].toFloat()) }
            val entries2 = linesData[1].mapIndexed { index, arrayList ->
                Entry(index.toFloat()+1.0f, linesData[1][index].toFloat()) }

            val lineChartView = binding.lineChartView
            val lineDataSet1 = LineDataSet(entries1, "Added Tasks")
            lineDataSet1.color = Color.RED
            lineDataSet1.setDrawValues(false)
            lineDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT)

            val lineDataSet2 = LineDataSet(entries2, "Finished Tasks")
            lineDataSet2.color = Color.BLUE
            lineDataSet2.setDrawValues(false)
            lineDataSet2.setAxisDependency(YAxis.AxisDependency.LEFT)


            val lineDataSets = arrayListOf(lineDataSet1, lineDataSet2)
            lineChartView.data = LineData(lineDataSets as List<ILineDataSet>?)
            val xAxis: XAxis = lineChartView.getXAxis()
            xAxis.setDrawGridLines(false)
            xAxis.labelCount = 6
            xAxis.axisMinimum = 0f
            xAxis.axisMaximum = 5f
            xAxis.valueFormatter = object :
                ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    println(value.toString())
                    println(value.toInt().toString())
                    return getLabel(value.toInt())
                }
            }
            lineChartView.axisLeft.mAxisMaximum = 10f
            lineChartView.axisLeft.mAxisMinimum = 0f
            lineChartView.axisLeft.mAxisRange = 10f
            lineChartView.invalidate()
        }
    }

    fun getLabel(value: Int):String{
        if (value == 1) {
            return "Week 1"
        } else if (value == 2) {
            return "Week 2"
        } else if (value == 3) {
            return "Week 3"
        } else if (value == 4) {
            return "Week 4"
        } else
            return ""
    }

    private suspend fun getCategories(dataSource: AppDatabaseDao) {
        lateinit var dataSet: PieDataSet
        var pieData: PieData
        val yValues = ArrayList<PieEntry>()
        val pieChart: PieChart = binding.categoriesPie
        withContext(Dispatchers.IO) {
            val categories = dataSource.getAllCategoriesPie()
            val tasksNumber = dataSource.getTasksByCategoryWithin(0,99994418400000)
            val colors = mutableListOf<Int>()
            print(tasksNumber.toString())
            for (c in tasksNumber.indices) {
                yValues.add(PieEntry(tasksNumber[c].tasksCount.toFloat()
                    , categories[tasksNumber[c].taskCategory.toInt()-1].categoryTitle))
                //api
                colors.add(categories[tasksNumber[c].taskCategory.toInt()-1].categoryColor.toInt())
            }
            Log.i("Colors", colors.toString())
                dataSet = PieDataSet(yValues, "Categories")
            val formatter: ValueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(
                    value: Float): String? {
                    return value.toInt().toString()
                }
            }
            dataSet.valueFormatter = formatter
                dataSet.colors = colors
                pieData = PieData(dataSet)
                pieChart.data = pieData
                pieChart.isDrawHoleEnabled = false
                //pieData.setDrawValues(false)
            pieChart.setDrawEntryLabels(false)
            dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE;

            pieChart.isDrawHoleEnabled = true
            pieChart.holeRadius = 70F
            pieChart.description.isEnabled = false
            pieChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
//            val legend: Legend = pieChart.legend
//            legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
//            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
//            legend.orientation = Legend.LegendOrientation.VERTICAL
//            legend.setDrawInside(false)
            Log.i("WHAT", dataSource.getTasksByCategoryWithin(1593727200000,1654418400000).toString())
            pieChart.invalidate()
            }

        }
}