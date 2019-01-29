package com.smsthn.thingscounter.SharedData

import android.content.Context
import android.content.SharedPreferences


private const val LANG_SHARED_PREFS = "LanguageSharedPreferences"
private const val LANG_CODE = "LanguageCode"
class LanguageSharedData(context: Context){
    private val appSharedPrefs: SharedPreferences
    private val prefsEditor: SharedPreferences.Editor

    init {
        appSharedPrefs = context.getSharedPreferences(LANG_SHARED_PREFS, Context.MODE_PRIVATE)
        prefsEditor = appSharedPrefs.edit()
    }


    fun get_lang_code(): String {
        return appSharedPrefs.getString(LANG_CODE, "en")
    }

    fun set_lang_code(lc: String) {
        prefsEditor.putString(LANG_CODE,lc)
        prefsEditor.commit()
    }




    fun reset() {
        prefsEditor.clear()
        prefsEditor.commit()

    }
}