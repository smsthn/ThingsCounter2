package com.smsthn.thingscounter.Fragments.ViewModels

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.smsthn.thingscounter.Data.Daos.PrevHistoriesDao
import com.smsthn.thingscounter.Data.Entities.PrevHistories
import com.smsthn.thingscounter.Data.ThingsDb

class ChartsViewModel : ViewModel() {
    var prevHisLive:LiveData<PrevHistories>? = null


    lateinit var prevHistoriesDao: PrevHistoriesDao
    init {
    
    }
    fun initThis(application: Application,ctg:String){
	    ThingsDb.getAppDataBase(application)?.apply {
		    prevHistoriesDao = prevHistoriesDao().apply {

				    prevHisLive = getPrevHisForCatagory(ctg)

		    }
	    } ?: throw NullPointerException("ThingViewModel db Was null")
    }
}
