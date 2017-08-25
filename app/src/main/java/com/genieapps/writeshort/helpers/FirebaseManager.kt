package com.genieapps.writeshort.helpers

import android.content.Context
import com.genieapps.writeshort.NoteEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*


/**
 * Created on 8/22/2017.
 */

class FirebaseManager(private var context:Context) {

    private lateinit var database:FirebaseDatabase
    private var notesDb:DatabaseReference? = null
    private var currentUserId:String? = null
    private var listener:DataReadyListener? = null

    init {
        currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        initDB()
    }

    fun insertANote(note:NoteEntity, completeListener:DatabaseReference.CompletionListener) {
        val referenceKey = notesDb?.push()?.key
        notesDb
                ?.child(currentUserId)
                ?.child(referenceKey)
                ?.setValue(note, completeListener)
    }

    fun getNotes(listener:DataReadyListener) {
        val items:ArrayList<NoteEntity> = ArrayList()
        val orderByChild = notesDb?.child(currentUserId)
        orderByChild?.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot:DataSnapshot) {
                dataSnapshot.children
                        .map { it.getValue<NoteEntity>(NoteEntity::class.java) }
                        .forEach { items.add(it) }

                Collections.reverse(items)
                listener.onDataReady(items)
            }

            override fun onCancelled(databaseError:DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }

    private fun initDB() {
        database = FirebaseDatabase.getInstance()
        notesDb = database.getReference("notes")
    }

    fun createNewReference(refStr:String?):DatabaseReference? {
        return database.getReference(refStr)
    }

    interface DataReadyListener {
        fun onDataReady(list:ArrayList<out Any>)
    }
}
