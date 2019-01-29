/*
package com.smsthn.thingscounter.CustomViews.CustomStyles

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import com.smsthn.thingscounter.Thing
import kotlinx.android.synthetic.main.disabled_recycle_one_thing_view.view.*
import kotlinx.android.synthetic.main.recycle_single_thing_view.view.*
import java.lang.Exception


class EnabledThingColorValues(
    val plusMinusBtns: Int,
    val detailsBtn: Int,
    val progressBar: Int,
    val middlePart: Int
)

class DisabledThingColorValues(val bg: Int)

class Status_ToolBarColorValues(val statusbar: Int, val toolbar: Int)

interface ThingPainter {
    fun paintThing(view: View, thing: Thing)
    fun colorIsDark(color: Int): Boolean

    fun getTransparantColor(type: String):Int

    fun getPrimaryColor(type: String): Int

    fun getLightColor(type: String): Int

    fun getDarkColor(type: String): Int
}

class PosNegNeuPainter private constructor() : ThingPainter {
    override fun getTransparantColor(type: String): Int {
        return when(type){"Positive"->UiOrganizer.res().TRANS_GREEN;"Negative"->UiOrganizer.res().TRANS_RED;"Neutral"->UiOrganizer.res().TRANS_GRAY;else ->UiOrganizer.res().TRANS_GRAY}
    }

    override fun colorIsDark(color: Int): Boolean {
        return ColorUtils.calculateLuminance(color) < 0.3
    }

    override fun getPrimaryColor(type: String): Int {
       return when(type){"Positive"->UiOrganizer.res().PR_GREEN;"Negative"->UiOrganizer.res().PR_RED;"Neutral"->UiOrganizer.res().PR_GRAY;else ->UiOrganizer.res().PR_GRAY}
    }

    override fun getLightColor(type: String): Int {
        return when(type){"Positive"->UiOrganizer.res().PR_GREEN_LT;"Negative"->UiOrganizer.res().PR_RED_LT;"Neutral"->UiOrganizer.res().PR_GRAY_LT;else ->UiOrganizer.res().PR_GRAY_LT}
    }

    override fun getDarkColor(type: String): Int {
        return when(type){"Positive"->UiOrganizer.res().PR_GREEN_DK;"Negative"->UiOrganizer.res().PR_RED_DK;"Neutral"->UiOrganizer.res().PR_GRAY_DK;else -> UiOrganizer.res().PR_GRAY_DK}
    }

    companion object {

        private var INSTANCE: PosNegNeuPainter? = null
        fun getPainter(): PosNegNeuPainter? {
            NotPosNegNeuPainter.destroyPainter()
            if (INSTANCE == null) INSTANCE = PosNegNeuPainter()
            return INSTANCE
        }

        fun destroyPainter() {
            INSTANCE = null
        }
    }

    override fun paintThing(view: View, thing: Thing) {
        if (thing.enabled) paintEnabled(view, thing)
        else paintDisabled(view, thing)
    }

    private fun paintEnabled(view: View, thing: Thing) {
        try {
            when (thing.type) {
                "Positive" -> {
                    //thingTransLayout.background = (context as MainActivity).getDrawable(R.drawable.green_down_grad)
                    view.ThingMinusBtn.setBackgroundColor(UiOrganizer.getRes().PR_GREEN_DK)
                    view.ThingPlusBtn.setBackgroundColor(UiOrganizer.getRes().PR_GREEN_DK)
                    view.MoreDetailsBtn.setBackgroundColor(UiOrganizer.getRes().PR_GREEN_LT)
                    view.ThingProgressBar.progressDrawable.setTint(UiOrganizer.getRes().PR_GREEN_LT)
                }
                "Negative" -> {
                    //thingTransLayout.background = (context as MainActivity).getDrawable(R.drawable.red_down_grad)
                    view.ThingMinusBtn.background = ColorDrawable(UiOrganizer.getRes().PR_RED_DK)
                    view.ThingPlusBtn.setBackgroundColor(UiOrganizer.getRes().PR_RED_DK)
                    view.MoreDetailsBtn.setBackgroundColor(UiOrganizer.getRes().PR_RED_LT)
                    view.ThingProgressBar.progressDrawable.setTint(UiOrganizer.getRes().PR_RED_LT)
                }
                "Neutral" -> {
                    //thingTransLayout.background = (context as MainActivity).getDrawable(R.drawable.gray_down_grad)
                    view.ThingMinusBtn.setBackgroundColor(UiOrganizer.getRes().PR_GRAY_DK)
                    view.ThingPlusBtn.setBackgroundColor(UiOrganizer.getRes().PR_GRAY_DK)
                    view.MoreDetailsBtn.setBackgroundColor(UiOrganizer.getRes().PR_GRAY)
                    view.ThingProgressBar.progressDrawable.setTint(UiOrganizer.getRes().PR_GRAY_DK)
                }
            }
        } catch (e: Exception) {
            Log.e("Failed Whem paintEnabled for ${thing.name} : ", e.message)
            throw e
        }
    }

    private fun paintDisabled(view: View, thing: Thing) {
        try {
            when (thing.type) {
                "Positive" -> {
                    view.DisabledThingMainLayout.setBackgroundColor(UiOrganizer.getRes().TRANS_GREEN)
                }
                "Negative" -> {
                    view.DisabledThingMainLayout.setBackgroundColor(UiOrganizer.getRes().TRANS_RED)
                }
                "Neutral" -> {
                    view.DisabledThingMainLayout.setBackgroundColor(UiOrganizer.getRes().TRANS_GRAY)
                }
            }
        } catch (e: Exception) {
            Log.e("Failed Whem paintDisabled for ${thing.name} : ", e.message)
            throw e
        }
    }
}


class NotPosNegNeuPainter private constructor() : ThingPainter {


    companion object {
        private var INSTANCE: NotPosNegNeuPainter? = null
        fun getPainter(): NotPosNegNeuPainter? {
            PosNegNeuPainter.destroyPainter()
            if (INSTANCE == null) INSTANCE = NotPosNegNeuPainter()
            return INSTANCE
        }

        fun destroyPainter() {
            INSTANCE = null
        }
    }


    val yellowEnabled = EnabledThingColorValues(
        Color.parseColor("#fdd835"),
        Color.parseColor("#ffff6b"),
        Color.parseColor("#ffff6b"),
        Color.parseColor("#ffffff")
    )
    val yellowDisabled = DisabledThingColorValues(Color.parseColor("#20fdd835"))
    val orangeEnabled = EnabledThingColorValues(
        Color.parseColor("#ffa726"),
        Color.parseColor("#ffd95b"),
        Color.parseColor("#ffd95b"),
        Color.parseColor("#ffffff")
    )
    val orangeDisabled = DisabledThingColorValues(Color.parseColor("#20ffa726"))
    val brownEnabled = EnabledThingColorValues(
        Color.parseColor("#795548"),
        Color.parseColor("#a98274"),
        Color.parseColor("#a98274"),
        Color.parseColor("#ffffff")
    )
    val brownDisabled = DisabledThingColorValues(Color.parseColor("#20795548"))
    val darkBlueEnabled = EnabledThingColorValues(
        Color.parseColor("#607d8b"),
        Color.parseColor("#8eacbb"),
        Color.parseColor("#8eacbb"),
        Color.parseColor("#ffffff")
    )
    val darkBlueDisabled = DisabledThingColorValues(Color.parseColor("#20607d8b"))
    val blueEnabled = EnabledThingColorValues(
        Color.parseColor("#29b6f6"),
        Color.parseColor("#73e8ff"),
        Color.parseColor("#73e8ff"),
        Color.parseColor("#ffffff")
    )
    val blueDisabled = DisabledThingColorValues(Color.parseColor("#2029b6f6"))
    val purpleEnabled = EnabledThingColorValues(
        Color.parseColor("#ab47bc"),
        Color.parseColor("#df78ef"),
        Color.parseColor("#df78ef"),
        Color.parseColor("#ffffff")
    )
    val purpleDisabled = DisabledThingColorValues(Color.parseColor("#20ab47bc"))
    val pinkEnabled = EnabledThingColorValues(
        Color.parseColor("#f06292"),
        Color.parseColor("#ff94c2"),
        Color.parseColor("#ff94c2"),
        Color.parseColor("#ffffff")
    )
    val pinkDisabled = DisabledThingColorValues(Color.parseColor("#20f06292"))
    val greenEnabled = EnabledThingColorValues(
        UiOrganizer.getRes().PR_GREEN,
        UiOrganizer.getRes().PR_GREEN_LT,
        UiOrganizer.getRes().PR_GREEN_LT,
        Color.parseColor("#ffffff")
    )
    val greenDisabled = DisabledThingColorValues(UiOrganizer.getRes().TRANS_GREEN)
    val redEnabled = EnabledThingColorValues(
        UiOrganizer.getRes().PR_RED,
        UiOrganizer.getRes().PR_RED_LT,
        UiOrganizer.getRes().PR_RED_LT,
        Color.parseColor("#ffffff")
    )
    val redDisabled = DisabledThingColorValues(UiOrganizer.getRes().TRANS_RED)
    val grayEnabled = EnabledThingColorValues(
        UiOrganizer.getRes().PR_GRAY,
        UiOrganizer.getRes().PR_GRAY_LT,
        UiOrganizer.getRes().PR_GRAY_LT,
        Color.parseColor("#ffffff")
    )
    val grayDisabled = DisabledThingColorValues(UiOrganizer.getRes().TRANS_GRAY)

    //TODO:

    val blackEnabled = EnabledThingColorValues(
        Color.parseColor("#263238"),
        Color.parseColor("#4f5b62"),
        Color.parseColor("#4f5b62"),
        Color.parseColor("#ffffff")
    )
    val black = DisabledThingColorValues(Color.parseColor("#22263238"))
    val darkOrangeEnabled = EnabledThingColorValues(
        Color.parseColor("#bf360c"),
        Color.parseColor("#f9683a"),
        Color.parseColor("#f9683a"),
        Color.parseColor("#ffffff")
    )
    val darkOrange = DisabledThingColorValues(Color.parseColor("#22bf360c"))
    val militaryGreenEnabled = EnabledThingColorValues(
        Color.parseColor("#827717"),
        Color.parseColor("#b4a647"),
        Color.parseColor("#b4a647"),
        Color.parseColor("#ffffff")
    )
    val militaryGreen = DisabledThingColorValues(Color.parseColor("#22827717"))
    val darkGreenEnabled = EnabledThingColorValues(
        Color.parseColor("#1b5e20"),
        Color.parseColor("#4c8c4a"),
        Color.parseColor("#4c8c4a"),
        Color.parseColor("#ffffff")
    )
    val darkGreen = DisabledThingColorValues(Color.parseColor("#20003300"))
    val darkerblueEnabled = EnabledThingColorValues(
        Color.parseColor("#0d47a1"),
        Color.parseColor("#5472d3"),
        Color.parseColor("#5472d3"),
        Color.parseColor("#ffffff")
    )
    val darkerBlue = DisabledThingColorValues(Color.parseColor("#220d47a1"))
    val morasakiEnabled = EnabledThingColorValues(
        Color.parseColor("#4a148c"),
        Color.parseColor("#7c43bd"),
        Color.parseColor("#7c43bd"),
        Color.parseColor("#ffffff")
    )
    val morasaki = DisabledThingColorValues(Color.parseColor("#114a148c"))
    val darkRedEnabled = EnabledThingColorValues(
        Color.parseColor("#b71c1c"),
        Color.parseColor("#f05545"),
        Color.parseColor("#f05545"),
        Color.parseColor("#ffffff")
    )
    val darkRed = DisabledThingColorValues(Color.parseColor("#26b71c1c"))


    override fun paintThing(view: View, thing: Thing) {
        if (thing.enabled) paintEnabled(view, thing)
        else paintDisabled(view, thing)

    }

    private fun paintEnabled(view: View, thing: Thing) {
        val clrs = getEnabledColorValues(thing.type)
        changeTextColor(view.ThingMainLayout, thing, clrs.detailsBtn)

        view.ThingMinusBtn.setBackgroundColor(clrs.plusMinusBtns)
        view.ThingPlusBtn.setBackgroundColor(clrs.plusMinusBtns)
        view.MoreDetailsBtn.setBackgroundColor(clrs.detailsBtn)
        view.ThingProgressBar.progressDrawable.setTint(clrs.progressBar)
    }

    private fun paintDisabled(view: View, thing: Thing) {
        val clrs = getDisabledColorValues(thing.type)
        view.DisabledThingMainLayout.setBackgroundColor(clrs.bg)
    }

    private fun getEnabledColorValues(type: String): EnabledThingColorValues {
        return when (type) {
            "Yellow" -> yellowEnabled
            "Orange" -> orangeEnabled
            "Brown" -> brownEnabled
            "DarkBlue" -> darkBlueEnabled
            "Blue" -> blueEnabled
            "Purple" -> purpleEnabled
            "Pink" -> pinkEnabled
            "Green" -> greenEnabled
            "Red" -> redEnabled
            "Gray" -> grayEnabled
            //DARKER
            "Black" -> blackEnabled
            "DarkOrange" -> darkOrangeEnabled
            "MilitaryGreen" -> militaryGreenEnabled
            "DarkGreen" -> darkGreenEnabled
            "DarkerBlue" -> darkerblueEnabled
            "Morasaki" -> morasakiEnabled
            "DarkRed" -> darkRedEnabled
            else -> grayEnabled
        }
    }

    private fun getDisabledColorValues(type: String): DisabledThingColorValues {
        return when (type) {
            "Yellow" -> yellowDisabled
            "Orange" -> orangeDisabled
            "Brown" -> brownDisabled
            "DarkBlue" -> darkBlueDisabled
            "Blue" -> blueDisabled
            "Purple" -> purpleDisabled
            "Pink" -> pinkDisabled
            "Green" -> greenDisabled
            "Red" -> redDisabled
            "Gray" -> grayDisabled
            //DARKER
            "Black" -> black
            "DarkOrange" -> darkOrange
            "MilitaryGreen" -> militaryGreen
            "DarkGreen" -> darkGreen
            "DarkerBlue" -> darkerBlue
            "Morasaki" -> morasaki
            "DarkRed" -> darkRed
            else -> grayDisabled
        }
    }

    override fun colorIsDark(color: Int): Boolean {
        return ColorUtils.calculateLuminance(color) < 0.3
    }

    private fun changeTextColor(view: View, thing: Thing, color: Int) {
        if (colorIsDark(color)) {

            doChildrenRecursive(view, { if (it is TextView) it.setTextColor(Color.WHITE) })

        } else {
            doChildrenRecursive(view, { if (it is TextView) it.setTextColor(Color.BLACK) })
        }
    }

    private fun doChildrenRecursive(view: View, func: (View) -> Unit) {
        try {
            func.invoke(view)
            if (view is ViewGroup) {
                if (view.childCount != 0) {
                    for (i in 0..view.childCount) doChildrenRecursive(view.getChildAt(i), func)
                }
            }

        } catch (e: Exception) {

        }
    }

    override fun getPrimaryColor(type: String): Int {
        return getEnabledColorValues(type).plusMinusBtns
    }

    override fun getLightColor(type: String): Int {
        return getEnabledColorValues(type).detailsBtn
    }

    override fun getDarkColor(type: String): Int {
        try {
            return NotPosNegNeuStatus_ToolBarPainter.getPainter()!!.getColorValues(type).statusbar
        } catch (e: NullPointerException) {
            Log.e("getDarkColor of $type : ", "NotPosNegNeuStatus_ToolBarPainter.getPainter() Was Null")
            throw e
        }
    }
    override fun getTransparantColor(type: String): Int {
        return getDisabledColorValues(type).bg
    }
}

class NotPosNegNeuStatus_ToolBarPainter private constructor() {
    companion object {
        private var INSTANCE: NotPosNegNeuStatus_ToolBarPainter? = null
        fun getPainter(): NotPosNegNeuStatus_ToolBarPainter? {
            if (INSTANCE == null) INSTANCE = NotPosNegNeuStatus_ToolBarPainter()
            return INSTANCE
        }

        fun destroyPainter() {
            INSTANCE = null
        }
    }


    val yellow = Status_ToolBarColorValues(
        Color.parseColor("#c6a700"),
        Color.parseColor("#fdd835")
    )
    val orange = Status_ToolBarColorValues(
        Color.parseColor("#c77800"),
        Color.parseColor("#ffa726")
    )
    val brown = Status_ToolBarColorValues(
        Color.parseColor("#4b2c20"),
        Color.parseColor("#795548")

    )
    val darkBlue = Status_ToolBarColorValues(
        Color.parseColor("#34515e"),
        Color.parseColor("#607d8b")
    )
    val blue = Status_ToolBarColorValues(
        Color.parseColor("#0086c3"),
        Color.parseColor("#29b6f6")
    )
    val purple = Status_ToolBarColorValues(
        Color.parseColor("#790e8b"),
        Color.parseColor("#ab47bc")
    )
    val pink = Status_ToolBarColorValues(
        Color.parseColor("#ba2d65"),
        Color.parseColor("#f06292")
    )
    val green = Status_ToolBarColorValues(
        UiOrganizer.getRes().PR_GREEN_DK,
        UiOrganizer.getRes().PR_GREEN
    )
    val red = Status_ToolBarColorValues(
        UiOrganizer.getRes().PR_RED_DK,
        UiOrganizer.getRes().PR_RED
    )
    val gray = Status_ToolBarColorValues(
        UiOrganizer.getRes().PR_GRAY_DK,
        UiOrganizer.getRes().PR_GRAY
    )


    //TODO ANOTHER CLASS
    val black = Status_ToolBarColorValues(
        Color.parseColor("#000a12"),
        Color.parseColor("#263238")
    )


    val darkOrange = Status_ToolBarColorValues(
        Color.parseColor("#870000"),
        Color.parseColor("#bf360c")
    )
    val militaryGreen = Status_ToolBarColorValues(
        Color.parseColor("#524c00"),
        Color.parseColor("#827717")
    )


    val darkGreen = Status_ToolBarColorValues(
        Color.parseColor("#003300"),
        Color.parseColor("#1b5e20")
    )

    val darkerBlue = Status_ToolBarColorValues(
        Color.parseColor("#002f6c"),
        Color.parseColor("#01579b")
    )


    val morasaki = Status_ToolBarColorValues(
        Color.parseColor("#12005e"),
        Color.parseColor("#4a148c")
    )


    val darkRed = Status_ToolBarColorValues(
        Color.parseColor("#7f0000"),
        Color.parseColor("#b71c1c")
    )

    fun paintTops(type: String) {

    }

    fun getColorValues(type: String): Status_ToolBarColorValues {
        return when (type) {
            "Yellow" -> yellow
            "Orange" -> orange
            "Brown" -> brown
            "DarkBlue" -> darkBlue
            "Blue" -> blue
            "Purple" -> purple
            "Pink" -> pink
            "Green" -> green
            "Red" -> red
            "Gray" -> gray
            //DARKER
            "Black" -> black
            "DarkOrange" -> darkOrange
            "MilitaryGreen" -> militaryGreen
            "DarkGreen" -> darkGreen
            "DarkerBlue" -> darkerBlue
            "Morasaki" -> morasaki
            "DarkRed" -> darkRed
            else -> gray
        }
    }

}
*/
