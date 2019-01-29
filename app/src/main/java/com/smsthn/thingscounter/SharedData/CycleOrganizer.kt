package com.smsthn.thingscounter.SharedData

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.os.Handler
import com.smsthn.thingscounter.BroadCasts.ThingBroadcastReciever
import com.smsthn.thingscounter.BroadCasts.setRepeatedPendingIntent
import com.smsthn.thingscounter.Data.Entities.OneHistory
import com.smsthn.thingscounter.Data.ThingsDb
import com.smsthn.thingscounter.MainActivity
import com.smsthn.thingscounter.Data.Entities.PrevHistories
import com.smsthn.thingscounter.Data.Entities.Thing
import java.util.*


const val THING_CYCLE_INTENT_ACTION = "THING_CYCLE_INTENT_ACTION"
const val THING_CYCLE_REQ_CODE = 9392


fun initCycleOrganizerBroadCast(context: Context,h:Int,m:Int = 0){
    setRepeatedPendingIntent(
        context,
        ThingBroadcastReciever::class.java,
        h,
        m,
        THING_CYCLE_REQ_CODE,
        THING_CYCLE_INTENT_ACTION
    )
}
fun resetThingsAndAddCycle(context: Context,tries:Int = 0){
    val tr = tries
    AsyncTask.execute {
        val db = ThingsDb.getAppDataBase(context)
        if(db != null){
            fun updatePreHis(prevHistories: PrevHistories, thing: Thing){
                prevHistories.apply {
                    when(thing.type){
                        "Positive"->{
                            posCounts += thing.count
                            posGoals += thing.goal
                            posTotals++
                            posCompleteds+=if(thing.goal<=thing.count) 1 else 0
                        }
                        "Negative"->{
                            negCounts += thing.count
                            negGoals += thing.goal
                            negTotals++
                            negCompleteds+=if(thing.goal<=thing.count) 1 else 0
                        }
                        "Neutral"->{
                            neuCounts += thing.count
                            neuGoals += thing.goal
                            neuTotals++
                            neuCompleteds+=if(thing.goal<=thing.count) 1 else 0
                        }
                    }
                }
            }
            val thingDao = db.thingDao()
            val oneHisDao = db.oneHistoryDao()
            val prevHisDao = db.prevHistoriesDao()
            val thngs = thingDao.getAllThingsNotLiveData()
            val prevhiss = prevHisDao.getAllPrevHissLstNotLive()
            val allprevhs = prevhiss.firstOrNull{ it.ctg == "All" }
            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE,-1)
            for(thing in thngs){
                val onhs = OneHistory(
                    thingId = thing.id,
                    cycleNum = thing.currentCycle,
                    cycleFreq = thing.repeatFreq,
                    count = thing.count,
                    goal = thing.goal,
                    date = cal.timeInMillis
                )

                val ph = prevhiss.firstOrNull { it.ctg == thing.catagory }.apply {
                    if (this == null)return@apply
                    updatePreHis(this,thing)
                }
                updatePreHis(allprevhs!!,thing)
                thing.resetThing()
                db.runInTransaction {
                    thingDao.updateThing(thing)
                    oneHisDao.addOneHistory(onhs)
                    prevHisDao.updatePrevHis(allprevhs)
                    ph?:return@runInTransaction prevHisDao.updatePrevHis(ph!!)
                }

            }
            if(context as? MainActivity? != null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    context.mainExecutor.execute {
                        (context as Activity).recreate()
                    }
                } else{
                    Handler(context.mainLooper).post {
                        (context as Activity).recreate()
                    }
                }

            }
        } else {
            if(tr < 30){
                Thread.sleep(10000)
                resetThingsAndAddCycle(context,tr+1)
            }
        }
    }
}



