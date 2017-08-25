package com.genieapps.writeshort.adapters

import android.content.Context
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import java.util.*


/**
 * Created on 8/22/2017.
 */
class BaseListAdapter<T>(context:Context, items:List<T>): MultiItemTypeAdapter<T>(context, items) {
    fun updateData(list:ArrayList<T>) {
        if (this.mDatas != null) {
            mDatas.addAll(list)
        }
    }
}