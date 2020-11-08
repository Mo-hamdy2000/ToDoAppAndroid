package com.example.android.todoapp.database

import android.graphics.Color

class SampleData {
    companion object {
        fun populateCategoriesData(): List<Category>{
            return listOf(
                Category(1, "Home",Color.rgb(255, 0, 0).toLong()),
                Category(2, "Work", Color.rgb(0, 225, 255).toLong()),
                Category(3, "Personal", Color.rgb(255, 255, 0).toLong()),
                Category(4, "Education", Color.rgb(0, 0, 255).toLong()))
        }

        fun populateTasksData(): List<Task>{
            return listOf(
                Task(1, "Shopping", "Go to Carrefour", 1, 1593640800000, 1593727200000, 1),
                Task(2, "Preparing Launch", "Call Dominoes pizza for Delivery", 1, 1593813600000, 1593986400000, 1),
                Task(3, "Doing Homework", "Chapter 6 Anatomy", 1, 1594332000000, 1594418400000, 4),
                Task(4, "Meeting", "New Project Discussion", 1, 1594850400000, 1594885500000, 2),
                Task(5, "Cut My Hair", "Cut my hair", 2, 1595109600000, 1595160600000, 3),
                Task(6, "Ironing", "Iron my suit", 2, 1595541600000, 1595601600000, 3),
                Task(7, "Assignment", "Chapter 8 Thermodynamics", 1, 1596319200000, 1596404700000, 4),
                Task(8, "Studying Chemistry", "Chapter 1 Organic", 2, 1596455100000, 1596487500000, 4),
                Task(9, "Preparing Certificate", "Call to check for the certificate", 1, 1596455100000, 1597229100000, 3),
                Task(10, "Maintenance", "Fix the garden tape", 1, 1596530700000, 1596876300000, 1),
                Task(11, "Project 1123", "Submit project final edition", 1, 1596962700000, 1598172300000, 2),
                Task(12, "Laundry", "Send clothes to laundry", 2, 1597308300000, 1597941900000, 1),
                Task(13, "Project 1548", "Submit project final edition", 1, 1597682700000, 1598978700000, 2),
                Task(14, "French Course", "Finish week 3 in the course", 1, 1598287500000, 1598546700000, 4),
                Task(15, "Work Out", "Go to GYM", 1, 1598604300000, 1598640300000, 3),
                Task(16, "Prepare My Bag", "Take the shower gel", 1, 1599072300000, 1599097500000, 3),
                Task(17, "Make my bed", "..", 1, 1599116400000, 1599127200000, 3),
                Task(18, "Prepare Breakfast", "..", 2, 1599285600000, 1599305340000, 1),
                Task(19, "Work Out", "Go to GYM", 1, 1599938940000, 1599992940000, 3),
                Task(20, "English Course", "Enroll on english course", 0, 1600252140000, 0, 4),
                Task(21, "Internship", "Apply for internship", 0, 1600597740000, 0, 2),
                Task(22, "Yasser Birthday", "Prepare for Yasser birthday", 2, 1600856940000, 1601202540000, 3),
                Task(23, "Shopping", "Back to school clothes", 1, 1601634540000, 1602152940000, 1),
                Task(24, "Meeting Youssef", "Having launch with youssef", 2, 1601893740000, 1602584940000, 3),
                Task(25, "Try Fifa 21", "Beating yasser on fifa", 0, 1601980140000, 0, 3),
                Task(26, "Studying OOP", "..", 0, 1602152940000, 0, 4),
                Task(27, "Water Plants", "..", 1, 1602239340000, 0, 1),
                Task(28, "Decore Home", "Put Ramadan Decorations", 2, 1602325740000, 1602757740000, 1),
                Task(29, "Discussion", "Discuss new Project Ideas", 1, 1602412140000, 1602671340000, 2),
                Task(30, "Studying Computer Org", "Study chapter 1", 0, 1602498540000, 0, 4),
                Task(31, "Discrete Assignment", "..", 1, 1602624540000, 1602761340000, 4)
            )
        }

       // fun populateQuotesData
    }
}