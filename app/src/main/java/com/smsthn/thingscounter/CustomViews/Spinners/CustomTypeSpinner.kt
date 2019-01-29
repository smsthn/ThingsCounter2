package com.smsthn.thingscounter.CustomViews.Spinners


import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
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

class CustomTypeSpinner : AppCompatSpinner {
    constructor(context: Context) : super(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    private lateinit var localTypes: MutableList<String>
    private lateinit var engTypes: MutableList<String>
    private var withAll:Boolean = false
    private var all_local = ""
    private var txtView: TextView?

    init {
        txtView = null

    }

    fun selectType(type: String,isEnglish:Boolean = false) {
        if(type == all_local||type == "All"){
            setSelection(0,true)
            return
        }
        val index = if(isEnglish){
            engTypes.indexOf(type)+if(withAll)1 else 0
        } else localTypes.indexOf(type)+if(withAll)1 else 0
        setSelection(index,true)

    }

    fun setup(txtView: TextView? = null,
              engTypes: Array<String>,
              localeTypes: Array<String>,
              func: ((String) -> Unit)? = null,
              withAll: Boolean = false
    ) {
        if(withAll)all_local = getStringInLocale(context, "all")!!
        this.localTypes =
            if (withAll) mutableListOf(context.getString(R.string.all)) else mutableListOf()
        this.engTypes =
            if (withAll) mutableListOf("All") else mutableListOf()
        this.txtView = txtView
        this.localTypes.addAll(localeTypes)
        this.engTypes.addAll(engTypes)
        val adp = CustomTypeArrayAdapter(context, android.R.layout.simple_spinner_item, this.localTypes, withAll)
        //adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter = adp
        isSaveEnabled = false


        onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                txtView?.setText(context.getString(R.string.all))
                if (func != null) func(context.getString(R.string.all))
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                txtView?.setText(this@CustomTypeSpinner.localTypes[position])
                if (func != null) func(this@CustomTypeSpinner.engTypes[position])
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
    inner class CustomTypeArrayAdapter(context: Context,
                                       res: Int,
                                       private val types: MutableList<String>,
                                       val withAll: Boolean = false
    ) : ArrayAdapter<String>(context, res, types) {
        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return TextView(context).apply {
                try {
                    val clr = if (withAll && position == 0) Color.WHITE else {
                        val sa = engTypes[localTypes.indexOf(types[position])]
                        getPrimColor(context, sa)
                    }
                    setBackgroundColor(clr)
                    if (isColorDark(clr)) setTextColor(Color.WHITE)
                    setText(types[position])
                    textSize = 18f
                    setPadding(2, 20, 2, 20)
                    textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                } catch (e: NullPointerException) {
                    Log.e(" CustomTypeArrayAdapter", "getDropDownView of ${types.get(position)} Painter Was Null")
                    return super.getDropDownView(position, convertView, parent)
                }
            }
        }
    }
}

