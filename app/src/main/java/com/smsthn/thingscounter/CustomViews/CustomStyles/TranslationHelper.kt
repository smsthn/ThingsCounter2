package com.smsthn.thingscounter.CustomViews.CustomStyles

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import com.smsthn.thingscounter.SharedData.LanguageSharedData
import com.smsthn.thingscounter.R
import java.lang.Exception
import java.util.*

@Throws(NullPointerException::class)
fun getStringInLocale(context: Context,identifier:String,locale:String = ""):String?{
    try {
        var l = locale
        if (locale.isBlank()) l = LanguageSharedData(context).get_lang_code()
        val id = context.resources.getIdentifier(identifier, "string", context.packageName)
        var conf = context.resources.configuration
        val fs = R.string::class.java.fields
        conf = Configuration(conf)
        conf.setLocale(Locale(l))
        val localizedContext = context.createConfigurationContext(conf)
        return localizedContext.resources.getString(id)
    } catch (e: NullPointerException) {
        Log.e("Exception get string","String: $identifier loc: $locale"+ e.message)
        throw e
    }

}


@Throws(NullPointerException::class)
fun getStringArrayInLocale(context: Context,identifier:String,locale:String = ""):Array<String>?
{
    var id = -1
    try {
    var l = locale
    if (locale.isBlank()) l = LanguageSharedData(context).get_lang_code()
    id = context.resources.getIdentifier(identifier, "array", context.packageName)
    var conf = context.resources.configuration
    val fs = R.string::class.java.fields
    conf = Configuration(conf)
    conf.setLocale(Locale(l))
    val localizedContext = context.createConfigurationContext(conf)
    return localizedContext.resources.getStringArray(id)
} catch (e: Exception) {
    Log.e("Exception get stringArr","String: $identifier loc: $locale"+ e.message)
    return context.resources.getStringArray(id)
}
}
/*
@Throws(NullPointerException::class)
fun translateString(context: Context,identifier:String,toLocale:String = "en"):String{
    try {
        val l = LanguageSharedData(context).get_lang_code()

    }catch (e: NullPointerException) {
        Log.e("Exception get string","String: $identifier loc: $toLocale"+ e.message)
        throw e
    }
}*/
