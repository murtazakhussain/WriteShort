package com.genieapps.writeshort.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.genieapps.writeshort.NoteEntity
import com.genieapps.writeshort.R
import com.genieapps.writeshort.adapters.BaseListAdapter
import com.genieapps.writeshort.adapters.NotesListDelegate
import com.genieapps.writeshort.helpers.FirebaseManager

class NoteListActivity: AppCompatActivity() {

    private var rvNotes:RecyclerView? = null

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_notes)

        initUI()
        setActionListener()
    }

    private fun initUI() {
        rvNotes = findViewById(R.id.rvNotes) as RecyclerView
        rvNotes?.layoutManager = LinearLayoutManager(this);
    }

    private fun setActionListener() {
        val adapter = BaseListAdapter(this, ArrayList<NoteEntity>())
        adapter.addItemViewDelegate(NotesListDelegate())
        rvNotes?.adapter = adapter

        FirebaseManager(this).getNotes(object: FirebaseManager.DataReadyListener {
            override fun onDataReady(list:ArrayList<out Any>) {
                adapter.updateData(list as ArrayList<NoteEntity>)
                adapter.notifyDataSetChanged()
            }
        })
    }
}
