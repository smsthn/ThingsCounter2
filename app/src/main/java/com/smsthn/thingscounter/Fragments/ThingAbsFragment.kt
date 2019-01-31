package com.smsthn.thingscounter.Fragments

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.smsthn.thingscounter.CustomViews.CustomStyles.*
import com.smsthn.thingscounter.Data.Entities.Thing
import com.smsthn.thingscounter.R
import kotlinx.android.synthetic.main.ms_btm_navbar.*
import kotlinx.android.synthetic.main.ms_btm_navbar.view.*
import kotlinx.android.synthetic.main.thing_details_popup.view.*

abstract class ThingAbsFragment : Fragment() {

}

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {
    private var thing: Thing? = null
    private lateinit var nameTxt: TextView
    private lateinit var catagoryTxt: TextView
    private lateinit var count_Goal_Txt: TextView
    private lateinit var cycleTxt: TextView
    /*private lateinit var showHistoryBtn: Button
    private lateinit var resetHistoryBtn: Button*/
    private lateinit var deleteThingBtn: Button
    private lateinit var mainLay: View
    private var madeReset: Boolean = false
    private lateinit var editBtn: Button
    private lateinit var statusChkBtn: CheckBox


    private lateinit var localCtgs: Array<String>
    private lateinit var engCtgs: Array<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.ms_btm_navbar, container, false)

        if (thing == null) this.dismiss()

        nameTxt = view.PopUpThingNameTxt
        catagoryTxt = view.PopupCatagoryTxt
        count_Goal_Txt = view.PopUpThingCount_GoaleTxt
        cycleTxt = view.PopUpThingCycleTxt
       /* showHistoryBtn = view.ShowHistoryBtn
        resetHistoryBtn = view.ResetHistoryBtn*/

        mainLay = view.navigation_view_layout
        editBtn = view.EditThingBtn.apply { bringToFront() }
        deleteThingBtn = view.DeleteThingBtn.apply { bringToFront() }
        statusChkBtn = view.PopupThingStatusChkBtn



        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (thing == null) {
            dismiss()
            return
        }


        madeReset = false
        statusChkBtn.apply {
            setOnCheckedChangeListener { buttonView, isChecked -> }
            isChecked = thing!!.enabled
            setOnCheckedChangeListener { buttonView, isChecked ->
                /* detailsOrganizer.changeStatus(this@ThingDetailsPopup.thing,isChecked)*/
            }
        }


        val trans =
            getTransparantColor(deleteThingBtn.context, thing!!.type)
        val prim = getPrimColor(deleteThingBtn.context, thing!!.type)
        val dark = getDarkColor(deleteThingBtn.context, thing!!.type)
        val lit = getLightColor(deleteThingBtn.context, thing!!.type)

        mainLay.backgroundTintList = ColorStateList.valueOf(trans)/*
        showHistoryBtn.backgroundTintList = ColorStateList.valueOf(lit)
        resetHistoryBtn.backgroundTintList = ColorStateList.valueOf(lit)*/
        deleteThingBtn.backgroundTintList = ColorStateList.valueOf(trans)
        editBtn.backgroundTintList = ColorStateList.valueOf(trans)

        val arr = IntArray(2)
        view?.getLocationOnScreen(arr)

        nameTxt.setText(thing!!.name)
        catagoryTxt.setText(localCtgs[engCtgs.indexOf(thing!!.catagory)])
        count_Goal_Txt.setText("" + thing!!.count + " / " + thing!!.goal)
        cycleTxt.setText(thing!!.currentCycle.toString())
        //TODO CHECK THIS OUT
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        localCtgs = getStringArrayInLocale(context!!, "Catagories")!!
        engCtgs = getStringArrayInLocale(
            context,
            "Catagories",
            "en"
        )!!
    }

    companion object {

        @JvmStatic
        fun newInstance(thing: Thing) =
            BottomNavigationDrawerFragment().apply {
                this.thing = thing
            }
    }
}