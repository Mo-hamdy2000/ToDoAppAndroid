package com.example.android.todoapp.addtasks

import android.content.Context
import android.content.res.Resources
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.cursoradapter.widget.CursorAdapter
import com.example.android.todoapp.R
import com.example.android.todoapp.database.Category


class SpinnerAdapter(context: Context,list: List<Category>) : ArrayAdapter<Category>(context,0,list){
    private lateinit var temp:View
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position,convertView,parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView : View?, parent: ViewGroup):View{

        if(convertView == null){
             temp = LayoutInflater.from(context).inflate(R.layout.text,parent,false)
        }else{
             temp=convertView
        }
        val theText:TextView=temp.findViewById(R.id.hello)
        val category:Category?=getItem(position)
        if (category!=null){
            theText.setText(category.categoryTitle)
            theText.setBackgroundColor(category.categoryColor.toInt())
        }


        return temp
    }
}