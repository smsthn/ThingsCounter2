package com.smsthn.thingscounter.SharedData

import android.content.Context
import android.content.SharedPreferences


private const val APP_SHARED_PREFS = "ReminderPreferences"



private const val reminderStatus = "reminderStatus"
private const val hour = "hour"
private const val min = "min"
private const val allowNot = "allowNotifications"
private const val isOngoing = "isOngoing"
private const val dataType = "dataType"

class NotificationSharedData(context: Context){
    private val appSharedPrefs: SharedPreferences
    private val prefsEditor: SharedPreferences.Editor

    init {
        appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS,Context.MODE_PRIVATE)
        prefsEditor = appSharedPrefs.edit()
    }
    fun getReminderStatus():Boolean{
        return appSharedPrefs.getBoolean(reminderStatus,false)
    }

    fun setReminderStatus(status:Boolean){
        prefsEditor.putBoolean(reminderStatus,status)
        prefsEditor.commit()
    }

    fun get_hour(): Int {
        return appSharedPrefs.getInt(hour, 20)
    }

    fun set_hour(h: Int) {
        prefsEditor.putInt(hour, h)
        prefsEditor.commit()
    }

    // Settings Page Reminder Time (Minutes)

    fun get_min(): Int {
        return appSharedPrefs.getInt(min, 0)
    }
    fun set_min(m: Int) {
        prefsEditor.putInt(min, m)
        prefsEditor.commit()
    }

    fun set_allow_nots(m: Boolean) {
        prefsEditor.putBoolean(allowNot, m)
        prefsEditor.commit()
    }
    fun get_allow_nots(): Boolean {
        return appSharedPrefs.getBoolean(allowNot, false)
    }
    fun set_ongoing(ongoing: Boolean) {
        prefsEditor.putBoolean(isOngoing, ongoing)
        prefsEditor.commit()
    }
    fun get_ongoing(): Boolean {
        return appSharedPrefs.getBoolean(isOngoing, true)
    }
    // 1- completed / total; 2- counts / goals
    fun get_data_Type(): Int {
        return appSharedPrefs.getInt(dataType, 0)
    }
    fun set_data_Type(type: Int) {
        prefsEditor.putInt(dataType, type)
        prefsEditor.commit()
    }


    fun reset() {
        prefsEditor.clear()
        prefsEditor.commit()

    }
}