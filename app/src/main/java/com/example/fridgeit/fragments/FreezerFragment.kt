package com.example.fridgeit.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fridgeit.DatabaseHelper
import com.example.fridgeit.R
import kotlinx.android.synthetic.main.fragment_fridge.view.*

class FreezerFragment : Fragment() {
    lateinit var dbHelper: DatabaseHelper
    var gridLayoutManager:GridLayoutManager? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dbHelper = DatabaseHelper(context)

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_freezer, container, false)

        val list = dbHelper.getAllRecords("2")
        val adapterItem = AdapterItem(context!!,list)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerview)

        gridLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.layoutManager = gridLayoutManager;
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapterItem
        val search = view.etSearch
        search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty())
                {
                    val list = dbHelper.searchData("2",s.toString())
                    val adapterItem = AdapterItem(context!!,list)
                    gridLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
                    recyclerView.layoutManager = gridLayoutManager;
                    recyclerView.setHasFixedSize(true)
                    recyclerView.adapter = adapterItem
                }
                else{
                    val list = dbHelper.getAllRecords("2")
                    val adapterItem = AdapterItem(context!!,list)
                    gridLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
                    recyclerView.layoutManager = gridLayoutManager;
                    recyclerView.setHasFixedSize(true)
                    recyclerView.adapter = adapterItem
                }


            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        return view
    }


}