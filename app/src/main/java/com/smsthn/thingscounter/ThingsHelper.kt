package com.smsthn.thingscounter

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

fun getDeviceWidth(context: Context):Int{
    val point = Point(0,0)
    (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(point)

    return point.x
}
fun getDeviceHeight(context: Context):Int{
    val point = Point(0,0)
    (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(point)

    return point.y
}
fun getDevicePoint(context: Context):Point{
    val point = Point(0,0)
    (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(point)

    return point
}

