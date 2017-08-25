package com.genieapps.writeshort.helpers

import java.text.SimpleDateFormat
import java.util.*


/**
 * Created on 8/23/2017.
 */
class DateHelper {
    companion object {
        fun getDate(milliSeconds:Long, dateFormat:String): String{
            val formatter = SimpleDateFormat(dateFormat)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = milliSeconds
            return formatter.format(calendar.time)
        }
    }
}