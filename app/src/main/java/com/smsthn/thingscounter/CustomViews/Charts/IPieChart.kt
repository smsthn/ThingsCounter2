package com.smsthn.thingscounter.CustomViews.Charts

import android.graphics.Color
import com.razerdp.widget.animatedpieview.AnimatedPieView
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig
import com.razerdp.widget.animatedpieview.data.SimplePieInfo
import com.smsthn.thingscounter.MainActivity
import com.smsthn.thingscounter.R
import java.lang.Exception


fun makeTwoElemPie(anim:AnimatedPieView,intData:Array<Int>,color:Int){
    makePie(
        anim,
        arrayOf(intData[0], intData[1]),
        arrayOf("Counts", "Goals"),
        arrayOf(color, Color.BLACK),
        true,
        15
    )
}
fun makeThreeElemPie(anim:AnimatedPieView,intData:Array<Int>){
    val mn = anim.context as MainActivity
    makePie(
        anim, arrayOf(intData[0], intData[1], intData[2]),
        arrayOf(
            mn.getString(R.string.positive), mn.getString(R.string.negative),
            mn.getString(R.string.neutral)
        ),
        arrayOf(
            Color.GREEN, Color.RED,
            Color.LTGRAY
        )
    )
}
fun makePie(anim:AnimatedPieView, intData: Array<Int>, names:Array<String>, colors:Array<Int>,ring:Boolean = false,ringWidth:Int = 10){
    if(intData.size != names.size || intData.size  != colors.size)throw Exception("Array Lengths Dont Match")
    val vals = intData.map { it.toDouble() }.toDoubleArray()
    val cnfg = AnimatedPieViewConfig()
    cnfg.startAngle(-90.0f).apply {
        for(i in 0..vals.size-1){
            addData(SimplePieInfo(vals[i],colors[i],names[i]))
        }
    }
        .duration(1000).textSize(28f)
        .strokeWidth(ringWidth).drawText(true).strokeMode(ring)
    anim.applyConfig(cnfg)
    anim.start()
}