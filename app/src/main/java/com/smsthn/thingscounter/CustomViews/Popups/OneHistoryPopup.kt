package com.smsthn.thingscounter.CustomViews.Popups

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smsthn.thingscounter.CustomViews.CustomStyles.getTransparantColor
import com.smsthn.thingscounter.Data.Entities.OneHistory
import com.smsthn.thingscounter.Data.Entities.Thing
import com.smsthn.thingscounter.Data.ThingsDb
import com.smsthn.thingscounter.CustomViews.RecycleViewAdapters.OneHistoryRecyclerAdapter
import com.smsthn.thingscounter.MainActivity
import com.smsthn.thingscounter.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class OneHistoryPopup(context: Context?) {
    val popup: PopupWindow
    private val oneHistoryMainLayout: LinearLayout
    /*private val currentDateTxt: TextView
    private val currentDateCountGoal: TextView*/
    private val oneHistoryRecyclerView: RecyclerView
    private var oneHistoryLive: LiveData<List<OneHistory>>?
    private val oneHistoryRecAdp: OneHistoryRecyclerAdapter
    /*private val backBtn:Button*/

    init {
        oneHistoryLive = null


        val infl = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = infl.inflate(R.layout.one_history_popup, null)

        /*backBtn = view.findViewById(R.id.OneHisBackBtn)*/

        oneHistoryRecAdp = OneHistoryRecyclerAdapter(context)


        val layManager = LinearLayoutManager(context)
        oneHistoryRecyclerView = view.findViewById(R.id.OneHistoryRecyclerView)
        oneHistoryRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = layManager
        }

        popup = PopupWindow(
            view,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        popup.isFocusable = true
        popup.isOutsideTouchable = false
        popup.setOnDismissListener {
            oneHistoryLive = null
            oneHistoryRecAdp.emptyList()
            oneHistoryRecyclerView.adapter = null
        }

        oneHistoryMainLayout = view.findViewById(R.id.OneHistoryPopupMainLayout)
        /*currentDateCountGoal = view.findViewById(R.id.CurrentCycleCountGoalTxt)
        currentDateTxt = view.findViewById(R.id.CurrentCycleDateTxt)
        backBtn.setOnClickListener {
            popup.dismiss()
        }

        view.findViewById<Button>(R.id.OneHisBackBtn).setOnClickListener { popup.dismiss() }*/


    }

    fun openPopup(thing: Thing) {
       /* currentDateCountGoal.setText("" + thing.count + " / " + thing.goal)

        currentDateTxt.setText(getDateFromLong(Calendar.getInstance().timeInMillis))*/

        oneHistoryRecyclerView.adapter = oneHistoryRecAdp

        oneHistoryMainLayout.setBackgroundColor(
            getTransparantColor(
                oneHistoryMainLayout.context,
                thing.type
            )
        )



        oneHistoryLive = ThingsDb.getAppDataBase(oneHistoryMainLayout.context)?.oneHistoryDao()?.getAllHistoryOfAsLive(thing.id)
        oneHistoryLive?.observe((oneHistoryMainLayout.context as MainActivity), androidx.lifecycle.Observer {
            oneHistoryRecAdp.refreshList(it)
        })



        popup.showAtLocation((oneHistoryMainLayout.context as MainActivity).container, Gravity.CENTER, 0, 0)

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