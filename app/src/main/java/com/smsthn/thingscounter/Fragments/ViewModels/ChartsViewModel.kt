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
    private lateinit var db:ThingsDb

    lateinit var prevHistoriesDao: PrevHistoriesDao
    init {
    
    }
    fun initThis(application: Application,ctg:String){
	    ThingsDb.getAppDataBase(application)?.apply {
            db = this
		    prevHistoriesDao = prevHistoriesDao().apply {

				    prevHisLive = getPrevHisForCatagory(ctg)

		    }
	    } ?: throw NullPointerException("ThingViewModel db Was null")
    }
    fun resetAllPrevHistories(){
        db.runInTransaction {
            db.prevHistoriesDao().resetAllPrevHis()
            db.oneHistoryDao().deleteAllOneHistory()
        }
    }
}
