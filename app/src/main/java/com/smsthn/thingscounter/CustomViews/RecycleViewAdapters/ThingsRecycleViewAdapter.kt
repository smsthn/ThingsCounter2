package com.smsthn.thingscounter.CustomViews.RecycleViewAdapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.smsthn.thingscounter.Data.Entities.Thing
import kotlinx.android.synthetic.main.disabled_recycle_one_thing_view.view.*
import kotlinx.android.synthetic.main.recycle_single_thing_view.view.*
import android.graphics.Paint
import android.os.AsyncTask
import android.os.Looper
import com.smsthn.thingscounter.*
import com.smsthn.thingscounter.CustomViews.CustomStyles.*
import com.smsthn.thingscounter.SharedData.LanguageSharedData
import com.smsthn.thingscounter.SharedData.MiscSharedData

class ThingsRecycleViewAdapter(
    allThingss: MutableList<Thing>/*, private val context: Context*/,
    private val countFunc: (Long, Int) -> Unit,
    private val detailsFunc: (Thing, View) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var displayedThings: MutableList<Thing>

    private var isOverCountingAllowed = false

    private var lastPosition = -1
    private var delayTime: Long = 0
    val backupEnabled = mutableListOf<Thing>()
    val backupDisabled = mutableListOf<Thing>()

    private val VIEW_TYPE_ENABLED = 1
    private val VIEW_TYPE_DISABLED = 2


    init {

        displayedThings = allThingss
    }

    fun refreshThings(newThings: MutableList<Thing>, enabled: Boolean = true) {

        if (enabled) {
            backupEnabled.clear()
            backupEnabled.addAll(newThings)
        } else {
            backupDisabled.clear()
            backupDisabled.addAll(newThings)
        }
    }

    /*fun filterThings(ctg: String, type: String, enabled: Boolean = true) {
        val backupThings = if (enabled) backupEnabled else backupDisabled
        val c = if (ctg == "All") "" else ctg
        val t = if (type == "All") "" else type
        val lst = backupThings.filter { (c.isBlank().or(c == it.catagory)).and(t.isBlank().or(it.type == t)) }
            .toMutableList()
        val diff = DiffUtil.calculateDiff(
            ThingRecycleDiffCallback(
                this.displayedThings,
                lst
            )
        )
        diff.dispatchUpdatesTo(this)
        displayedThings = lst
    }*/

    fun filterThings(ctgs: MutableList<String>, types: MutableList<String>, enabled: Boolean = true) {
        val backupThings = if (enabled) backupEnabled else backupDisabled
        val lst = backupThings.filter {
            (ctgs.isNullOrEmpty().or(ctgs.contains(it.catagory)))
                .and(types.isNullOrEmpty().or(types.contains(it.type)))
        }
            .toMutableList()
        val diff = DiffUtil.calculateDiff(
            ThingRecycleDiffCallback(
                this.displayedThings,
                lst
            )
        )
        diff.dispatchUpdatesTo(this)
        displayedThings = lst
    }

    override fun getItemViewType(position: Int): Int {
        if (displayedThings[position].enabled) return VIEW_TYPE_ENABLED
        else return VIEW_TYPE_DISABLED
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        isOverCountingAllowed = MiscSharedData(recyclerView.context!!).is_over_counting_allowed()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_DISABLED) {
            return DisabledThingsViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.disabled_recycle_one_thing_view,
                    parent,
                    false
                )
            )
        }
        return EnabledThingsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recycle_single_thing_view,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return displayedThings.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DisabledThingsViewHolder) {
            holder.setup(thing = displayedThings[position])
        } else {
            (holder as EnabledThingsViewHolder).initThis(thing = displayedThings[position])
        }
    }

    inner class EnabledThingsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var thing: Thing? = null
        val nameTxt: TextView
        val countTxt: TextView
        val goalTxt: TextView
        val slash: TextView
        val mainlayout: LinearLayout
        val minusBtn: Button
        val plusBtn: Button
        val moreDetailsBtn: Button
        val thingTransLayout: LinearLayout
        val prog: ProgressBar
        val prog2: ProgressBar
        val handler: Handler

        init {
            nameTxt = view.ThingNameTxt
            countTxt = view.ThingCountTxt
            goalTxt = view.ThingGoalTxt
            slash = view.SlashTxt
            mainlayout = view.ThingMainLayout
            minusBtn = view.ThingMinusBtn
            plusBtn = view.ThingPlusBtn
            thingTransLayout = view.ThingTransLayout
            prog = view.ThingProgressBar
            prog2 = view.ThingProgressBar2
            moreDetailsBtn = view.MoreDetailsBtn
            handler = Handler(Looper.getMainLooper())
            view.setBackgroundColor(Color.WHITE)
            view.elevation = 20f


        }

        fun initThis(thing: Thing) {
            this.thing = thing




            initVals(thing)
            initBtns()

            //thingTransLayout.background = (context as MainActivity).getDrawable(R.drawable.green_down_grad)
            minusBtn.setBackgroundColor(
                getPrimColor(
                    this.itemView.context,
                    thing.type
                )
            )
            plusBtn.setBackgroundColor(
                getPrimColor(
                    this.itemView.context,
                    thing.type
                )
            )
            moreDetailsBtn.setBackgroundColor(
                getLightColor(
                    this.itemView.context,
                    thing.type
                )
            )
            prog.progressTintList = ColorStateList.valueOf(
                getTransparantColor(
                    this.itemView.context,
                    thing.type
                )
            )
            prog2.progressTintList = ColorStateList.valueOf(
                getTransparantColor(
                    this.itemView.context,
                    thing.type
                )
            )
            /*nameTxt.setBackgroundColor(
                getTransparantColor(
                    this.itemView.context,
                    thing.type
                )
            )*/

            /*val isDark = isColorDark(
                getPrimColor(
                    this.itemView.context,
                    thing.type
                )
            )
            val txtclr = if (isDark) Color.WHITE else Color.BLACK
            plusBtn.setTextColor(Color.WHITE)
            minusBtn.setTextColor(txtclr)
            moreDetailsBtn.setTextColor(txtclr)*/

            /*prog.progressBackgroundTintList = ColorStateList.valueOf(getLightColor(this.itemView.context,"Gray"))*/
        }


        private fun initVals(thing: Thing) {
            val co = thing.count.toString()
            val go = thing.goal.toString()
            nameTxt.setText(thing.name)
            countTxt.setText(co)
            goalTxt.setText(go)
            prog.max = thing.goal
            prog2.max = thing.goal
            prog.setProgress(thing.count)
            prog2.setProgress(thing.count)
        }

        private fun initBtns() {
            minusBtn.setOnClickListener {

                if (thing!!.count - 1 >= 0)
                    countFunc.invoke(thing!!.id, thing!!.count - 1)
            }
            plusBtn.setOnClickListener {
                if (!isOverCountingAllowed && thing!!.count + 1 > thing!!.goal) return@setOnClickListener
                countFunc.invoke(thing!!.id, thing!!.count + 1)

            }
            prog.setOnClickListener {
                detailsFunc.invoke(thing!!, this.itemView)
            }
            prog2.setOnClickListener {
                detailsFunc.invoke(thing!!, this.itemView)
            }
            plusBtn.setOnLongClickListener {
                var count = 0
                AsyncTask.execute {
                    var time: Long = 500

                    var busy = false
                    while (plusBtn.isPressed) {
                        if (!isOverCountingAllowed)
                            if (thing!!.count + count >= thing!!.goal) break

                        if (!busy) {
                            busy = true
                            handler.postDelayed({
                                count++
                                countTxt.setText("" + (count + thing!!.count))
                                prog.setProgress(count + thing!!.count)
                                prog2.setProgress(count + thing!!.count)
                                time = if (time < 30) 30 else (if (time <= 100) time - 10 else time - 100)
                                busy = false

                            }, time)
                        }

                    }
                    if (count != 0) countFunc.invoke(thing!!.id, thing!!.count + count)
                }
                true
            }
            minusBtn.setOnLongClickListener {
                var count = 0
                AsyncTask.execute {
                    var time: Long = 500

                    var busy = false
                    while (minusBtn.isPressed) {
                        if ((thing!!.count + count) <= 0) break

                        if (!busy) {
                            busy = true
                            handler.postDelayed({
                                count--
                                countTxt.setText("" + (thing!!.count + count))
                                prog.setProgress(thing!!.count + count)
                                prog2.setProgress(thing!!.count + count)
                                time = if (time < 30) 30 else (if (time <= 100) time - 10 else time - 100)
                                busy = false

                            }, time)
                        }


                    }
                    if (count != 0) countFunc.invoke(thing!!.id, thing!!.count + count)
                }
                true
            }
        }

    }

    inner class DisabledThingsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTxt: TextView
        val ctgTxt: TextView
        val goalTxt: TextView
        val showHistoryBtn: Button
        val disabledThingMainLay: LinearLayout
        val enableThngBtn: Button
        var thing: Thing?
        val localCtgs: Array<String>
        val engCtgs: Array<String>
        var engIsLocale: Boolean = false

        init {
            thing = null
            nameTxt = view.DisabledThingNameTxt.apply { paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG }
            ctgTxt = view.DisabledThingCatagoryTxt.apply { paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG }
            goalTxt = view.DisabledThingGoal.apply { paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG }
            showHistoryBtn = view.DisabledThingShowHistoryBtn
            disabledThingMainLay = view.DisabledThingMainLayout
            enableThngBtn = view.DisabledThingEnableBtn.apply {
                setOnClickListener {
                    /* organizer.enableThing(thing)*/
                }
            }
            view.setBackgroundColor(Color.WHITE)
            view.elevation = 10f
            localCtgs = getStringArrayInLocale(
                this.itemView.context,
                "Catagories"
            )!!
            if (LanguageSharedData(this.itemView.context).get_lang_code() == "en") {
                engCtgs = localCtgs;engIsLocale = true
            } else engCtgs = getStringArrayInLocale(
                this.itemView.context,
                "Catagories",
                "en"
            )!!
        }

        private fun getCtgEngToLocale(ctg: String): String {
            if (engIsLocale) return ctg
            return localCtgs[engCtgs.indexOf(ctg)]
        }

        private fun getCtgLocaleToEng(ctg: String): String {
            if (engIsLocale) return ctg
            return engCtgs[localCtgs.indexOf(ctg)]
        }

        fun setup(thing: Thing) {
            this.thing = thing
            disabledThingMainLay.setBackgroundColor(
                getTransparantColor(
                    this.itemView.context,
                    thing.type
                )
            )
            nameTxt.setText(thing.name)
            ctgTxt.setText(getCtgEngToLocale(thing.catagory))
            goalTxt.setText("" + thing.goal)
            showHistoryBtn.setOnClickListener {
                /*organizer.openOneHistoryPopup(thing)*/
            }
        }
    }
}

class ThingRecycleDiffCallback(private val oldList: MutableList<Thing>, private val newList: MutableList<Thing>) :
    DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].compareToThing(newList[newItemPosition])

    }

}
