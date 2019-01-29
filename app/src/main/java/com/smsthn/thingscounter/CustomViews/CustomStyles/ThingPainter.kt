package com.smsthn.thingscounter.CustomViews.CustomStyles

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.smsthn.thingscounter.Data.Entities.Thing
import com.smsthn.thingscounter.R

fun getPrimColor(context: Context,color:String):Int{
    return when (color) {
        "Yellow" -> R.color.yellow_pr
        "Orange" -> R.color.orange_pr
        "Brown" -> R.color.brown_pr
        "DarkBlue" -> R.color.darkBlue_pr
        "Blue" -> R.color.blue_pr
        "Purple" -> R.color.purple_pr
        "Pink" -> R.color.pink_pr
        "Green" -> R.color.green_pr
        "Positive" -> R.color.green_pr
        "Red" -> R.color.red_pr
        "Negative" -> R.color.red_pr
        "Gray" -> R.color.gray_pr
        "Neutral" -> R.color.gray_pr
        //DARKER
        "Black" -> R.color.black_pr
        "DarkOrange" -> R.color.darkOrange_pr
        "MilitaryGreen" -> R.color.militaryGreen_pr
        "DarkGreen" -> R.color.darkGreen_pr
        "DarkerBlue" -> R.color.darkerblue_pr
        "Morasaki" -> R.color.morasaki_pr
        "DarkRed" -> R.color.darkRed_pr
        else -> R.color.gray_pr
    }.run {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                (context).resources.getColor(this,context.theme)
            } else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                ContextCompat.getColor(context,this)
            } else {
                return Color.GRAY
            }
    }
}
fun getLightColor(context: Context,color: String):Int{
    return when (color) {
        "Yellow" -> R.color.yellow_lt
        "Orange" -> R.color.orange_lt
        "Brown" -> R.color.brown_lt
        "DarkBlue" -> R.color.darkBlue_lt
        "Blue" -> R.color.blue_lt
        "Purple" -> R.color.purple_lt
        "Pink" -> R.color.pink_lt
        "Green" -> R.color.green_lt2
        "Positive" -> R.color.green_lt2
        "Red" -> R.color.red_lt2
        "Negative" -> R.color.red_lt2
        "Gray" -> R.color.gray_lt2
        "Neutral" -> R.color.gray_lt2
        //DARKER
        "Black" -> R.color.black_lt
        "DarkOrange" -> R.color.darkOrange_lt
        "MilitaryGreen" -> R.color.militaryGreen_lt
        "DarkGreen" -> R.color.darkGreen_lt
        "DarkerBlue" -> R.color.darkerblue_lt
        "Morasaki" -> R.color.morasaki_lt
        "DarkRed" -> R.color.darkRed_lt
        else -> R.color.gray_lt2
    }.run {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (context).resources.getColor(this,context.theme)
        } else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            ContextCompat.getColor(context,this)
        }else {
            return Color.LTGRAY
        }
    }
}
fun getDarkColor(context: Context,color: String):Int{
    return when (color) {
        "Yellow" -> R.color.yellow_dk
        "Orange" -> R.color.orange_dk
        "Brown" -> R.color.brown_dk
        "DarkBlue" -> R.color.darkBlue_dk
        "Blue" -> R.color.blue_dk
        "Purple" -> R.color.purple_dk
        "Pink" -> R.color.pink_dk
        "Green" -> R.color.green_dk
        "Positive" -> R.color.green_dk
        "Red" -> R.color.red_dk
        "Negative" -> R.color.red_dk
        "Gray" -> R.color.gray_dk
        "Neutral" -> R.color.gray_dk
        //DARKER
        "Black" -> R.color.black_dk
        "DarkOrange" -> R.color.darkOrange_dk
        "MilitaryGreen" -> R.color.militaryGreen_dk
        "DarkGreen" -> R.color.darkGreen_dk
        "DarkerBlue" -> R.color.darkerBlue_dk
        "Morasaki" -> R.color.morasaki_dk
        "DarkRed" -> R.color.darkRed_dk
        else -> R.color.gray_dk
    }.run {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (context).resources.getColor(this,context.theme)
        } else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            ContextCompat.getColor(context,this)
        }else {
            return Color.DKGRAY
        }
    }
}

fun getTransparantColor(context: Context,color:String):Int{
    return when (color) {
        "Yellow" -> R.color.yellow_trans
        "Orange" -> R.color.orange_trans
        "Brown" -> R.color.brown_trans
        "DarkBlue" -> R.color.darkBlue_trans
        "Blue" -> R.color.blue_trans
        "Purple" -> R.color.purple_trans
        "Pink" -> R.color.pink_trans
        "Green" -> R.color.green_trans
        "Positive" -> R.color.green_trans
        "Red" -> R.color.red_trans
        "Negative" -> R.color.red_trans
        "Gray" -> R.color.gray_trans
        "Neutral" -> R.color.gray_trans
        //DARKER
        "Black" -> R.color.black_trans
        "DarkOrange" -> R.color.darkOrange_trans
        "MilitaryGreen" -> R.color.militaryGreen_trans
        "DarkGreen" -> R.color.darkGreen_trans
        "DarkerBlue" -> R.color.darkerBlue_trans
        "Morasaki" -> R.color.morasaki_trans
        "DarkRed" -> R.color.darkRed_trans
        else -> R.color.gray_trans
    }.run {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (context).resources.getColor(this,context.theme)
        } else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            ContextCompat.getColor(context,this)
        }else {
            return Color.DKGRAY
        }
    }
}


fun isColorDark(intColor:Int):Boolean{
    return ColorUtils.calculateLuminance(intColor) < 0.3
}


fun changeTextColor(view: View, thing: Thing, color: Int) {
    if (isColorDark(color)) {

        doChildrenRecursive(
            view
        ) { if (it is TextView) it.setTextColor(Color.WHITE) }

    } else {
        doChildrenRecursive(
            view
        ) { if (it is TextView) it.setTextColor(Color.BLACK) }
    }
}

fun doChildrenRecursive(view: View, func: (View) -> Unit) {
    try {
        func.invoke(view)
        if (view is ViewGroup) {
            if (view.childCount != 0) {
                for (i in 0..view.childCount) doChildrenRecursive(
                    view.getChildAt(
                        i
                    ), func
                )
            }
        }

    } catch (e: Exception) {

    }
}