package com.smsthn.thingscounter.SharedData

import android.content.Context
import android.content.SharedPreferences
import java.util.*

private const val CYCLE_SHARED_PREFS = "CycleSharedPrefs"
private const val cycleHour = "CycleHour"
private const val cycleMinute = "CycleMinute"
private const val cycleNextDate = "CycleNextDate"

class CycleSharedData(context: Context){
    private val appSharedPrefs: SharedPreferences
    private val prefsEditor: SharedPreferences.Editor

    init {
        appSharedPrefs = context.getSharedPreferences(
            CYCLE_SHARED_PREFS,
            Context.MODE_PRIVATE
        )
        prefsEditor = appSharedPrefs.edit()
    }


    fun get_hour(): Int {
        return appSharedPrefs.getInt(cycleHour, 0)
    }

    fun set_hour(h: Int) {
        prefsEditor.putInt(cycleHour, h)
        prefsEditor.commit()
    }
    fun get_minute(): Int {
        return appSharedPrefs.getInt(cycleMinute, 0)
    }

    fun set_minute(h: Int) {
        prefsEditor.putInt(cycleMinute, h)
        prefsEditor.commit()
    }



    fun set_cycle_next_date() {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR,get_hour())
        cal.set(Calendar.MINUTE,0)
        cal.set(Calendar.SECOND,0)
        cal.add(Calendar.DATE,1)
        prefsEditor.putLong(cycleNextDate, cal.timeInMillis)
        prefsEditor.commit()
    }
    fun get_cycle_next_date(): Long {
        return appSharedPrefs.getLong(cycleNextDate, Calendar.getInstance().apply {
            set(Calendar.HOUR,get_hour())
            set(Calendar.MINUTE,0)
            set(Calendar.SECOND,0)
        }.timeInMillis)
    }



    fun reset() {
        prefsEditor.clear()
        prefsEditor.commit()

    }
}