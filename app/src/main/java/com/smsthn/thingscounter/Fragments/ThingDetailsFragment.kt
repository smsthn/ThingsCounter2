package com.smsthn.thingscounter.Fragments

import android.content.Context
import android.content.res.ColorStateList
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.ChipGroup
import com.smsthn.thingscounter.CustomViews.CustomStyles.*
import com.smsthn.thingscounter.CustomViews.RecycleViewAdapters.OneHistoryRecyclerAdapter
import com.smsthn.thingscounter.Data.Entities.OneHistory
import com.smsthn.thingscounter.Data.Entities.Thing
import com.smsthn.thingscounter.Data.Entities.cloneThing
import com.smsthn.thingscounter.Data.ThingsDb
import com.smsthn.thingscounter.MainActivity
import com.smsthn.thingscounter.R
import kotlinx.android.synthetic.main.new_details_popup.view.*
import java.util.*

abstract class ThingAbsFragment : Fragment() {

}

class ThingDetailsFragment : BottomSheetDialogFragment() {
    private var thing: Thing? = null
    private lateinit var nameTxt: TextView
    private lateinit var catagoryTxt: TextView
    private lateinit var count_Goal_Txt: TextView
    private lateinit var cycleTxt: TextView
    private lateinit var deleteThingBtn: Button
    private lateinit var statusChipGroup: ChipGroup
    private lateinit var mainLay: View
    private var madeReset: Boolean = false


    private lateinit var oneHistoryRecyclerView: RecyclerView
    private var oneHistoryLive: LiveData<List<OneHistory>>? = null
    private lateinit var oneHistoryRecAdp: OneHistoryRecyclerAdapter


    private lateinit var localCtgs: Array<String>
    private lateinit var engCtgs: Array<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.new_details_popup, container, false)

        if (thing == null) this.dismiss()

        nameTxt = view.PopUpThingNameTxt
        catagoryTxt = view.PopupCatagoryTxt
        count_Goal_Txt = view.PopUpThingCount_GoaleTxt
        cycleTxt = view.PopUpThingCycleTxt
        statusChipGroup = view.details_statis_chip_group.apply {
            check(if(thing!!.enabled)R.id.details_enabled_chip else R.id.details_disabled_cjip)
            setOnCheckedChangeListener { chipGroup, i ->
                val enabled = i == R.id.details_enabled_chip
                if(thing!!.enabled == enabled)return@setOnCheckedChangeListener
                AsyncTask.execute { ThingsDb.INSTANCE!!.thingDao().updateThing(cloneThing(thing!!).apply { this.enabled = enabled  }) }
                dismiss()
            }
        }
        mainLay = view.navigation_view_layout

        deleteThingBtn = view.DeleteThingBtn.apply { bringToFront();
        setOnClickListener { AsyncTask.execute { ThingsDb.INSTANCE!!.thingDao().deleteThing(thing!!) }
        dismiss()}}

        oneHistoryRecAdp = OneHistoryRecyclerAdapter(context!!)


        val layManager = LinearLayoutManager(context)
        oneHistoryRecyclerView = view.findViewById(R.id.OneHistoryRecyclerView)
        oneHistoryRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = layManager
        }
        oneHistoryRecyclerView.adapter = oneHistoryRecAdp


        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (thing == null) {
            dismiss()
            return
        }


        madeReset = false
        /*statusChkBtn.apply {
            setOnCheckedChangeListener { buttonView, isChecked -> }
            isChecked = thing!!.enabled
            setOnCheckedChangeListener { buttonView, isChecked ->
                *//* detailsOrganizer.changeStatus(this@ThingDetailsPopup.thing,isChecked)*//*
            }
        }*/


        val trans =
            getTransparantColor(deleteThingBtn.context, thing!!.type)
        val prim = getPrimColor(deleteThingBtn.context, thing!!.type)
        val dark = getDarkColor(deleteThingBtn.context, thing!!.type)
        val lit = getLightColor(deleteThingBtn.context, thing!!.type)

        mainLay.backgroundTintList = ColorStateList.valueOf(trans)/*
        showHistoryBtn.backgroundTintList = ColorStateList.valueOf(lit)
        resetHistoryBtn.backgroundTintList = ColorStateList.valueOf(lit)*/
        deleteThingBtn.backgroundTintList = ColorStateList.valueOf(trans)


        val arr = IntArray(2)
        view?.getLocationOnScreen(arr)

        nameTxt.setText(thing!!.name)
        catagoryTxt.setText(localCtgs[engCtgs.indexOf(thing!!.catagory)])
        count_Goal_Txt.setText("" + thing!!.count + " / " + thing!!.goal)
        cycleTxt.setText(thing!!.currentCycle.toString())
        //TODO CHECK THIS OUT


        oneHistoryLive = ThingsDb.getAppDataBase(context!!)?.oneHistoryDao()?.getAllHistoryOfAsLive(thing!!.id)
        oneHistoryLive?.observe((context as MainActivity), androidx.lifecycle.Observer {
            oneHistoryRecAdp.refreshList(it)
        })

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
            ThingDetailsFragment().apply {
                this.thing = thing
            }
    }
}

fun getDateFromLong(lng: Long): String {
    val cal = Calendar.getInstance()
    cal.timeInMillis = lng
    val y = "" + cal.get(Calendar.YEAR)
    val mm = (cal.get(Calendar.MONTH) + 1)
    var m = ""
    if (mm < 10) m = "0"
    m += mm
    val dd = cal.get(Calendar.DAY_OF_MONTH)
    var d = ""
    if (dd < 10) d = "0"
    d += dd
    return "" + y + "." + m + "." + d
}