package com.smsthn.thingscounter.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.AbsSpinner
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smsthn.thingscounter.CustomViews.CustomStyles.getDarkColor
import com.smsthn.thingscounter.CustomViews.CustomStyles.getLightColor
import com.smsthn.thingscounter.CustomViews.CustomStyles.getStringArrayInLocale
import com.smsthn.thingscounter.CustomViews.Popups.ThingDetailsPopup
import com.smsthn.thingscounter.CustomViews.RecycleViewAdapters.ThingsRecycleViewAdapter
import com.smsthn.thingscounter.CustomViews.Spinners.CustomCtgSpinner
import com.smsthn.thingscounter.Data.Daos.TypeAndCount
import com.smsthn.thingscounter.Data.Entities.Thing
import com.smsthn.thingscounter.Fragments.ViewModels.ThingViewModel

import com.smsthn.thingscounter.R
import kotlinx.android.synthetic.main.thing_fragment.view.*
import kotlin.properties.Delegates
import android.text.method.Touch.onTouchEvent
import android.util.Log
import android.view.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.smsthn.thingscounter.SharedData.MiscSharedData


class ThingFragment : ThingAbsFragment() {

    val args: ThingFragmentArgs by navArgs()

    private lateinit var thingRecyclerView: RecyclerView
    private lateinit var viewModel: ThingViewModel
    private lateinit var recAdapter: ThingsRecycleViewAdapter
    private lateinit var thingDetailsPopup: ThingDetailsPopup
    private lateinit var typerad: RadioGroup
    private lateinit var ctgSpinner: CustomCtgSpinner
    private lateinit var prefs:SharedPreferences
    private var isposnegneu = true



    private var isEnabled: Boolean by Delegates.observable(true) { d, old, new ->
        if (isVisible) try {
            recAdapter.filterThings(this.currentCatagory, this.currentType, new)
        } catch (e: Exception) {
        }
    }
    private var currentCatagory: String by Delegates.observable("All") { d, old, new ->
        if (isVisible) try {
            recAdapter.filterThings(new, this.currentType, isEnabled)
        } catch (e: Exception) {
        }
    }
    val getCurrentCtg get() = currentCatagory

    private var currentType: String   by Delegates.observable("All") { d, old, new ->
        if (isVisible) try {
            recAdapter.filterThings(this.currentCatagory, new, isEnabled)
        } catch (e: Exception) {
        }
    }
    val getCurrentType get() = currentType

    private var localCtgs: Array<String>? = arrayOf()
    private var engCtgs: Array<String>? = arrayOf()

    private var localTypes: Array<String>? = arrayOf()
    private var engTypes: Array<String>? = arrayOf()

    var pr1: ProgressBar? = null
    var pr2: ProgressBar? = null
    var pr3: ProgressBar? = null
    var prtxt1: TextView? = null
    var prtxt2: TextView? = null
    var prtxt3: TextView? = null


    /**
     * onSaveInstanceState
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putBoolean("isEnabled", isEnabled)
            putString("currentCatagory", currentCatagory)
            putString("currentType", currentType)
        }
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
	    
    }*/
    /**
     * onAttach
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        context?.also {

            prefs= PreferenceManager.getDefaultSharedPreferences(context)
            Log.e("THE CURRENT LANGUAGE IS",prefs.getString(getString(R.string.settings_languagees_key),"English"))
            isposnegneu = MiscSharedData(context).is_pos_neg_neu_allowed()
            localCtgs = getStringArrayInLocale(it, "Catagories")

            engCtgs = getStringArrayInLocale(it, "Catagories", "en")
            localTypes = if (isposnegneu) {
                arrayOf(getString(R.string.positive), getString(R.string.negative), getString(R.string.neutral))
            } else getStringArrayInLocale(it, "colors_arr")
            engTypes = if (isposnegneu) {
                arrayOf("Positive", "Negative", "Neutral")
            } else getStringArrayInLocale(it, "colors_arr", "en")
        }

    }

    /**
     * onViewStateRestored
     */
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.apply {
            isEnabled = getBoolean("isEnabled")
            currentCatagory = getString("currentCatagory") ?: "All"
            currentType = getString("currentType") ?: "All"
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
        pr1 = view.ProgBar1;prtxt1 = view.Prog1SumTxt
        pr2 = view.ProgBar2;prtxt2 = view.Prog2SumTxt
        pr3 = view.ProgBar3;prtxt3 = view.Prog3SumTxt
        thingRecyclerView = view.AllThingsRecycleView

        typerad = view.findViewById(R.id.HomeTypeRadioGroup)
        ctgSpinner = view.findViewById(R.id.MainCatagoriesSpinner)

        return view
    }

    /**
     * onActivityCreated
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        currentType = args.Type ?: "All"


        viewModel = ViewModelProviders.of(this).get(ThingViewModel::class.java).apply {
            initViewModel(activity!!.application, isposnegneu)
            enabledThngs.observe(this@ThingFragment, object : Observer<MutableList<Thing>> {
                override fun onChanged(t: MutableList<Thing>?) {
                    recAdapter.refreshThings(t ?: return, true)
                    if (isEnabled) recAdapter.filterThings(currentCatagory, currentType, true)
                }
            })
            disabledThngs.observe(this@ThingFragment, object : Observer<MutableList<Thing>> {
                override fun onChanged(t: MutableList<Thing>?) {
                    recAdapter.refreshThings(t ?: return, false)
                    if (!isEnabled) recAdapter.filterThings(currentCatagory, currentType, false)
                }
            })
            typesAndCounts.observe(this@ThingFragment, object : Observer<List<TypeAndCount>> {
                override fun onChanged(t: List<TypeAndCount>?) {
                    makeSumsAndStuf(t ?: return)
                }

            })
        }
        thingDetailsPopup =
            ThingDetailsPopup(context, viewModel::updateThing, viewModel::deleteThing, viewModel::resetOneHis)
        thingRecyclerView.apply {
            layoutManager = LinearLayoutManager(view!!.context)
            recAdapter =
                ThingsRecycleViewAdapter(mutableListOf(), viewModel::updateThingCount, thingDetailsPopup::openPopup)
            adapter = recAdapter
        }

        typerad.apply {
            when (currentType) {
                "All" -> check(R.id.HomeTypeAllBtn)
                "Positive" -> check(R.id.HomeTypePosBtn)
                "Negative" -> check(R.id.HomeTypeNegBtn)
                "Neutral" -> check(R.id.HomeTypeNeuBtn)
            }
            setOnCheckedChangeListener { g, id ->
                when (id) {
                    R.id.HomeTypePosBtn -> currentType = "Positive"
                    R.id.HomeTypeNegBtn -> currentType = "Negative"
                    R.id.HomeTypeNeuBtn -> currentType = "Neutral"
                    R.id.HomeTypeAllBtn -> currentType = "All"
                }
            }
        }
        ctgSpinner.setup(null, localCtgs!!, engCtgs!!, { s -> currentCatagory = s }, true)

    }


    companion object {
        fun newInstance() = ThingFragment()
    }

    /**
     * makeSumsAndStuf
     */
    private fun makeSumsAndStuf(lst: List<TypeAndCount>) {

        val pr = arrayOf(pr1, pr2, pr3)
        val prtxt = arrayOf(prtxt1, prtxt2, prtxt3)
        if (isposnegneu) {
            listOf("Negative", "Positive", "Neutral").forEachIndexed { i, s ->
                lst.firstOrNull { tc -> tc.type == s } ?: TypeAndCount("").apply {
                    pr[i]?.apply { max = goalsum;progress = countsum }
                    prtxt[i]?.apply { setText("" + countsum + " / " + goalsum) }
                }
            }
        } else {
            val notZero = { tc: TypeAndCount -> tc.countsum > 0 && tc.goalsum > 0 }
            if (lst.isNullOrEmpty()) {
                prtxt1?.setText(getString(R.string.no_data_to_show))
                val clr = ColorStateList.valueOf(Color.LTGRAY)
                pr.forEachIndexed { i, p ->

                    if (i == 0) p?.apply {
                        progress = 0;progressBackgroundTintList = clr
                    } else p?.visibility = View.INVISIBLE
                }
            } else {
                val l = lst.filter(notZero).sortedByDescending { it.countsum.toDouble() / it.goalsum.toDouble()  }.take(3)
                val i = 3 - l.size
                l.forEachIndexed { i2, tc2 ->
                    prtxt[i2]?.apply { setText("" + tc2.countsum + " / " + tc2.goalsum) }
                    pr[i2]?.apply {
                        visibility = View.VISIBLE
                        max = tc2.goalsum;progress = tc2.countsum
                        progressBackgroundTintList = ColorStateList.valueOf(if(tc2.type != "Black")getDarkColor(context!!, tc2.type) else Color.WHITE )
                        progressTintList = ColorStateList.valueOf(getLightColor(context!!, tc2.type))
                    }
                }
                if (i != 0) {
                    val clr = ColorStateList.valueOf(Color.LTGRAY)
                    for (ii in i..2) {
                        if (ii == 0) {
                            pr[ii]?.apply { progress = 0;progressBackgroundTintList = clr }
                            prtxt1?.setText(getString(R.string.no_data_to_show))
                        } else {
                            pr[ii]?.visibility = View.INVISIBLE
                        }
                    }
                }

                view?.invalidate()

            }
        }
    }
}



