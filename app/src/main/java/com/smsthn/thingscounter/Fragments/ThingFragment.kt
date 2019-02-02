package com.smsthn.thingscounter.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.ProgressBar
import android.widget.RadioGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smsthn.thingscounter.CustomViews.CustomStyles.getDarkColor
import com.smsthn.thingscounter.CustomViews.CustomStyles.getLightColor
import com.smsthn.thingscounter.CustomViews.CustomStyles.getStringArrayInLocale
import com.smsthn.thingscounter.CustomViews.RecycleViewAdapters.ThingsRecycleViewAdapter
import com.smsthn.thingscounter.CustomViews.Spinners.CustomCtgSpinner
import com.smsthn.thingscounter.Data.Daos.TypeAndCount
import com.smsthn.thingscounter.Data.Entities.Thing
import com.smsthn.thingscounter.Fragments.ViewModels.ThingViewModel

import com.smsthn.thingscounter.R
import kotlinx.android.synthetic.main.thing_fragment.view.*
import kotlin.properties.Delegates
import android.util.Log
import android.view.*
import androidx.navigation.fragment.findNavController
import com.smsthn.thingscounter.BroadCasts.buildNotificationGeneral
import com.smsthn.thingscounter.BroadCasts.showNotification
import com.smsthn.thingscounter.CustomViews.CustomStyles.getPrimColor
import com.smsthn.thingscounter.CustomViews.Popups.CtgTypeChipPopup
import com.smsthn.thingscounter.Data.Entities.cloneThing
import com.smsthn.thingscounter.Fragments.SecondaryFragments.AddThingFragment
import com.smsthn.thingscounter.MainActivity
import com.smsthn.thingscounter.SharedData.MiscSharedData
import com.smsthn.thingscounter.SharedData.NotificationSharedData


class ThingFragment : ThingAbsFragment() {

    val args: ThingFragmentArgs by navArgs()

    private lateinit var thingRecyclerView: RecyclerView
    private lateinit var viewModel: ThingViewModel
    private lateinit var recAdapter: ThingsRecycleViewAdapter
    private lateinit var thingDetailsBtmSheet: ThingDetailsFragment
    private lateinit var typerad: RadioGroup
    private lateinit var ctgSpinner: CustomCtgSpinner
    private lateinit var prefs: SharedPreferences
    private lateinit var ctgsPopup: CtgTypeChipPopup
    private lateinit var typesPopup: CtgTypeChipPopup
    private var isposnegneu = true


    private fun filterThings() {
        try {
            val c = mutableListOf<String>()
            val t = mutableListOf<String>()
            currentCatagories!!.theMutableList.forEach {
                c.add(engCtgs!![localCtgs!!.indexOf(it)])
            }
            currentTypes!!.theMutableList.forEach {
                t.add(engTypes!![localTypes!!.indexOf(it)])
            }
            if (isVisible) recAdapter.filterThings(
                c,
                t,
                currentName,
                isEnabled
            )
        } catch (e: java.lang.Exception) {
            Log.d("ThingFragment", "Tries to access the adapter befor init")
        }
    }


    private var currentCatagories: ThingObservalbeList? = null
    private var currentTypes: ThingObservalbeList? = null
    private var currentName: String by Delegates.observable("") { d, old, new ->
        kotlin.runCatching {
            recAdapter.filterThings(
                this.currentCatagories!!.get()
                , this.currentTypes!!.get(), new, isEnabled
            )
        }
    }


    private var isEnabled: Boolean by Delegates.observable(true) { d, old, new ->
        if (isVisible) try {
            val clr = if (new) getPrimColor(context!!, "Black") else getDarkColor(context!!, "Gray")
            thingRecyclerView.setBackgroundColor(clr)
            recAdapter.filterThings(
                this.currentCatagories!!.theMutableList,
                currentTypes!!.theMutableList,
                currentName,
                new
            )
        } catch (e: Exception) {
        }
    }


    private var localCtgs: Array<String>? = arrayOf()
    private var engCtgs: Array<String>? = arrayOf()

    private var localTypes: Array<String>? = arrayOf()
    private var engTypes: Array<String>? = arrayOf()

    var pr1: ProgressBar? = null
    var pr2: ProgressBar? = null
    var pr3: ProgressBar? = null


    /**
     * onSaveInstanceState
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putBoolean("isEnabled", isEnabled)
            putStringArray("currentCatagories", currentCatagories?.theMutableList?.toTypedArray())
            putStringArray("currentTypes", currentTypes?.theMutableList?.toTypedArray())
        }
    }


    /**
     * onAttach
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        context?.also {

            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            Log.e("THE CURRENT LANGUAGE IS", prefs.getString(getString(R.string.settings_languagees_key), "English"))
            isposnegneu = MiscSharedData(context).is_pos_neg_neu_allowed()
            localCtgs = getStringArrayInLocale(it, "Catagories")

            engCtgs = getStringArrayInLocale(it, "Catagories", "en")
            localTypes = if (isposnegneu) {
                arrayOf(getString(R.string.positive), getString(R.string.negative), getString(R.string.neutral))
            } else getStringArrayInLocale(it, "colors_arr")
            engTypes = if (isposnegneu) {
                arrayOf("Positive", "Negative", "Neutral")
            } else getStringArrayInLocale(it, "colors_arr", "en")

            currentCatagories = ThingObservalbeList(this::filterThings, this::filterThings, localCtgs!!, engCtgs!!)
            currentTypes = ThingObservalbeList(this::filterThings, this::filterThings, localTypes!!, engTypes!!)
        }

    }

    /**
     * onViewStateRestored
     */
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.apply {
            isEnabled = getBoolean("isEnabled")
            currentCatagories?.theMutableList?.addAll(getStringArray("currentCatagories") ?: arrayOf())
            currentTypes?.theMutableList?.addAll(getStringArray("currentTypes") ?: arrayOf())
        }
    }

    /**
     * onCreateView
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.thing_fragment, container, false)
        pr1 = view.progBar1
        pr2 = view.progBar2
        pr3 = view.progBar3
        thingRecyclerView = view.AllThingsRecycleView

        ctgsPopup = CtgTypeChipPopup(view.context!!, localCtgs!!.toList(), currentCatagories!!, this::restartRecTouch)
        typesPopup =
            CtgTypeChipPopup(view.context!!, localTypes!!.toList(), currentTypes!!, this::restartRecTouch, true)
        view.thing_ctg_btn.setOnClickListener {
            stopRecTouch()
            ctgsPopup.openPopup(it, currentCatagories!!.theLocalMutableList)
        }
        view.thing_type_btn.setOnClickListener {
            stopRecTouch()
            typesPopup.openPopup(it, currentTypes!!.theLocalMutableList)
        }

        view.add_fab.setOnClickListener {
            AddThingFragment().show(fragmentManager, "Add Thing")
        }

        view.search_fab.setOnClickListener {
            view.search_thing_txt.visibility = View.VISIBLE
            view.search_thing_txt.requestFocus()
        }
        view.search_thing_txt.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus)view.search_thing_txt.visibility = View.GONE
        }


        view.thing_bottom_nav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.charts_nav -> ChartsFragment().show(fragmentManager, "Charts")
                R.id.options_nav2 -> findNavController().navigate(R.id.go_to_perfer)
                R.id.disabled_nav -> isEnabled = false
                R.id.things_nav -> isEnabled = true
            }

            true
        }


        return view
    }

    /**
     * onActivityCreated
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel = ViewModelProviders.of(this).get(ThingViewModel::class.java).apply {
            initViewModel(activity!!.application, isposnegneu)
            enabledThngs.observe(this@ThingFragment, object : Observer<MutableList<Thing>> {
                override fun onChanged(t: MutableList<Thing>?) {
                    recAdapter.refreshThings(t ?: return, true)
                    if (isEnabled) recAdapter.filterThings(
                        currentCatagories!!.get(),
                        currentTypes!!.get(),
                        currentName,
                        true
                    )
                }
            })
            disabledThngs.observe(this@ThingFragment, object : Observer<MutableList<Thing>> {
                override fun onChanged(t: MutableList<Thing>?) {
                    recAdapter.refreshThings(t ?: return, false)
                    if (!isEnabled) recAdapter.filterThings(
                        currentCatagories!!.get(),
                        currentTypes!!.get(),
                        currentName,
                        false
                    )
                }
            })
            typesAndCounts.observe(this@ThingFragment, object : Observer<List<TypeAndCount>> {
                override fun onChanged(t: List<TypeAndCount>?) {
                    makeSumsAndStuf(t ?: return)

                }

            })
        }


        thingRecyclerView.apply {
            layoutManager = LinearLayoutManager(view!!.context)
            recAdapter =
                ThingsRecycleViewAdapter(mutableListOf(), viewModel::updateThingCount, { thing, view ->
                    ThingDetailsFragment
                        .newInstance(thing).show(fragmentManager, thing.name)
                },
                    { viewModel.updateThing(cloneThing(it).apply { enabled = true }) },
                    { viewModel.deleteThing(it) })
            adapter = recAdapter
        }


    }


    companion object {
        fun newInstance() = ThingFragment()
    }

    /**
     * makeSumsAndStuf
     */
    private fun makeSumsAndStuf(lst: List<TypeAndCount>) {

        val pr = arrayOf(pr1, pr2, pr3)

        if (isposnegneu) {
            listOf("Positive", "Neutral", "Negative").forEachIndexed { i, s ->
                val sth = lst.firstOrNull { tc -> tc.type == s } ?: TypeAndCount("")
                sth.apply {
                    Log.e("$countsum", "$goalsum")
                    pr[i]!!.max = this.goalsum
                    pr[i]!!.progress = this.countsum
                    if (NotificationSharedData(context!!).get_allow_nots()) buildNotificationGeneral(context!!)
                }
            }
        } else {
            val notZero = { tc: TypeAndCount -> tc.countsum > 0 && tc.goalsum > 0 }
            if (lst.isNullOrEmpty()) {

                val clr = ColorStateList.valueOf(Color.LTGRAY)
                pr.forEachIndexed { i, p ->

                    if (i == 0) p?.apply {
                        progress = 0;progressBackgroundTintList = clr
                    } else p?.visibility = View.INVISIBLE
                }
                buildNotificationGeneral(context!!)
            } else {
                val l =
                    lst.filter(notZero).sortedByDescending { it.countsum.toDouble() / it.goalsum.toDouble() }.take(3)
                val i = 3 - l.size
                val tps = mutableListOf<String>()
                val intdata = mutableListOf<Int>()
                l.forEachIndexed { i2, tc2 ->
                    tps.add(tc2.type)
                    intdata.add(tc2.countsum)
                    intdata.add(tc2.goalsum)
                    pr[i2]?.apply {
                        visibility = View.VISIBLE
                        max = tc2.goalsum;progress = tc2.countsum
                        progressBackgroundTintList = ColorStateList.valueOf(
                            if (tc2.type != "Black") getDarkColor(
                                context!!,
                                tc2.type
                            ) else Color.WHITE
                        )
                        progressTintList = ColorStateList.valueOf(getLightColor(context!!, tc2.type))
                    }
                }
                NotificationSharedData(context!!).apply {
                    if (get_allow_nots()) {
                        if (get_ongoing()) {
                            showNotification(
                                context!!,
                                MainActivity::class.java,
                                "sadaa",
                                "dadsa",
                                tps.toTypedArray(),
                                true,
                                false,
                                intdata.toTypedArray()
                            )
                        } else {
                            buildNotificationGeneral(context!!)
                        }
                    }
                }

                if (i != 0) {
                    val clr = ColorStateList.valueOf(Color.LTGRAY)
                    for (ii in i..2) {
                        if (ii == 0) {
                            pr[ii]?.apply { progress = 0;progressBackgroundTintList = clr }

                        } else {
                            pr[ii]?.visibility = View.INVISIBLE
                        }
                    }
                }


            }
        }
        view?.main_collapsing?.invalidate()

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.toolbar_menu, menu)
    }

    val lsnr = object : RecyclerView.OnItemTouchListener {
        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

        }

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

        }

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            return true
        }
    }

    fun stopRecTouch() {
        thingRecyclerView.addOnItemTouchListener(lsnr)
    }

    fun restartRecTouch() {
        thingRecyclerView.removeOnItemTouchListener(lsnr)
    }


}

class ThingObservalbeList(
    private val addFunc: () -> Unit, private val removeFunc: () -> Unit, private val localRep: Array<String>,
    private val englishRep: Array<String>
) {
    val theMutableList = mutableListOf<String>()
    val theLocalMutableList = mutableListOf<String>()
    fun get() = theMutableList
    fun getLocal() = theLocalMutableList
    fun add(value: String) {
        theLocalMutableList.add(value);theMutableList.add(englishRep[localRep.indexOf(value)])
        addFunc.invoke()
    }

    fun remove(value: String) {
        val i = theLocalMutableList.indexOf(value)
        theLocalMutableList.removeAt(i);theMutableList.removeAt(i)
        removeFunc.invoke()
    }

    fun containsLocal(element: String) = theMutableList.contains(element)
    fun containsEng(element: String) = theMutableList.contains(element)
    fun clear() {
        theMutableList.clear();theLocalMutableList.clear()
        ;removeFunc.invoke()
    }
}