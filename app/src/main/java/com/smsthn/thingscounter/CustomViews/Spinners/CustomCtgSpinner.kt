package com.smsthn.thingscounter.CustomViews.Spinners

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import com.smsthn.thingscounter.CustomViews.CustomStyles.getPrimColor
import com.smsthn.thingscounter.CustomViews.CustomStyles.getStringInLocale
import com.smsthn.thingscounter.CustomViews.CustomStyles.isColorDark
import com.smsthn.thingscounter.R

class CustomCtgSpinner : AppCompatSpinner {
    constructor(context: Context) : super(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    private lateinit var localCtgs: MutableList<String>
    private lateinit var engCtgs: MutableList<String>
    private var withAll: Boolean = false
    private var all_local = ""
    private var txtView: TextView?

    init {
        txtView = null

    }

    fun selectCtg(ctg: String, isEnglish: Boolean = false) {
        if (ctg == all_local || ctg == "All") {
            setSelection(0, true)
            return
        }
        val index = if (isEnglish) {
            engCtgs.indexOf(ctg) + if (withAll) 1 else 0
        } else localCtgs.indexOf(ctg) + if (withAll) 1 else 0
        setSelection(index, true)
    }

    fun setup(
        txtView: TextView? = null,
        localCtgs: Array<String>,
        engCtgs: Array<String>,
        func: ((String) -> Unit)? = null,
        withAll: Boolean = false
    ) {
        this.withAll = withAll
        if (withAll) all_local = getStringInLocale(context, "all")!!
        this.localCtgs =
            if (withAll) mutableListOf(context.getString(com.smsthn.thingscounter.R.string.all)) else mutableListOf()
        this.engCtgs =
            if (withAll) mutableListOf("All") else mutableListOf()
        this.txtView = txtView
        this.localCtgs.addAll(localCtgs)
        this.engCtgs.addAll(engCtgs)
        val adp = ArrayAdapter<String>(context, R.layout.spinner_item, this.localCtgs)
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter = adp
        isSaveEnabled = false


        onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

                txtView?.setText(context.getString(com.smsthn.thingscounter.R.string.all))
                if (func != null)
                    func("All")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                txtView?.setText(localCtgs[position])
                if (func != null)
                    func(this@CustomCtgSpinner.engCtgs[position])
            }
        }

    }

    override fun setSelection(position: Int, animate: Boolean) {
        val sameSelected = position == selectedItemPosition
        super.setSelection(position, animate)
        if (onItemSelectedListener == null) {
            onItemSelectedListener = object : OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
            }
        }
        onItemSelectedListener!!.onItemSelected(this, selectedView, position, selectedItemId)
        if (sameSelected) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            onItemSelectedListener!!.onItemSelected(this, selectedView, position, selectedItemId)
        }
    }

    override fun setSelection(position: Int) {
        val sameSelected = position == selectedItemPosition
        super.setSelection(position)
        if (onItemSelectedListener == null) {
            onItemSelectedListener = object : OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
            }
        }
        onItemSelectedListener!!.onItemSelected(this, selectedView, position, selectedItemId)
        if (sameSelected) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            onItemSelectedListener!!.onItemSelected(this, selectedView, position, selectedItemId)
        }
    }

    inner class CustomCtgArrayAdapter(
        context: Context,
        res: Int,
        private val ctgs: MutableList<String>,
        val withAll: Boolean = false
    ) : ArrayAdapter<String>(context, res, ctgs) {
        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return TextView(context).apply {
                try {
                    /*val clr = if (withAll && position == 0) Color.WHITE else {
                        val sa = engCtgs[localCtgs.indexOf(ctgs[position])]
                        getPrimColor(context, sa)
                    }
                    setBackgroundColor(clr)
                    if (isColorDark(clr)) setTextColor(Color.WHITE)
                    setText(ctgs[position])*/
                    textSize = 18f
                    setPadding(2, 20, 2, 20)
                    textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                } catch (e: NullPointerException) {
                    Log.e(" CustomTypeArrayAdapter", "getDropDownView of ${ctgs.get(position)} Painter Was Null")
                    return super.getDropDownView(position, convertView, parent)
                }
            }
        }

        /*override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return TextView(context).apply {
                text=ctgs[position]
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)
                gravity = Gravity.CENTER
                textSize = 18f
                setPadding(2, 20, 2, 20)
                textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            }
        }*/
    }
}