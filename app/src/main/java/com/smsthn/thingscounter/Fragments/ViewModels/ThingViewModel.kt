package com.smsthn.thingscounter.Fragments.ViewModels

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.smsthn.thingscounter.Data.Daos.OneHistoryDao
import com.smsthn.thingscounter.Data.Daos.PrevHistoriesDao
import com.smsthn.thingscounter.Data.Daos.ThingDao
import com.smsthn.thingscounter.Data.Daos.TypeAndCount
import com.smsthn.thingscounter.Data.Entities.Thing
import com.smsthn.thingscounter.Data.ThingsDb

class ThingViewModel : ViewModel()
{
	private lateinit var thingDao: ThingDao
	private lateinit var oneHisDao: OneHistoryDao
	private lateinit var prevHisDao: PrevHistoriesDao
	lateinit var enabledThngs: LiveData<MutableList<Thing>>
	lateinit var disabledThngs: LiveData<MutableList<Thing>>
	lateinit var typesAndCounts: LiveData<List<TypeAndCount>>
	private lateinit var db: ThingsDb
	
	init
	{
	}
	
	fun initViewModel(application: Application, isPosNegNeu: Boolean = true)
	{
		db = ThingsDb.getAppDataBase(application) ?: throw NullPointerException("ThingViewModel db Was null")
		db.apply {
			oneHisDao = oneHistoryDao()
			prevHisDao = prevHistoriesDao()
			thingDao = (if(isPosNegNeu) posNegNeuThingDao() else NotPosNegNeuThingDao()).apply {
				enabledThngs = getAllEnabledThingsSortedByName()
				disabledThngs = getAllDisabledThingsSortedByName()
				typesAndCounts = getTypesAndCountsLive()
			}
		}
	}
	
	fun updateThingCount(id: Long, count: Int)
	{
		AsyncTask.execute { thingDao.updateThingCount(id, count) }
	}
	
	fun deleteThing(thing: Thing, withPrevHis: Boolean = false)
	{
		AsyncTask.execute {
			if(withPrevHis)
			{
				erasePrevHis(thing)
			}
			thingDao.deleteThing(thing)
		}
		
	}
	fun resetOneHis(thing: Thing, withPrevHis: Boolean = false)
	{
		AsyncTask.execute {
			if(withPrevHis)
			{
				erasePrevHis(thing)
			}
			oneHisDao.deleteWhereThingId(thing.id)
		}
	}
	
	fun updateThing(thing: Thing)
	{
		AsyncTask.execute {
			thingDao.updateThing(thing)
		}
	}
	
	private fun erasePrevHis(thing: Thing)
	{
		oneHisDao.getCountsGoalsCompletedTotalFromOneHisNotLive(thing.id)?.apply {
			when(thing.type)
			{
				"Positive" ->
				{
					prevHisDao.substractfromPosOfCth(countsum,
							goalsum,
							completed,
							total,
							thing.catagory)
                    prevHisDao.substractfromPosOfCth(countsum, goalsum, completed, total, "All")
				}
				"Negative" ->
				{
					prevHisDao.substractfromNegOfCth(countsum,
							goalsum,
							completed,
							total,
							thing.catagory)
                    prevHisDao.substractfromPosOfCth(countsum,
						goalsum, completed, total, "All")
				}
				"Neutral"  ->
				{
					prevHisDao.substractfromNeuOfCth(countsum,
							goalsum,
							completed,
							total,
							thing.catagory)
                    prevHisDao.substractfromPosOfCth(countsum,
						goalsum, completed, total, "All")
				}
			}
		}
	}
}

