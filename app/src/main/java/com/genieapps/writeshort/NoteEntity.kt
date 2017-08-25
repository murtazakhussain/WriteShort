package com.genieapps.writeshort

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

/**
 * Created on 8/22/2017.
 */

@IgnoreExtraProperties
class NoteEntity {
    var note:String? = null
    var timestamp:String? = Date().time.toString()

    constructor()

    constructor(note:String) {
        this.note = note
    }

    override fun toString():String {
        return "NoteEntity(note=$note, timestamp=$timestamp)"
    }


}
