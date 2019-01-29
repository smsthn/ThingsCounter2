package com.smsthn.thingscounter.Fragments.ViewModels

import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import com.smsthn.thingscounter.Data.Daos.ThingDao
import com.smsthn.thingscounter.Data.Entities.Thing
import com.smsthn.thingscounter.Data.ThingsDb

class AddThingViewModel : ViewModel()
{
	private var db: ThingsDb? = null
	private var thingDao: ThingDao? = null
	var thing:Thing = Thing(name = "",catagory = "",goal = 0,type = "")
	
	
	fun setup(ctx: Context)
	{
		db = ThingsDb.getAppDataBase(ctx) ?: throw Exception("Could Not OpenDatabase")
		thingDao = db!!.thingDao()
	}
	fun insertThing(thing: Thing){
		AsyncTask.execute {
			thingDao?.insertThing(thing)?:Handler(Looper.getMainLooper()).post {
				Log.e("AddThingViewModel",":insertThing :Something went wrong could not add thing please try again")
			}
		}
	}
	/*fun updateThing(thing: Thing){
		AsyncTask.execute {
			thingDao?.updateThing(thing)
		}
	}*/
	
	override fun onCleared()
	{
		super.onCleared()
		thing = Thing(name = "",catagory = "",goal = 0,type = "")
		db = null
		thingDao = null
	}
}
