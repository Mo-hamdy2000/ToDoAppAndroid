package com.example.android.todoapp.tracker

import android.app.Application
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.android.todoapp.R
import com.example.android.todoapp.database.AppDatabase
import com.example.android.todoapp.database.AppDatabaseDao
import com.example.android.todoapp.databinding.TrackerFragmentBinding
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.coroutines.*
import java.util.*


class TrackerFragment : Fragment() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        println("Iam Here")
        val binding : TrackerFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.tracker_fragment,container,false)
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).appDatabaseDao
        binding.lifecycleOwner=this


        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)

        uiScope.launch {
            get(application, cal.timeInMillis, dataSource, binding)
        }




        return binding.root
    }

    private suspend fun get(application: Application, timeSec: Long, database: AppDatabaseDao, binding: TrackerFragmentBinding){
        withContext(Dispatchers.IO) {

            //database.insertAll(SampleData.populateTasksData())

            val c = database.getCategoriesInfo()
            for (category in c) {
                println(category.categoryId.toString() + " " + category.categoryTitle + " " + category.categoryColor)
            }

            val v = database.getTasksInfo()
            for (task in v) {
                println(task.taskId.toString() + " " + task.title + " " + task.dequeueTime + " " + task.status)
            }
            val values = database.getTasksCountInMonthWeeks(timeSec)
            println(values.toString())
            val tempEntries = Array<Int>(4) {0}
            for (value in values) {
                if (value.week == 1)
                    tempEntries[0]  = value.count
                else if (value.week == 2)
                    tempEntries[1]  = value.count
                else if (value.week == 3)
                    tempEntries[2]  = value.count
                else if (value.week == 4)
                    tempEntries[3]  = value.count
            }
            val values2 = database.getTasksFinishedCountInMonthWeeks(timeSec)
            val tempEntries2 = Array<Int>(4) {0}
            for (value in values2) {
                if (value.week == 1)
                    tempEntries2[0]  = value.count
                else if (value.week == 2)
                    tempEntries2[1]  = value.count
                else if (value.week == 3)
                    tempEntries2[2]  = value.count
                else if (value.week == 4)
                    tempEntries2[3]  = value.count
            }
            val entries1 = tempEntries.mapIndexed { index, arrayList ->
                Entry(index.toFloat() + 1f, tempEntries[index].toFloat()) }
            val entries2 = tempEntries2.mapIndexed { index, arrayList ->
                Entry(index.toFloat() + 1f, tempEntries2[index].toFloat()) }

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
            val xAxis: XAxis = lineChartView.getXAxis()
            val xAxisLabel: ArrayList<String> = ArrayList()
            xAxisLabel.add("Week 1")
            xAxisLabel.add("Week 2")
            xAxisLabel.add("Week 3")
            xAxisLabel.add("Week 4")
            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float, axis: AxisBase?): String? {
                    return xAxisLabel[value.toInt()]
                }
            }
            lineChartView.data = LineData(lineDataSets as List<ILineDataSet>?)
            /*lineChartView.xAxis.mAxisMaximum = 5f
            lineChartView.xAxis.mAxisMinimum = 0f*/
            lineChartView.axisLeft.mAxisMaximum = 10f
            lineChartView.axisLeft.mAxisMinimum = 0f
            lineChartView.axisLeft.mAxisRange = 10f
            println("data added")
            lineChartView.invalidate()
        }
    }
}