package com.genieapps.writeshort.adapters

import android.widget.TextView
import com.genieapps.writeshort.NoteEntity
import com.genieapps.writeshort.R
import com.genieapps.writeshort.helpers.DateHelper
import com.zhy.adapter.recyclerview.base.ItemViewDelegate
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * Created on 8/22/2017.
 */
open class NotesListDelegate: ItemViewDelegate<NoteEntity> {

    override fun getItemViewLayoutId():Int = R.layout.item_notes

    override fun convert(holder:ViewHolder?, entity:NoteEntity?, position:Int) {
        val noteTextView = holder?.getView<TextView>(R.id.note)
        val txtTimestamp = holder?.getView<TextView>(R.id.txtTimestamp)
        if (entity != null) {
            noteTextView?.text = entity.note
            txtTimestamp?.text = DateHelper.getDate(entity.timestamp?.toLong()!!, "dd/MM/yyyy")
        }
    }

    override fun isForViewType(item:NoteEntity?, position:Int):Boolean = true

}
