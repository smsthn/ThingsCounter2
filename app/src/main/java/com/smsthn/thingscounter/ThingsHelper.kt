package com.smsthn.thingscounter

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.bottomappbar.BottomAppBar
import java.lang.Float.max
import java.lang.Float.min

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


