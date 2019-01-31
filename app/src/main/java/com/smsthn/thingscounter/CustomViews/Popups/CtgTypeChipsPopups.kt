package com.smsthn.thingscounter.CustomViews.Popups

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.view.children
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.smsthn.thingscounter.CustomViews.CustomStyles.getDarkColor
import com.smsthn.thingscounter.CustomViews.CustomStyles.getTransparantColor
import com.smsthn.thingscounter.Fragments.ThingObservalbeList
import com.smsthn.thingscounter.R

class CtgChipsPopup(context: Context, lst:Collection<String>, checkedCtgs: ThingObservalbeList, resetTouchFunc :()->Unit, isType:Boolean = false){
    val popup:PopupWindow
    val layout:LinearLayout
    val chipGroup:ChipGroup

    init {
        val infl = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layout = infl.inflate(R.layout.mat_lin_lay,null) as LinearLayout
        chipGroup = ChipGroup(layout.context).apply {
            chipSpacingHorizontal = 50
            isSingleSelection = false
            for (item in lst){
                val chip = Chip((layout.context)).apply {
                    isCheckable = true
                    setTextAppearanceResource(android.R.style.TextAppearance_Material)
                    setText(item)
                    setTextColor(Color.BLACK)
                    if(isType)chipBackgroundColor = ColorStateList.valueOf(getDarkColor(context,item))
                    textSize = 18f
                    layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                        setMargins(15,15,15,15)

                    }
                    this.closeIconSize = 50f
                    this.chipIconSize = 50f

                    setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
                       val c= this.text.toString()
                        //TODO FIX IS CHECK NOT BEING RECOGNIZED
                        if(!checkedCtgs.contains(c))checkedCtgs.add(c)
                        else if(checkedCtgs.contains(c))checkedCtgs.remove(c)
                        Log.d("DID STH TO CATAGORY $c","NOW THE LIST IS $checkedCtgs")
                    }
                }
                this.addView(chip)
            }
            setOnCheckedChangeListener { chipGroup, i ->

            }
        }
        layout.addView(chipGroup)

        popup = PopupWindow(layout,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        popup.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        popup.elevation = 10f
        popup.isOutsideTouchable = true

        popup.setOnDismissListener {
           Handler(Looper.getMainLooper()).postDelayed(
               {resetTouchFunc.invoke()}
           ,100)
        }


    }
    fun openPopup(view: View,checkedCtgs: MutableList<String>){
        chipGroup.children.forEach {
            (it as Chip).isChecked = checkedCtgs.contains(it.text.toString())
        }
        /*((popup.contentView.context as ContextThemeWrapper).baseContext  as  MainActivity).showShadow()*/
        popup.showAsDropDown(view)
    }
}