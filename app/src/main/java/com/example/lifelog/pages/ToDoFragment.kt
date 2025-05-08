package com.example.lifelog.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lifelog.ToDoList.RecView
import com.example.lifelog.database.Database
import com.example.lifelog.database.TaskDaos.todolist.ToDoList
import com.example.lifelog.database.TaskDaos.todolist.ToDoListDao
import com.example.lifelog.databinding.FragmentToDoBinding


class ToDoFragment : Fragment() {
    private lateinit var Tasklist: ArrayList<ToDoList>
    private lateinit var adapter: RecView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding= FragmentToDoBinding.inflate(inflater ,container, false) //Tasarım üzerindeki nesneler bağlandı.
        val vt= Database(requireContext()) //Eğer main activty üzerinde çalışsaydık this olacakctı ama fragmenta olduğumuz için requireContext()

        binding.ToDoAddButton.setOnClickListener {
            val Task=binding.TaskPlainText.text.trim()//Yazılan veri alındı.

            if(Task.toString().trim().isEmpty()){ //Veritabanına eklemeden önce edittext içi boş olup olmadığını kontrol ediyor.
                binding.TaskPlainText.setText("") //Boş ise eklemiycek ve uyarı verecek
                Toast.makeText(requireContext(),"Bir şeyler yazın", Toast.LENGTH_SHORT).show() //Uyarı metnini değiştirin.
            }

            else{//Eğer birşeyler yazılmışsa veritabanına eklenecek
                val task= ToDoList(task = Task.toString())
                ToDoListDao().TaskAdd(vt,task)//Yazılan veri veritabanına eklendi
                binding.TaskPlainText.setText("")//Edittext içi siliniyor.

                //Yeni veri eklendiğinde rv güncelleniyor.
                Tasklist= ToDoListDao().GetAllTask(vt)
                adapter= RecView(requireContext(),Tasklist.toMutableList())
                binding.RV.adapter=adapter
            }
        }

        //Veritabanında veri çekilip rv'ye gönderildi.
        binding.RV.setHasFixedSize(true)
        binding.RV.layoutManager= LinearLayoutManager(requireContext())
        Tasklist= ToDoListDao().GetAllTask(vt)
        adapter= RecView(requireContext(),Tasklist.toMutableList())
        binding.RV.adapter=adapter

        return binding.root
    }

}