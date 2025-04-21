package com.example.lifelog.todoList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.example.lifelog.R
import com.example.lifelog.database.ToDoList

class RvAdapter(private val mContext: Context, private val ulkelerliste:List<ToDoList>): RecyclerView.Adapter<RvAdapter.CardviewTasarimNesneTutucu>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RvAdapter.CardviewTasarimNesneTutucu {
        val tasarim= LayoutInflater.from(mContext).inflate(R.layout.to_do_list_cardview_design,parent,false)
        return CardviewTasarimNesneTutucu(tasarim)
    }

    override fun onBindViewHolder(holder: RvAdapter.CardviewTasarimNesneTutucu, position: Int) {

    }

    override fun getItemCount(): Int {
        return 5
    }

    inner class CardviewTasarimNesneTutucu(view: View): RecyclerView.ViewHolder(view){

    }
}