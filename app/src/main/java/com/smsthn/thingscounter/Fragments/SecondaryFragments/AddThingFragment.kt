package com.smsthn.thingscounter.Fragments.SecondaryFragments

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.smsthn.thingscounter.CustomViews.CustomStyles.*
import com.smsthn.thingscounter.CustomViews.Popups.CtgTypeChipPopup
import com.smsthn.thingscounter.Data.Entities.SumHistory
import com.smsthn.thingscounter.Data.Entities.Thing
import com.smsthn.thingscounter.Data.Entities.cloneThing
import com.smsthn.thingscounter.Fragments.ThingObservalbeList
import com.smsthn.thingscounter.Fragments.ViewModels.AddThingViewModel
import com.smsthn.thingscounter.MainActivity
import com.smsthn.thingscounter.SharedData.LanguageSharedData
import com.smsthn.thingscounter.SharedData.MiscSharedData
import com.smsthn.thingscounter.R
import kotlinx.android.synthetic.main.add_thing_fragment.*
import kotlinx.android.synthetic.main.add_thing_fragment.view.*
import kotlinx.android.synthetic.main.disabled_recycle_one_thing_view.view.*
import kotlinx.android.synthetic.main.recycle_single_thing_view.view.*
import kotlinx.android.synthetic.main.thing_color_picker_popup.view.*
import kotlin.Exception
import kotlin.properties.Delegates

class AddThingFragment : BottomSheetDialogFragment()
{
	//args

	private var isEdit: Boolean = false
	private var typeIndex = 0
	private lateinit var addbtn:Button
	private lateinit var cancelbtn:Button
	private lateinit var plusbtn:Button
	private lateinit var minusbtn:Button
	private lateinit var detbtn:Button
	private lateinit var addmainlay:View
	private lateinit var prog:ProgressBar
	private lateinit var prog2:ProgressBar
	private lateinit var ctgMainChip:Chip
	private lateinit var typeMainChip:Chip
	private lateinit var ctgChipPopup:CtgTypeChipPopup
	private lateinit var typeChipPopup:CtgTypeChipPopup

	private var color: String  by Delegates.observable(""){d,old,new->
		if(isVisible){
			paintThings()
            typeMainChip.chipBackgroundColor = ColorStateList.valueOf(getDarkColor(context!!,new))
            typeMainChip.setText(new)
		}
	}





	private var catagory: String by Delegates.observable("No Catagory"){d,old,new->
        if(isVisible){
            ctgMainChip.setText(new)

        }
    }




	private var colorPicker: ColorPickerClass? = null
	private var engTypes: Array<String> = arrayOf()
	private var localTypes: Array<String> = arrayOf()
	private var isEngLocal: Boolean = false
	private var localCtgs: Array<String> = arrayOf()
	private var engCtgs: Array<String> = arrayOf()

    private var currentCatagories: ThingObservalbeList? =null
    private var currentTypes: ThingObservalbeList? =null


	/**
	 * Here are the initial catagories and
	 */
	fun initialData(context: Context)
	{
		localCtgs = getStringArrayInLocale(context, "Catagories")!!
		if(LanguageSharedData(context).get_lang_code() == "en")
		{
			engCtgs = localCtgs;isEngLocal = true
		}
		else engCtgs = getStringArrayInLocale(context, "Catagories", "en")!!
		catagory = engCtgs[0]
		
		if(MiscSharedData(context).is_pos_neg_neu_allowed())
		{
			if(LanguageSharedData(context).get_lang_code() == "en") isEngLocal = true
			localTypes = listOf("positive", "negative", "neutral").map {
				getStringInLocale(context, it)!!
			}.toTypedArray()
			engTypes = if(isEngLocal) localTypes
			else listOf("positive", "negative", "neutral").map {
				getStringInLocale(context, it, "en")!!
			}.toTypedArray()
		}
		else
		{
			localTypes = getStringArrayInLocale(context, "colors_arr")!!
			if(LanguageSharedData(context).get_lang_code() == "en")
			{
				engTypes = localTypes;isEngLocal = true
			}
			else engTypes = getStringArrayInLocale(context, "colors_arr", "en")!!
		}
        currentCatagories= ThingObservalbeList({catagory = currentCatagories!!.get()[0];currentCatagories!!.clear()}
            ,{},localCtgs,engCtgs)
        currentTypes= ThingObservalbeList({color = currentTypes!!.get()[0];currentTypes!!.clear()}
            ,{},localTypes,engTypes)



		isEdit = false
	}
	
	private fun getTypeEngToLocale(ctg: String): String
	{
		if(isEngLocal) return ctg
		return localTypes[engTypes.indexOf(ctg)]
	}
	
	private fun getTypeLocaleToEng(ctg: String): String
	{
		if(isEngLocal) return ctg
		return engTypes[localTypes.indexOf(ctg)]
	}
	
	private fun getCtgEngToLocale(ctg: String): String
	{
		if(isEngLocal) return ctg
		return localCtgs[engCtgs.indexOf(ctg)]
	}
	
	private fun getCtgLocaleToEng(ctg: String): String
	{
		if(isEngLocal) return ctg
		return engCtgs[localCtgs.indexOf(ctg)]
	}
	
	private fun checkEntryValid(name: String, goal: Int): Boolean
	{
		return !(name.isEmpty() || goal <= 0)
	}
	
	companion object
	{
		fun newInstance() = AddThingFragment()
	}
	
	/**
	 * Here Starts The Normal Fragment Stuff
	 */
	private lateinit var viewModel: AddThingViewModel
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
	{
		val view = inflater.inflate(R.layout.add_thing_fragment, container, false)
		initialData(view.context)
		addbtn = view.findViewById(R.id.AddThingBtn)
		cancelbtn = view.findViewById(R.id.AddThingCancelBtn)
		plusbtn = view.findViewById(R.id.ThingPlusBtn)
		minusbtn = view.findViewById(R.id.ThingMinusBtn)
		detbtn = view.findViewById(R.id.MoreDetailsBtn)
		addmainlay = view.findViewById(R.id.add_main_layout)
		prog = view.findViewById(R.id.ThingProgressBar)
		prog2 = view.findViewById(R.id.ThingProgressBar2)
		colorPicker = ColorPickerClass(view)
		ctgMainChip = view.add_main_ctg_chip
		typeMainChip = view.add_main_type_chip




        ctgChipPopup = CtgTypeChipPopup(view.context,localCtgs.toList(),currentCatagories!!,isSingleChoice = true)
        typeChipPopup = CtgTypeChipPopup(view.context,localTypes.toList(),currentTypes!!,isType = true,isSingleChoice = true)

        ctgMainChip.setOnClickListener { ctgChipPopup.openPopup(ctgMainChip, mutableListOf(catagory)) }
        typeMainChip.setOnClickListener { typeChipPopup.openPopup(typeMainChip, mutableListOf(color)) }
		initBtns(view)

		return view
	}
	
	private fun initBtns(view: View)
	{
		fun showWarning()
		{
			val text = TextView(view.AddTableLayout.context)
			text.setText(getString(R.string.add_warning_msg))
			text.setTextColor(Color.RED)
			view.AddWarningLayout.apply {
				removeAllViewsInLayout()
				addView(text)
			}
		}
		
		addbtn.setOnClickListener {
			val name = AddThingNameTxt.text.toString().trim()
			val goal = AddGoadTxt.text.toString().toInt()
			if(!checkEntryValid(name, goal))
			{
				showWarning()
				return@setOnClickListener
			}
			val thng = view.run {
				Thing(name = name, catagory = catagory, type = color, goal = goal)
			}
			viewModel.insertThing(thng)
			dismiss()
		}
		cancelbtn.setOnClickListener {
			dismiss()
		}
	}
	

	
	override fun onActivityCreated(savedInstanceState: Bundle?)
	{
		super.onActivityCreated(savedInstanceState)
		viewModel = ViewModelProviders.of(this).get(AddThingViewModel::class.java).apply { setup(context!!) }
	}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        color = engTypes[0].toString()
        catagory = engCtgs[0]
        paintThings()
        ctgMainChip.setText(catagory)
        typeMainChip.setText(color)
    }
	fun makeInitialSchema(view: View){
	
	}
	
	
	fun paintThings(){

			try
			{
				val prime = getPrimColor(context!!,color)
				val dark = getDarkColor(context!!,color)
				val light = getLightColor(context!!,color)
				val trans = getTransparantColor(context!!,color)
				//val txtColor = if(isColorDark(prime)) Color.WHITE else Color.BLACK
				prime.apply { plusbtn.setBackgroundColor(this);minusbtn.setBackgroundColor(this);}
                prog.progressTintList = ColorStateList.valueOf(trans)
                prog2.progressTintList = ColorStateList.valueOf(trans)
               /* detbtn.setBackgroundColor(light);addmainlay.setBackgroundColor(trans)*/
				addmainlay.backgroundTintList = ColorStateList.valueOf(trans)
                addbtn.backgroundTintList = ColorStateList.valueOf(trans)
                cancelbtn.backgroundTintList = ColorStateList.valueOf(trans)



			}catch(e:Exception){
                Log.d("ADSA",e.message)
            }

	}
	/**
	 * Here is the ColorPickerClass
	 */
	private inner class ColorPickerClass(view: View)
	{
		
		var colorBtnsLst: List<ImageButton> = listOf()
		var colorBtnsLstDK: List<ImageButton> = listOf()
		val view: View? = null
		val previewTypeLst = listOf("Blue",
				"DarkBlue",
				"Yellow",
				"Red",
				"Brown",
				"Orange",
				"Pink",
				"Purple",
				"Gray",
				"Green")
		val previewDKTypeLst =
				listOf("DarkOrange", "DarkerBlue", "DarkRed", "DarkGreen", "Black", "MilitaryGreen", "Morasaki")
		var minusBtn: Button? = null
		var plusBtn: Button? = null
		var moreDetailsBtn: Button? = null
		var prog: ProgressBar? = null
		var nameTxt: TextView? = null
		var countTxt: TextView? = null
		var goalTxt: TextView? = null
		var slash: TextView? = null
		/*var disabledThingMainLay: View? = null*/
		
		init
		{
			minusBtn = view.ThingMinusBtn
			plusBtn = view.ThingPlusBtn
			moreDetailsBtn = view.MoreDetailsBtn
			prog = view.ThingProgressBar
			nameTxt = view.ThingGoalTxt
			countTxt = view.ThingCountTxt
			goalTxt = view.ThingGoalTxt
			slash = view.SlashTxt
			/*disabledThingMainLay = view.DisabledThingMainLayout*/
			
			
			/*if(!MiscSharedData(view.context).is_pos_neg_neu_allowed()){
				colorBtnsLst =
						loadImgBtns(view).apply { forEach { view.PickersColorsHolderLayout.addView(it) };get(0).callOnClick() }
				colorBtnsLstDK = loadDarkerBtns(view).apply { forEach { view.PickersColorsHolderLayoutDarker.addView(it) } }
			}*/
		}
		
		private fun loadImgBtns(view: View): List<ImageButton>
		{
			val context = view.context
			var index = 0
			val point = Point(0, 0)
			(context as MainActivity).windowManager.defaultDisplay.getSize(point)
			val w = point.x / 10
			val sampleEnabledThng = Thing(0, "", "", "", true, 0, 0, 0, 0, 0, SumHistory(0, 0, 0), 0)
			val sampleDisabledThng = Thing(0, "", "", "", false, 0, 0, 0, 0, 0, SumHistory(0, 0, 0), 0)
			return listOf(R.drawable.blue_round,
					R.drawable.dark_blue_round,
					R.drawable.yellow_round,
					R.drawable.red_round,
					R.drawable.brown_round,
					R.drawable.orange_round,
					R.drawable.pink_round,
					R.drawable.purple_round,
					R.drawable.gray_round,
					R.drawable.green_round).map {
				context.getDrawable(it).run {
					ImageButton(context).apply {
						this.contentDescription = previewTypeLst[index++]
						background = this@run
						layoutParams = ViewGroup.MarginLayoutParams(w - 5, w - 5).apply { setMargins(2, 10, 2, 10) }
						setOnClickListener {
							cloneThing(sampleEnabledThng).apply {
								paintThing(this)
								type = contentDescription.toString()
							}
							color = contentDescription.toString()
						}
					}
				}
			}
		}
		
		private fun loadDarkerBtns(view: View): List<ImageButton>
		{
			val context = view.context
			var index = 0
			val point = Point(0, 0)
			(context as MainActivity).windowManager.defaultDisplay.getSize(point)
			val w = point.x / 10
			val sampleEnabledThng = Thing(0, "", "", "", true, 0, 0, 0, 0, 0, SumHistory(0, 0, 0), 0)
			val sampleDisabledThng = Thing(0, "", "", "", false, 0, 0, 0, 0, 0, SumHistory(0, 0, 0), 0)
			
			return listOf(R.drawable.dark_orange_round,
					R.drawable.darker_blue_round,
					R.drawable.darker_red_round,
					R.drawable.dark_green_round,
					R.drawable.black_round,
					R.drawable.military_green_round,
					R.drawable.morasaki_round).map {
				context.getDrawable(it).run {
					ImageButton(context).apply {
						this.contentDescription = previewDKTypeLst[index++]
						background = this@run
						layoutParams = ViewGroup.MarginLayoutParams(w - 5, w - 5).apply { setMargins(4, 10, 4, 10) }
						setOnClickListener {
							cloneThing(sampleEnabledThng).apply {
								paintThing(this)
								type = contentDescription.toString()
							}
							color = contentDescription.toString()
						}
					}
				}
			}
		}
		
		private fun paintThing(thing: Thing)
		{
			/*disabledThingMainLay?.setBackgroundColor(getTransparantColor(plusBtn?.context!!, thing.type))*/
			minusBtn?.setBackgroundColor(getPrimColor(plusBtn?.context!!, thing.type))
			plusBtn?.setBackgroundColor(getDarkColor(plusBtn?.context!!, thing.type))
			moreDetailsBtn?.setBackgroundColor(getLightColor(plusBtn?.context!!, thing.type))
			prog?.progressDrawable?.setTint(getLightColor(plusBtn?.context!!, thing.type))
			val isDark = isColorDark(getPrimColor(plusBtn?.context!!, thing.type))
			val txtclr = if(isDark) Color.WHITE else Color.BLACK
			nameTxt?.setTextColor(Color.WHITE)
			countTxt?.setTextColor(txtclr)
			goalTxt?.setTextColor(txtclr)
			slash?.setTextColor(txtclr)
		}
	}
}
