package com.example.lifelog.ToDoList

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifelog.R
import com.example.lifelog.database.Database
import com.example.lifelog.database.ToDoList
import com.example.lifelog.database.ToDoListdao

class RecView(private var mContext: Context, private val TasksList: MutableList<ToDoList>):
    RecyclerView.Adapter<RecView.CardViewNesneTutucu>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecView.CardViewNesneTutucu {
        val design= LayoutInflater.from(mContext).inflate(R.layout.card_view_tasarim_rv,parent,false)
        return CardViewNesneTutucu(design)

    }

    override fun onBindViewHolder(holder: RecView.CardViewNesneTutucu, position: Int) {
        val vt= Database(mContext)
        val task=TasksList[position]
        holder.ToDoText.text=task.task
        holder.checkBox.setOnCheckedChangeListener({buttonView,isChecked->
            if(isChecked){
                ToDoListdao().DeleteTask(vt,task.task_id!!.toInt())
                TasksList.removeAt(position)
                notifyItemRemoved(position)
            }
        })

    }

    override fun getItemCount(): Int {
        return TasksList.size
    }

    inner class CardViewNesneTutucu(view: View): RecyclerView.ViewHolder(view){
        var SatirCardView: CardView
        var ToDoText: TextView
        var checkBox: CheckBox

        init{
            SatirCardView=view.findViewById(R.id.SatirCardView)
            ToDoText=view.findViewById(R.id.ToDoText)
            checkBox=view.findViewById(R.id.checkBox)
        }
    }

}