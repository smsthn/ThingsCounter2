package com.smsthn.thingscounter.SharedData

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.os.Build
import java.util.*

private const val MISC_PREFS = "MiscPrefs"
private const val First_Run = "FirstRun"
private const val Allow_Overcounting = "AllowOvercounting"
private const val Pos_Neg_Neu_Allowed = "PosNegNeuAllowed"

class MiscSharedData(context: Context){
    private val appSharedPrefs: SharedPreferences
    private val prefsEditor: SharedPreferences.Editor

    init {
        appSharedPrefs = context.getSharedPreferences(MISC_PREFS, Context.MODE_PRIVATE)
        prefsEditor = appSharedPrefs.edit()
    }

    fun is_first_run(): Boolean {
        return appSharedPrefs.getBoolean(First_Run, true)
    }

    fun set_is_first_run(fr: Boolean) {
        prefsEditor.putBoolean(First_Run,fr)
        prefsEditor.commit()
    }
    fun is_over_counting_allowed(): Boolean {
        return appSharedPrefs.getBoolean(Allow_Overcounting, false)
    }

    fun set_overcounting_allowed(oc: Boolean) {
        prefsEditor.putBoolean(Allow_Overcounting,oc)
        prefsEditor.commit()
    }
    fun is_pos_neg_neu_allowed(): Boolean {
        return appSharedPrefs.getBoolean(Pos_Neg_Neu_Allowed, true)
    }

    fun set_pos_neg_neu_allowed(pnn: Boolean) {
        prefsEditor.putBoolean(Pos_Neg_Neu_Allowed,pnn)
        prefsEditor.commit()
    }
    fun reset() {
        prefsEditor.clear()
        prefsEditor.commit()

    }
}

fun changeLang(context:Context,local: Locale): ContextWrapper {
    val res = context.resources
    val config = res.configuration
    val sysLocale: Locale
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        sysLocale = config.getLocales().get(0)
    } else {
        sysLocale = config.locale
    }
    Locale.setDefault(local)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        config.setLocale(local)
    } else {
        config.locale = local
    }
    val context = context.createConfigurationContext(config)
    return ContextWrapper(context)
}

fun doOnFirstTimeRun(){

}