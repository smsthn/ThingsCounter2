package com.smsthn.thingscounter.CustomViews.Dialogs

import android.app.Activity
import android.app.AlertDialog
import com.smsthn.thingscounter.R

fun buildAlertDialog(activity: Activity,msgRes: Int,titleRes:Int,okFunc:()->Unit,cancelFunc:()->Unit){
    val builder: AlertDialog.Builder? = activity.let {
        AlertDialog.Builder(it,R.style.Base_ThemeOverlay_MaterialComponents_Dialog)
    }
    var canceled = true

    builder?.apply {
        setMessage(msgRes);setTitle(titleRes)
        setPositiveButton(R.string.ok) { dialog, which -> okFunc.invoke();canceled = false}
        setNegativeButton(R.string.cancel) { dialog, which ->cancelFunc.invoke()}
        setOnDismissListener {if(canceled)  cancelFunc.invoke() }
        create()
        show()
    }
}

fun buildYesNoDialog(activity: Activity,msgRes: Int,titleRes:Int,yesFunc:()->Unit,noFunc:()->Unit,
                     dismissFunc:()->Unit = {}){
    val builder: AlertDialog.Builder? = activity.let {
        AlertDialog.Builder(it,R.style.Base_Theme_MaterialComponents_Light_Dialog)
    }
    //var canceled = true

    builder?.apply {
        setMessage(msgRes);setTitle(titleRes)
        setPositiveButton(R.string.yes) { dialog, which -> yesFunc.invoke()}
        setNegativeButton(R.string.no) { dialog, which ->noFunc.invoke()}
        setOnDismissListener { dismissFunc.invoke() }
        create()
        show()
    }
}