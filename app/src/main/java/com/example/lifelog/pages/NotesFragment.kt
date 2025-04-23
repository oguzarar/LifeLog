package com.example.lifelog.pages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lifelog.Notes.NotYazmaActivity
import com.example.lifelog.Notes.NotesAdapter
import com.example.lifelog.R
import com.example.lifelog.database.Database
import com.example.lifelog.database.Notes
import com.example.lifelog.database.Notesdao
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var notesList: ArrayList<Notes>
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //
        val view = inflater.inflate(R.layout.fragment_notes, container, false)
        val vt = Database(requireContext())

        recyclerView = view.findViewById(R.id.rvNotlar)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        //Tasarım nesnelerine erişim sağlama
        notesList = Notesdao().notGetir(vt)
        notesAdapter = NotesAdapter(requireContext(), notesList)
        recyclerView.adapter = notesAdapter

        for (note in notesList) {
            Log.e("Notlar", "Başlık: ${note.note_title}")
        }


        val fab = view.findViewById<FloatingActionButton>(R.id.fab_notEkle)

        fab.setOnClickListener{

            val intent = Intent(requireContext(), NotYazmaActivity::class.java)     //fragmentin bağlı olduğu contextten, notYazmaActivitye geçiş
            startActivity(intent)
        }

        return view
    }
    //Kaydedilen bir notun anında notlar bölümde görüntülenebilmesi.
    override fun onResume() {
        super.onResume()

        val vt = Database(requireContext())

        notesList.clear()
        notesList.addAll(Notesdao().notGetir(vt))
        notesAdapter.notifyDataSetChanged()
    }

}