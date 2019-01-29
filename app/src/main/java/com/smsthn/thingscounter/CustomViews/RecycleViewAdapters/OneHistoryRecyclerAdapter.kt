package com.smsthn.thingscounter.CustomViews.RecycleViewAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smsthn.thingscounter.Data.Entities.OneHistory
import com.smsthn.thingscounter.CustomViews.Popups.getDateFromLong
import com.smsthn.thingscounter.R
import kotlinx.android.synthetic.main.one_his_recycler_single_thing.view.*

class OneHistoryRecyclerAdapter(context: Context) :
    RecyclerView.Adapter<OneHistoryRecyclerAdapter.OneHistoryViewHolder>() {
    private val oneHisLst: MutableList<OneHistory>

    init {
        oneHisLst = mutableListOf()
    }
    fun refreshList(lst:Collection<OneHistory>){
        oneHisLst.clear()
        oneHisLst.addAll(lst)
        notifyDataSetChanged()
    }
    fun emptyList(){
        oneHisLst.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OneHistoryViewHolder {
        return OneHistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.one_his_recycler_single_thing,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return oneHisLst.size
    }

    override fun onBindViewHolder(holder: OneHistoryViewHolder, position: Int) {
        val oh = oneHisLst[position]
        holder.setup("Day"+(position + 1),
            getDateFromLong(oh.date), ""+oh.count,""+oh.goal)
    }


    inner class OneHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dayTxt: TextView
        private val dateTxt: TextView
        private val countTxt: TextView
        private val goalTxt: TextView

        init {
            dayTxt = view.OneHistoryDayTxt
            dateTxt = view.OneHistoryDateTxt
            countTxt = view.OneHistoryCountTxt
            goalTxt = view.OneHistoryGoalTxt
        }

        fun setup(day: String, date: String, count: String, goal: String) {
            dayTxt.setText(day)
            dateTxt.setText(date)
            countTxt.setText(count)
            goalTxt.setText(goal)
        }
    }
}