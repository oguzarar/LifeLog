package com.example.lifelog.database.TaskDaos

import com.example.lifelog.database.Database

interface TaskDaos<T> {

    fun TaskAdd(vt: Database,item:T)

    fun GetAllTask(vt:Database): ArrayList<T>

    fun DeleteTask(vt: Database,Task_id: Int)
}