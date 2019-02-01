package com.smsthn.thingscounter.Fragments

import android.content.DialogInterface
import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.razerdp.widget.animatedpieview.AnimatedPieView
import com.smsthn.thingscounter.CustomViews.Charts.makePie
import com.smsthn.thingscounter.CustomViews.CustomStyles.getPrimColor
import com.smsthn.thingscounter.Data.Entities.PrevHistories
import com.smsthn.thingscounter.Fragments.ViewModels.ChartsViewModel
import com.smsthn.thingscounter.R

class ChartsFragment : BottomSheetDialogFragment()  {
	val args:ChartsFragmentArgs by navArgs()
	private lateinit var poscounts:TextView
	private lateinit var posgoals:TextView
	private lateinit var poscompleted:TextView
	private lateinit var postotal:TextView
	private lateinit var negcounts:TextView
	private lateinit var neggoals:TextView
	private lateinit var negcompleted:TextView
	private lateinit var negtotal:TextView
	private lateinit var neucounts:TextView
	private lateinit var neugoals:TextView
	private lateinit var neucompleted:TextView
	private lateinit var neutotal:TextView
	private lateinit var posPie:AnimatedPieView
	private lateinit var negPie:AnimatedPieView
	private lateinit var neuPie:AnimatedPieView
	private lateinit var allPie:AnimatedPieView
	
	
	
    companion object {
        fun newInstance() = ChartsFragment()
    }

    private lateinit var viewModel: ChartsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.charts_fragment, container, false)
	    posPie = view.findViewById(R.id.PosPieView)
	    negPie = view.findViewById(R.id.NegPieView)
	    neuPie = view.findViewById(R.id.NeuPieView)
	    allPie = view.findViewById(R.id.AllPieView)
	
	    poscounts = view.findViewById(R.id.Prev_His_Pos_Count)
	    posgoals = view.findViewById(R.id.Prev_His_Pos_Goal)
	    poscompleted = view.findViewById(R.id.Prev_His_Pos_Completed)
	    postotal = view.findViewById(R.id.Prev_His_Pos_Total_Num)
	    negcounts = view.findViewById(R.id.Prev_His_Neg_Count)
	    neggoals = view.findViewById(R.id.Prev_His_Neg_Goal)
	    negcompleted = view.findViewById(R.id.Prev_His_Neg_Completed)
	    negtotal = view.findViewById(R.id.Prev_His_Neg_Total_Num)
	    neucounts = view.findViewById(R.id.Prev_His_Neu_Count)
	    neugoals = view.findViewById(R.id.Prev_His_Neu_Goal)
	    neucompleted = view.findViewById(R.id.Prev_His_Neu_Completed)
	    neutotal = view.findViewById(R.id.Prev_His_Neu_Total_Number)
	    
	    return view
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ChartsViewModel::class.java).apply {
            initThis(activity!!.application,"All")
            prevHisLive?.observe(this@ChartsFragment, object : Observer<PrevHistories>
            {
                override fun onChanged(t: PrevHistories?)
                {
                    t?.apply {

                        poscounts.setText(""+posCounts);posgoals.setText(""+posGoals)
                        poscompleted.setText(""+posCompleteds); postotal.setText(""+posTotals)

                        negcounts.setText(""+negCounts); neggoals.setText(""+negGoals)
                        negcompleted.setText(""+negCompleteds);negtotal.setText(""+negTotals)

                        neucounts.setText(""+neuCounts); neugoals.setText(""+neuGoals)
                        neucompleted.setText(""+neuCompleteds);neutotal.setText(""+neuTotals)
                        val names2 = resources.getString(R.string.counts_goals).split("/",limit = 2).toTypedArray()
                        val clrs = arrayOf("Positive","Negative","Neutral","Black").map {
                            getPrimColor(context!!,it)
                        }.toTypedArray()
                        makePie(posPie, arrayOf(posCompleteds,posTotals), names2, arrayOf(clrs[0], Color.BLACK),true,14)
                        makePie(negPie, arrayOf(negCompleteds,negTotals), names2, arrayOf(clrs[1], Color.BLACK),true,14)
                        makePie(neuPie, arrayOf(neuCompleteds,neuTotals), names2, arrayOf(clrs[2], Color.BLACK),true,14)
                        val allcmpl = posCompleteds+negCompleteds+negCompleteds
                        val alltotal = posTotals+negTotals+neuTotals
                        val pos = if(posTotals == 0)0 else ((posCompleteds.toDouble()/ posTotals.toDouble())*100).toInt()
                        val neg = if(negTotals == 0)0 else ((negCompleteds.toDouble()/ negTotals.toDouble())*100).toInt()
                        val neu = if(neuTotals == 0)0 else ((neuCompleteds.toDouble()/ neuTotals.toDouble())*100).toInt()
                        val all = if(alltotal == 0)0 else ((allcmpl.toDouble() / alltotal.toDouble())*100).toInt()



                        makePie(allPie, arrayOf(pos,neg,neu,100-all), arrayOf("Positive","Negative","Neutral","Uncompleted"), clrs,false,18)
                        view!!.invalidate()
                    }
                }
            })
        }

        
    }

}
