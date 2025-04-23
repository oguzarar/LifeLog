package com.example.lifelog.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lifelog.AddPages.AddPagesRecView
import com.example.lifelog.R
import com.example.lifelog.database.AddPagesDao
import com.example.lifelog.database.Database
import com.example.lifelog.database.Plugins
import com.example.lifelog.databinding.FragmentMainPageBinding

class MainPageFragment : Fragment() {
    private lateinit var Pluginlist: ArrayList<Plugins>
    private lateinit var adapter: AddPagesRecView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding= FragmentMainPageBinding.inflate(inflater,container,false)
        val vt= Database(requireContext())

        binding.MainPageRV.setHasFixedSize(true)
        binding.MainPageRV.layoutManager= LinearLayoutManager(requireContext())
        Pluginlist= AddPagesDao().getAllPlugin(vt)
        adapter= AddPagesRecView(requireContext(),Pluginlist.toMutableList())
        binding.MainPageRV.adapter=adapter


        return binding.root
    }}
