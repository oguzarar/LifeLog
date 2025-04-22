package com.example.lifelog.pages

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lifelog.R
import com.example.lifelog.database.Database
import com.example.lifelog.database.ToDoListdao
import com.example.lifelog.databinding.FragmentToDoBinding


class ToDoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding= FragmentToDoBinding.inflate(inflater ,container, false) //Tasarım üzerindeki nesneler bağlandı.
        val vt= Database(requireContext()) //Eğer main activty üzerinde çalışsaydık this olacakctı ama fragmenta olduğumuz için requireContext()

        binding.ToDoAddButton.setOnClickListener {
            val Task=binding.TaskPlainText.text.trim()
            if(Task.toString().trim().isEmpty()){ //Veritabanına eklemeden önce edittext içi boş olup olmadığını kontrol ediyor.
                binding.TaskPlainText.setText("") //Boş ise eklemiycek ve uyarı verecek
                Toast.makeText(requireContext(),"Bir şeyler yazın", Toast.LENGTH_SHORT).show() //Uyarı metnini değiştirin.
            }else{//Eğer birşeyler yazılmışsa veritabanına eklenecek
                ToDoListdao().TaskAdd(vt,Task.toString())
                binding.TaskPlainText.setText("")
            }
        }



        return binding.root
    }
}