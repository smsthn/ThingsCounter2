package com.smsthn.thingscounter.CustomViews.Popups

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.view.MarginLayoutParamsCompat
import androidx.core.view.children
import androidx.core.view.marginEnd
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.resources.TextAppearance
import com.smsthn.thingscounter.Fragments.ThingObservalbeList

class CtgChipsPopup(context: Context, ctgs:Collection<String>,checkedCtgs: ThingObservalbeList){
    val popup:PopupWindow
    val layout:LinearLayout
    val chipGroup:ChipGroup

    init {
        layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(15,15,15,15)
        }
        chipGroup = ChipGroup(ContextThemeWrapper(context,android.R.style.Theme_Material_Light)).apply {
            isSingleSelection = false
            for (ctg in ctgs){
                val chip = Chip((ContextThemeWrapper(context,android.R.style.Theme_Material_Light))).apply {
                    isCheckable = true
                    setTextAppearanceResource(android.R.style.TextAppearance_Material)
                    setText(ctg)
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

    }
    fun openPopup(view: View,checkedCtgs: MutableList<String>){
        chipGroup.children.forEach {
            (it as Chip).isChecked = checkedCtgs.contains(it.text.toString())
        }
        popup.showAsDropDown(view)
    }
}