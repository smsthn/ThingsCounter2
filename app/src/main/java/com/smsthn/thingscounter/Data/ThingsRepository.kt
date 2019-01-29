package com.smsthn.thingscounter.Data

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import com.smsthn.thingscounter.Data.Daos.*
import com.smsthn.thingscounter.Data.Entities.OneHistory
import com.smsthn.thingscounter.Data.Entities.PrevHistories
import com.smsthn.thingscounter.Data.Entities.Thing
import com.smsthn.thingscounter.Data.Entities.cloneThing
import com.smsthn.thingscounter.SharedData.MiscSharedData
import java.util.*


val primCatagories: List<String> = listOf("Fun", "Food", "Family", "Friend", "Health", "Addiction")

class ThingsRepository(application: Application) {
    private var thngDao: ThingDao
    private val oneHistoryDao: OneHistoryDao
    private val allCtgDao: CatagoryDao
    private val prevHisDao: PrevHistoriesDao
    private val db: ThingsDb?


    init {
        db = ThingsDb.getAppDataBase(application)
        if (db == null) throw Exception("CouldNotOpenDb")
        thngDao = if(MiscSharedData(application).is_pos_neg_neu_allowed())db.posNegNeuThingDao()
                    else db.NotPosNegNeuThingDao()
        oneHistoryDao = db.oneHistoryDao()
        allCtgDao = db.allCatagoriesDao()
        prevHisDao = db.prevHistoriesDao()


    }

    fun getOneThingHistory(thing: Thing): List<OneHistory> {
        return oneHistoryDao.getAllHistoryOf(thing.id)
    }

    fun addThing(thing: Thing) {
        doAsyncThing(thngDao::insertThing).execute(thing)
    }

    fun addManyThings(things: Collection<Thing>) {
        AsyncTask.execute {
            thngDao.insetManyThings(things)
        }
    }

    fun addManyOneHistory(histories: Collection<OneHistory>) {
        AsyncTask.execute {
            oneHistoryDao.addManyOneHistory(histories)
        }
    }

    fun addOneHistory(oneHistory: OneHistory) {
        AsyncTask.execute { oneHistoryDao.addOneHistory(oneHistory) }
    }

    fun getCountsGoalsCompletedTotalFromOneHis(thingId: Long): SumOfOneHis? {
        return oneHistoryDao.getCountsGoalsCompletedTotalFromOneHisNotLive(thingId)
    }

    private fun substractStuffFromPrevHis(prevHistories: PrevHistories, oneHisSums: SumOfOneHis, thing: Thing) {
        prevHistories.apply {
            when (thing.type) {
                "Positive" -> {
                    posCounts -= oneHisSums.countsum
                    posGoals -= oneHisSums.goalsum
                    posCompleteds -= oneHisSums.completed
                    posTotals -= oneHisSums.total
                }
                "Negative" -> {
                    negCounts -= oneHisSums.countsum
                    negGoals -= oneHisSums.goalsum
                    negCompleteds -= oneHisSums.completed
                    negTotals -= oneHisSums.total
                }
                "Neutral" -> {
                    neuCounts -= oneHisSums.countsum
                    neuGoals -= oneHisSums.goalsum
                    neuCompleteds -= oneHisSums.completed
                    neuTotals -= oneHisSums.total
                }
            }
        }
    }

    fun resetHisAndStats(thing: Thing, alsoDeleteThing: Boolean) {
        AsyncTask.execute {
            val oneHisSums: SumOfOneHis = oneHistoryDao.getAllHistoryOf(thing.id).let {
                val sumHis = SumOfOneHis(0, 0, 0, 0)
                it.forEach { sumHis.completed+=if (it.count>it.goal)1 else 0
                sumHis.total++;sumHis.countsum+=it.count;sumHis.goalsum+=it.goal
                }
                sumHis
            }
            oneHisSums
            val allPrevHis = prevHisDao.getPrevHisofCtgAllNotLive()
            val specPrevHis = prevHisDao.getSpecificCtgPrevHisNotLive(thing.catagory)
            try {
                allPrevHis!!.apply { substractStuffFromPrevHis(this, oneHisSums, thing) }
            } catch (e: NullPointerException) {
                Log.e(
                    "resetHisAndStats OF ${thing.name}",
                    "ALL CTG FROM PREVHIS WAS NOT FOUND"
                );throw e
            }
            specPrevHis?.apply { substractStuffFromPrevHis(this, oneHisSums, thing) }

            if (alsoDeleteThing){
                db?:throw Exception("CouldNotOpenDb")
                db.runInTransaction {
                    thngDao.deleteThing(thing)
                    prevHisDao.apply { updatePrevHis(allPrevHis);specPrevHis?:return@apply;updatePrevHis(specPrevHis) }
                }

            }
            else {
                db?:throw Exception("CouldNotOpenDb")
                db.runInTransaction {
                    thngDao.updateThing(cloneThing(thing).apply { resetEveryThingAndStartOver() })
                     (oneHistoryDao.deleteWhereThingId(thingId = thing.id))
                    prevHisDao.updatePrevHis(allPrevHis)
                    specPrevHis?:return@runInTransaction
                    prevHisDao.updatePrevHis(specPrevHis)
                }

            }

        }
    }

    fun endThingCycle(thing: Thing) {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        val onhs = OneHistory(
            thingId = thing.id,
            cycleNum = thing.currentCycle,
            cycleFreq = thing.repeatFreq,
            count = thing.count,
            goal = thing.goal,
            date = cal.timeInMillis
        )
        thing.resetThing()
        AsyncTask.execute { thngDao.updateThing(thing) }
        AsyncTask.execute { oneHistoryDao.addOneHistory(onhs) }
    }

    fun deleteThing(thing: Thing) {
        doAsyncThing(thngDao::deleteThing).execute(thing)
    }

    fun deleteAllThings() {
        AsyncTask.execute { thngDao.deleteAllThings() }
    }

    fun setOvercountsToGoals() {
        AsyncTask.execute { thngDao.setOvercountsToGoals() }
    }

    fun resetThing(thing: Thing) {
        val runnable = Runnable {
            val thng = cloneThing(thing).apply { resetEveryThingAndStartOver() }
            db?:throw Exception("CouldNotOpenDb")
            db.runInTransaction {
                updateThing(thng)
                oneHistoryDao.deleteWhereThingId(thingId = thing.id)
            }

        }
        AsyncTask.execute(runnable)
    }

    fun resetCurrentThing(thing: Thing) {
        thing.resetCurrentCycle()
        doAsyncThing(thngDao::updateThing).execute(thing)
    }

    fun updateThing(thing: Thing) {
        doAsyncThing(thngDao::updateThing).execute(thing)
    }

    fun updateManyThings(things: Collection<Thing>) {
        AsyncTask.execute {
            thngDao.updateManyThings(things)
        }
    }

    fun updateThingCount(id: Long, count: Int) {
        AsyncTask.execute { thngDao.updateThingCount(id, count) }
    }

    fun addCtg(ctg: String) {

        AsyncTask.execute {
            allCtgDao.insetCatagory(Catagoty(ctg = ctg))
        }
    }

    fun getPreHisAll(): LiveData<PrevHistories> {
        return prevHisDao.getPrevHisForAll()
    }

    fun getPreHisForCtg(catagory: String): LiveData<PrevHistories> {
        return prevHisDao.getPrevHisForCatagory(catagory)
    }


    /*fun switchToPosNegNeu(){
        db?:throw Exception("CouldNotOpenDb")
        thngDao = db.posNegNeuThingDao()
        enabledThings = thngDao.getAllEnabledThingsSortedByName()
        disabledThings = thngDao.getAllDisabledThingsSortedByName()
        typesAndCounts = thngDao.getTypesAndCountsLive()
    }
    fun switchToNotPosNegNeu(){
        db?:throw Exception("CouldNotOpenDb")
        thngDao = db.NotPosNegNeuThingDao()
        enabledThings = thngDao.getAllEnabledThingsSortedByName()
        disabledThings = thngDao.getAllDisabledThingsSortedByName()
        typesAndCounts = thngDao.getTypesAndCountsLive()
    }*/

}

class doAsyncThing(val handler: (Thing) -> Unit) : AsyncTask<Thing, Unit, Unit>() {
    override fun doInBackground(vararg params: Thing?): Unit {
        handler(params[0]!!)
    }

}

