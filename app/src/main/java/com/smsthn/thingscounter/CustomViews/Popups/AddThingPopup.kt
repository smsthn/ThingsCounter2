/*
package com.smsthn.thingscounter.CustomViews.Popups

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.smsthn.CustomViews.CustomStyles.*
import com.smsthn.thingscounter.*
import com.smsthn.thingscounter.CustomViews.CustomStyles.*
import com.smsthn.thingscounter.CustomViews.Spinners.CustomCtgSpinner
import com.smsthn.thingscounter.CustomViews.Spinners.CustomTypeSpinner
import com.smsthn.thingscounter.Data.Entities.SumHistory
import com.smsthn.thingscounter.Data.Entities.Thing
import com.smsthn.thingscounter.Data.Entities.cloneThing
import com.smsthn.thingscounter.SharedData.LanguageSharedData
import com.smsthn.thingscounter.SharedData.MiscSharedData
import kotlinx.android.synthetic.main.add_thing_popup_pos_neg_neu.view.*
import kotlinx.android.synthetic.main.disabled_recycle_one_thing_view.view.*
import kotlinx.android.synthetic.main.recycle_single_thing_view.view.*
import kotlinx.android.synthetic.main.thing_color_picker_popup.view.*

class AddThingPopup(context: Context?) {
    val popup: PopupWindow
    private var thing: Thing?
    private val addBtn: Button
    private val cancelBtn: Button
    private val nameTxt: TextView
    private val catagorySpinner: CustomCtgSpinner
    private lateinit var typeRadioGoup: RadioGroup
    private val goalTxt: TextView
    private val warnLay: LinearLayout
    private val ctgTxt: TextView
    private val tableLay: LinearLayout
    private val btnsLay:LinearLayout
    private var includedeColorPicker:View? = null
    private val addTypeRow:View
    private val addColorRow:View
    private var openColorPickerBtn:Button? = null
    private lateinit var typeSpinner: CustomTypeSpinner
    private var typeIndex = 0
    private var isEdit:Boolean
    private var color:String = "Blue"
    private var catagory:String = "NoCatagory"
    private var colorPicker: ColorPickerClass? = null
    private val engTypes:Array<String>
    private val localTypes:Array<String>
    private var isEngLocal:Boolean = false
    private val localCtgs: Array<String>
    private val engCtgs: Array<String>

    private fun getTypeEngToLocale(ctg: String): String {
        if (isEngLocal) return ctg
        return localTypes[engTypes.indexOf(ctg)]
    }

    private fun getTypeLocaleToEng(ctg: String): String {
        if (isEngLocal) return ctg
        return engTypes[localTypes.indexOf(ctg)]
    }
    private fun getCtgEngToLocale(ctg: String): String {
        if (isEngLocal) return ctg
        return localCtgs[engCtgs.indexOf(ctg)]
    }

    private fun getCtgLocaleToEng(ctg: String): String {
        if (isEngLocal) return ctg
        return engCtgs[localCtgs.indexOf(ctg)]
    }
    init {
        localCtgs = getStringArrayInLocale(context!!, "Catagories")!!
        if (LanguageSharedData(context).get_lang_code() == "en") {
            engCtgs = localCtgs;isEngLocal = true
        } else engCtgs = getStringArrayInLocale(
            context,
            "Catagories",
            "en"
        )!!


        if(MiscSharedData(context).is_pos_neg_neu_allowed()){
            if(LanguageSharedData(context).get_lang_code() == "en")isEngLocal = true
            localTypes = listOf("positive","negative","neutral").map { getStringInLocale(
                context, it
            )!! }.toTypedArray()
            engTypes = if(isEngLocal) localTypes else listOf("positive","negative","neutral").map { getStringInLocale(
                context, it, "en"
            )!! }.toTypedArray()
        } else {
            localTypes = getStringArrayInLocale(
                context!!,
                "colors_arr"
            )!!
            if (LanguageSharedData(context).get_lang_code() == "en") {
                engTypes = localTypes;isEngLocal = true
            } else engTypes = getStringArrayInLocale(
                context,
                "colors_arr",
                "en"
            )!!
        }

        isEdit = false

        thing = null
        val infalter = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = */
/*if(!UiOrganizer.IsPosNegNeu)*//*
infalter.inflate(R.layout.add_thing_popup_pos_neg_neu, null)
                    */
/*else infalter.inflate(R.layout.add_thing_popup, null)*//*

        val pont = getDevicePoint(context)
        popup = PopupWindow(
            view, pont.x-20,
            pont.y-20
        )
        */
/*view.layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)*//*


        popup.isFocusable = true
        popup.isOutsideTouchable = false
        popup.elevation = 20f
        popup.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        includedeColorPicker = view.ColorPickerIncludedLay
        addBtn = view.findViewById(R.id.AddThingBtn)
        cancelBtn = view.findViewById(R.id.AddThingCancelBtn)
        nameTxt = view.findViewById(R.id.AddThingNameTxt)
        catagorySpinner = view.findViewById(R.id.AddCatagorySpinner)

        goalTxt = view.findViewById(R.id.AddGoadTxt)
        warnLay = view.findViewById(R.id.AddWarningLayout)
        ctgTxt = view.findViewById(R.id.AddCatagoryTxt)
        tableLay = view.findViewById(R.id.AddTableLayout)
        btnsLay = view.findViewById(R.id.BtnsLinLay)
        addTypeRow = view.findViewById(R.id.AddTypeRow)
        addColorRow = view.findViewById(R.id.AddColorRow)
        openColorPickerBtn = view.findViewById(R.id.OpenColorPickerButton)
        if(!MiscSharedData(context).is_pos_neg_neu_allowed()){
            typeSpinner = view.AddTypeSpinner
            typeSpinner.setup(null, engTypes,localTypes,
                {s:String->
                    val clr = getTransparantColor(context, s)
                    tableLay.setBackgroundColor(clr)
                    btnsLay.setBackgroundColor(clr)
                    color = s
                    thing?.type = s
                    catagory = s

                    },false)

        }else {
            typeRadioGoup = view.findViewById(R.id.AddThingTypeRadGroup)
        }




        if(MiscSharedData(context).is_pos_neg_neu_allowed()){
            addColorRow.visibility = View.GONE
            addTypeRow.visibility = View.VISIBLE
        } else {
            addColorRow.visibility = View.VISIBLE
            addTypeRow.visibility = View.GONE
        }

        catagorySpinner.setup(ctgTxt,localCtgs,engCtgs,{
            s->
        })

        addEvents()
    }

    private fun addEvents() {
        addBtn.setOnClickListener {
            var name = nameTxt.text.toString()
            var goal = goalTxt.text.toString().toInt()
            var ctg = catagory
            var type = if(MiscSharedData(popup.contentView.context).is_pos_neg_neu_allowed())engTypes[typeIndex] else color
            if (name.trim().equals("") || goal <= 0 || ctg.trim().equals("") || type.trim().equals("")) {
                showWarning()
                return@setOnClickListener
            }
            if (!isEdit||thing == null) {
                thing =
                    Thing(name = name, catagory = ctg, type = type, goal = goal)
            } else {
                thing?.apply {
                    this.name = name
                    this.catagory = ctg
                    this.goal = goal
                    this.type = type
                }
            }

            if(isEdit) editOrganizer.dismissEditThing(thing) else addOrganizer.dismissAddThing(thing)
        }
        cancelBtn.setOnClickListener {
            popup.dismiss()
        }
        popup.setOnDismissListener {
            goalTxt.setText("10")
            nameTxt.setText("")
            catagorySpinner.setSelection(0)
            if(MiscSharedData(popup.contentView.context).is_pos_neg_neu_allowed())typeRadioGoup.check(R.id.AddPositiveRad)
            thing = null
            warnLay.removeAllViews()
            if(isEdit) editOrganizer.dismissEditThing(onlyDismiss = true)
            else addOrganizer.dismissAddThing(onlyDismiss = true)
        }
        var i  =1
        if(MiscSharedData(popup.contentView.context).is_pos_neg_neu_allowed()){
            typeRadioGoup.setOnCheckedChangeListener { group, checkedId ->
                listOf("Green","Red","Gray")[listOf(R.id.AddPositiveRad,R.id.AddNegativeRad,
                    R.id.AddNeutralRad ).indexOf(checkedId)].apply {
                    getTransparantColor(tableLay.context, this).apply {

                        tableLay.setBackgroundColor(this)
                            btnsLay.setBackgroundColor(this)
                    }
                }

            }
        } else {
            openColorPickerBtn?.setOnClickListener {

                if(includedeColorPicker?.visibility == View.VISIBLE){
                    includedeColorPicker?.visibility = View.GONE
                    btnsLay.visibility = View.VISIBLE
                    tableLay.visibility = View.VISIBLE
                }
                else{
                    if(colorPicker == null)colorPicker = ColorPickerClass(popup.contentView)
                    colorPicker?.selectedColor = thing?.type?:"Blue"
                    includedeColorPicker?.visibility = View.VISIBLE
                    tableLay.visibility = View.GONE
                    btnsLay.visibility = View.GONE
                }
            }
        }

    }

    private fun showWarning() {
        val text = TextView(tableLay.context)
        text.setText("Please Fill EveryThing Properly")
        text.setTextColor(Color.RED)
        warnLay.removeAllViewsInLayout()
        warnLay.addView(text)
    }

    fun openPopup(view: View,notPosNegNeu:Boolean = false) {
        isEdit = false
        addBtn.setText(tableLay.context.getString(R.string.add_btn_txt))
        popup.showAtLocation(view, Gravity.CENTER, 0, 0)
    }

    fun openPopupForEdit(view: View, thing: Thing, notPosNegNeu:Boolean = false) {
        isEdit = true
        addBtn.setText(tableLay.context.getString(R.string.edit_thing_btn_txt))
        goalTxt.setText("" + thing.goal)
        nameTxt.setText(thing.name)
        catagorySpinner.selectCtg(thing.catagory)
        ctgTxt.setText(thing.catagory)
        when (thing.type) {
            "Positive" -> typeRadioGoup.check(R.id.AddPositiveRad)
            "Negative" -> typeRadioGoup.check(R.id.AddNegativeRad)
            "Neutral" -> typeRadioGoup.check(R.id.AddNeutralRad)
        }
        this.thing = thing
        popup.showAtLocation(view, Gravity.CENTER, 0, 0)
    }

    */
/**
     * The ColorPicker Class
     *//*

    private inner class ColorPickerClass(view:View){
        var okBtn:Button? = null
        var cancelBtn:Button? = null
        var colorBtnsLst:List<ImageButton> = listOf()
        var colorBtnsLstDK:List<ImageButton> = listOf()
        var selectedColor = "Blue"
        */
/*val view:View*//*

        val previewTypeLst = listOf<String>("Blue","DarkBlue","Yellow","Red","Brown","Orange","Pink",
            "Purple","Gray","Green")
        val previewDKTypeLst = listOf<String>("DarkOrange","DarkerBlue","DarkRed","DarkGreen","Black","MilitaryGreen","Morasaki")

        var minusBtn:Button? = null
        var plusBtn:Button? = null
        var moreDetailsBtn:Button? = null
        var prog:ProgressBar? = null
        var nameTxt:TextView? = null
        var countTxt:TextView? = null
        var goalTxt:TextView? = null
        var slash:TextView? = null
        var disabledThingMainLay:View? = null

        init{
            minusBtn= view.ThingMinusBtn
            plusBtn= view.ThingPlusBtn
            moreDetailsBtn= view.MoreDetailsBtn
            prog= view.ThingProgressBar
            nameTxt= view.ThingGoalTxt
            countTxt= view.ThingCountTxt
            goalTxt = view.ThingGoalTxt
            slash = view.SlashTxt
            disabledThingMainLay = view.DisabledThingMainLayout

            okBtn = view.ColorPickerOkBtn
            cancelBtn = view.ColorPickerCancelBtn
            okBtn?.setOnClickListener {

                thing?.type = selectedColor
                color = selectedColor
                tableLay.setBackgroundColor(
                    getTransparantColor(
                        view.context,
                        selectedColor
                    )
                )
                btnsLay.setBackgroundColor(
                    getTransparantColor(
                        view.context,
                        selectedColor
                    )
                )
                typeSpinner.selectType(selectedColor)
                includedeColorPicker?.visibility = View.GONE
                btnsLay.visibility = View.VISIBLE
                tableLay.visibility = View.VISIBLE
            }
            cancelBtn?.setOnClickListener {
                includedeColorPicker?.visibility = View.GONE
                btnsLay.visibility = View.VISIBLE
                tableLay.visibility = View.VISIBLE
            }
            colorBtnsLst = loadImgBtns(view)
                .apply { forEach { view.PickersColorsHolderLayout.addView(it) };get(0).callOnClick() }
            colorBtnsLstDK = loadDarkerBtns(view)
                .apply { forEach { view.PickersColorsHolderLayoutDarker.addView(it) }}



        }

        private fun loadImgBtns(view:View):List<ImageButton>{
            val context = view.context
            var index = 0
            var point = Point(0,0)
            (context as MainActivity).windowManager.defaultDisplay.getSize(point)



            val w = point.x/10
            val sampleEnabledThng = Thing(
                0, "", "", "", true, 0, 0, 0,
                0, 0, SumHistory(0, 0, 0), 0
            )
            val sampleDisabledThng = Thing(
                0, "", "", "", false, 0, 0, 0,
                0, 0, SumHistory(0, 0, 0), 0
            )
            return listOf<Int>(R.drawable.blue_round,R.drawable.dark_blue_round,R.drawable.yellow_round,
                R.drawable.red_round,R.drawable.brown_round,R.drawable.orange_round,R.drawable.pink_round,R.drawable.purple_round,
                R.drawable.gray_round,R.drawable.green_round).map {
                context!!.getDrawable(it).run {
                    ImageButton(context).apply { this.contentDescription = previewTypeLst[index++];
                        background = this@run;
                        layoutParams = ViewGroup.MarginLayoutParams(w-5,w-5).apply { setMargins(2,10,2,10) }
                        setOnClickListener {
                            cloneThing(sampleEnabledThng).apply {
                                paintThing(this)
                                type = contentDescription.toString() }
                            selectedColor =contentDescription.toString()
                        } }
                }
            }
        }
        private fun loadDarkerBtns(view:View):List<ImageButton>{
            val context = view.context
            var index = 0
            var point = Point(0,0)
            (context as MainActivity).windowManager.defaultDisplay.getSize(point)
            val w = point.x/10
            val sampleEnabledThng = Thing(
                0, "", "", "", true, 0, 0, 0,
                0, 0, SumHistory(0, 0, 0), 0
            )
            val sampleDisabledThng = Thing(
                0, "", "", "", false, 0, 0, 0,
                0, 0, SumHistory(0, 0, 0), 0
            )

            return listOf(R.drawable.dark_orange_round,R.drawable.darker_blue_round,R.drawable.darker_red_round,
                R.drawable.dark_green_round,R.drawable.black_round,R.drawable.military_green_round,
                R.drawable.morasaki_round).map {
                context!!.getDrawable(it).run {
                    ImageButton(context).apply { this.contentDescription = previewDKTypeLst[index++];
                        background = this@run;
                        layoutParams = ViewGroup.MarginLayoutParams(w-5,w-5).apply { setMargins(4,10,4,10) }
                        setOnClickListener {
                            cloneThing(sampleEnabledThng).apply {
                                paintThing(this)
                                type = contentDescription.toString() }
                            selectedColor =contentDescription.toString()
                        } }
                }
            }

        }
        private fun paintThing(thing: Thing){
            disabledThingMainLay?.setBackgroundColor(
                getTransparantColor(
                    plusBtn?.context!!,
                    thing.type
                )
            )
            minusBtn?.setBackgroundColor(
                getPrimColor(
                    plusBtn?.context!!,
                    thing.type
                )
            )
            plusBtn?.setBackgroundColor(
                getDarkColor(
                    plusBtn?.context!!,
                    thing.type
                )
            )
            moreDetailsBtn?.setBackgroundColor(
                getLightColor(
                    plusBtn?.context!!,
                    thing.type
                )
            )
            prog?.progressDrawable?.setTint(
                getLightColor(
                    plusBtn?.context!!,
                    thing.type
                )
            )

            val isDark = isColorDark(
                getPrimColor(
                    plusBtn?.context!!,
                    thing.type
                )
            )
            val txtclr = if (isDark) Color.WHITE else Color.BLACK
            nameTxt?.setTextColor(Color.WHITE)
            countTxt?.setTextColor(txtclr)
            goalTxt?.setTextColor(txtclr)
            slash?.setTextColor(txtclr)
        }

    }
}


*/
