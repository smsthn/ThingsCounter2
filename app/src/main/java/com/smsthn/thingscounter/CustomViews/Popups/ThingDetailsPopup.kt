/*
package com.smsthn.thingscounter.CustomViews.Popups

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.transition.TransitionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.smsthn.thingscounter.CustomViews.CustomStyles.*
import com.smsthn.thingscounter.CustomViews.Dialogs.buildAlertDialog
import com.smsthn.thingscounter.CustomViews.Dialogs.buildYesNoDialog
import com.smsthn.thingscounter.Data.Entities.Thing
import com.smsthn.thingscounter.MainActivity
import com.smsthn.thingscounter.R
import com.smsthn.thingscounter.getDeviceHeight
import com.smsthn.thingscounter.getDeviceWidth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.advanced_thing_details_popup.view.*
import kotlinx.android.synthetic.main.thing_details_popup.view.*
import kotlinx.coroutines.channels.consumesAll

class ThingDetailsPopup(context: Context?,private val updateFunc:(Thing)->Unit,private val deleteFunc:(Thing,Boolean)->Unit,private val resetFunc:(Thing,Boolean)->Unit  ){
    val popup:PopupWindow
    private var thing: Thing?
    private val nameTxt:TextView
    private val catagoryTxt:TextView
    private val count_Goal_Txt:TextView
    private val cycleTxt:TextView
    private val showHistoryBtn:Button
    private val resetHistoryBtn:Button
    private val deleteThingBtn:FloatingActionButton
    private val mainLay:LinearLayout
    private var madeReset:Boolean
    private val editBtn:FloatingActionButton
    private val statusChkBtn:CheckBox
    private val plsbtn:Button
    private val minusbtn:Button
	private val btmnav:BottomNavigationView
	private val prog:ProgressBar
    
    
    private val localCtgs:Array<String>
    private val engCtgs:Array<String>



    init {
        madeReset = false
        thing = null

        localCtgs = getStringArrayInLocale(context!!, "Catagories")!!
        engCtgs = getStringArrayInLocale(
            context,
            "Catagories",
            "en"
        )!!
        
        



        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = inflater.inflate(R.layout.advanced_thing_details_popup,null)
        popup = PopupWindow(view,
            getDeviceWidth(view.context!!)-30,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        popup.isFocusable = true
        popup.isOutsideTouchable = true
        popup.elevation = 20f
        popup.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        nameTxt = view.PopUpThingNameTxt
        catagoryTxt = view.PopupCatagoryTxt
        count_Goal_Txt = view.PopUpThingCount_GoaleTxt
        cycleTxt = view.PopUpThingCycleTxt
        showHistoryBtn = view.ShowHistoryBtn
        resetHistoryBtn = view.ResetHistoryBtn

        mainLay = view.DetailsMainLinLay
        editBtn = view.EditThingBtn.apply { bringToFront() }
        deleteThingBtn = view.DeleteThingBtn.apply { bringToFront() }
        statusChkBtn = view.PopupThingStatusChkBtn
	    
	    
	    plsbtn = view.adv_pls_btn
	    minusbtn = view.adv_minus_btn
	    prog = view.adv_der_prog
	    btmnav = view.adv_btm_nav

        popup.setOnDismissListener {
            dismissPopup()
        }
        */
/*popup.animationStyle = R.style.details_popup_animaton*//*

        addBtnLsners()
    }
    fun openPopup(thing: Thing,view:View? = null){
        this.thing = thing
        madeReset = false
        statusChkBtn.apply {
            setOnCheckedChangeListener { buttonView, isChecked ->  }
            isChecked = thing.enabled
            setOnCheckedChangeListener { buttonView, isChecked ->
               */
/* detailsOrganizer.changeStatus(this@ThingDetailsPopup.thing,isChecked)*//*

            }
        }

        (btmnav.context as MainActivity).showShadow()

        if(this.thing == null)return
        val trans =
            getTransparantColor(deleteThingBtn.context, thing.type)
        val prim = getPrimColor(deleteThingBtn.context, thing.type)
	    val dark = getDarkColor(deleteThingBtn.context,thing.type)
	    val lit = getLightColor(deleteThingBtn.context,thing.type)


        mainLay.background = ColorDrawable(trans)
        showHistoryBtn.backgroundTintList= ColorStateList.valueOf(lit)
        resetHistoryBtn.backgroundTintList= ColorStateList.valueOf(lit)
        deleteThingBtn.backgroundTintList = ColorStateList.valueOf(lit)
        editBtn.backgroundTintList = ColorStateList.valueOf(lit)
	    plsbtn.setBackgroundColor(prim+trans);minusbtn.setBackgroundColor(prim+trans)
	    btmnav.setBackgroundColor(prim+trans);prog.progressTintList = ColorStateList.valueOf(lit)
        val arr = IntArray(2)
        view?.getLocationOnScreen(arr)

        nameTxt.setText(thing.name)
        catagoryTxt.setText(localCtgs[engCtgs.indexOf(thing.catagory)])
        count_Goal_Txt.setText(""+thing.count+" / "+thing.goal)
        cycleTxt.setText(thing.currentCycle.toString())
        //TODO CHECK THIS OUT
        */
/*TransitionManager.beginDelayedTransition((mainLay.context as MainActivity).container);*//*

        popup.showAtLocation(view?:(mainLay.context as MainActivity).container,Gravity.NO_GRAVITY,arr[0],arr[1])

    }
    private fun dismissPopup(){
        (btmnav.context as MainActivity).showShadow()
        thing = null
        nameTxt.setText("")
        catagoryTxt.setText("")
        count_Goal_Txt.setText("")
        cycleTxt.setText("")
    }
    private fun addBtnLsners(){
	    val context = deleteThingBtn.context?:throw NullPointerException("ThingDetailsPopup addBtnLsners context wan null")
        resetHistoryBtn.setOnClickListener {
            buildAlertDialog((context as? Activity?)!!,
                R.string.reset_one_his_diag_msg,
                R.string.reset_history_btn_txt,
                okFunc = {
                    buildYesNoDialog((context as? Activity?)!!,
                        R.string.delete_thing_prompt_delete_prev_diag_msg,
                        R.string.reset_history_btn_txt,
                        yesFunc = { resetFunc(thing!!,true) },
                        noFunc = { resetFunc(thing!!,false) }
                    )
                },
                cancelFunc = {}
            )
        }
        deleteThingBtn.setOnClickListener {
            buildAlertDialog((context as Activity),
                R.string.delete_thing_diag_msg,
                R.string.delete_thing_btn_txt,
                okFunc = {
                    buildYesNoDialog((context as? Activity?)!!,
                        R.string.delete_thing_prompt_delete_prev_diag_msg,
                        R.string.delete_thing_btn_txt,
                        yesFunc = { deleteFunc(thing!!,true) },
                        noFunc = { deleteFunc(thing!!,false) }
                    )
                },
                cancelFunc = {}
            )
        }
        showHistoryBtn.setOnClickListener {
            */
/*detailsOrganizer.openOneHistoryPopup(thing)*//*

        }
        editBtn.setOnClickListener {
            */
/*detailsOrganizer.openEditPopup(thing)*//*


        }
    }

}
*/
