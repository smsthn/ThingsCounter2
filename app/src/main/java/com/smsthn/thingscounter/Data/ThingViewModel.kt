/*
package com.smsthn.thingscounter.Data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.smsthn.thingscounter.Data.Daos.SumOfOneHis
import com.smsthn.thingscounter.Data.Daos.TypeAndCount
import com.smsthn.thingscounter.Data.Entities.OneHistory
import com.smsthn.thingscounter.Data.Entities.PrevHistories
import com.smsthn.thingscounter.Data.Entities.Thing

class ThingViewModel(application: Application):AndroidViewModel(application){
    private val thingsRepository: ThingsRepository

    //val thngData:LiveData<MutableList<Thing>>
    var enabledThngs:LiveData<MutableList<Thing>>
    var disabledThngs:LiveData<MutableList<Thing>>
    val allctg:LiveData<MutableList<Catagoty>>
    var typesAndCounts:LiveData<List<TypeAndCount>>
    init {
        thingsRepository = ThingsRepository(application)
        enabledThngs = thingsRepository.enabledThings
        disabledThngs = thingsRepository.disabledThings
        allctg = thingsRepository.allCatagories
        typesAndCounts = thingsRepository.typesAndCounts
    }
    fun getOneThingHistory(thing: Thing):List<OneHistory>{
        return thingsRepository.getOneThingHistory(thing)
    }
    fun getCountsGoalsCompletedTotalFromOneHis(thingId:Long): SumOfOneHis? {
        return thingsRepository.getCountsGoalsCompletedTotalFromOneHis(thingId)
    }
    fun addThing(thing: Thing){
        thingsRepository.addThing(thing)
    }
    fun addManyThings(things: Collection<Thing>){
        thingsRepository.addManyThings(things)
    }
    fun addManyOneHistory(histories:Collection<OneHistory>){
        thingsRepository.addManyOneHistory(histories)
    }
    fun addOneHistory(oneHistory: OneHistory){
        thingsRepository.addOneHistory(oneHistory)
    }
    fun endThingCycle(thing: Thing){
        thingsRepository.endThingCycle(thing)
    }
    fun deleteThing(thing: Thing){
        thingsRepository.deleteThing(thing)
    }
    fun deleteAllThings(){
        thingsRepository.deleteAllThings()
    }
    fun setOvercountsToGoals(){
        thingsRepository.setOvercountsToGoals()
    }
    fun resetThing(thing: Thing){
        thingsRepository.resetThing(thing)
    }
    fun resetHisAndStats(thing: Thing, alsoDeleteThing:Boolean){
        thingsRepository.resetHisAndStats(thing,alsoDeleteThing)
    }
    fun resetCurrentThing(thing: Thing){
        thingsRepository.resetCurrentThing(thing)
    }
    fun updateThing(thing: Thing){
        thingsRepository.updateThing(thing)
    }
    fun updateManyThings(things: Collection<Thing>){

        thingsRepository.updateManyThings(things)

    }
    fun updateThingCount(id:Long, count:Int){
        thingsRepository.updateThingCount(id,count)
    }
    fun addCtg(ctg:String){
        thingsRepository.addCtg(ctg)
    }
    fun getPreHisAll():LiveData<PrevHistories>{
        return thingsRepository.getPreHisAll()
    }
    fun getPreHisForCtg(catagory:String):LiveData<PrevHistories>{
        return thingsRepository.getPreHisForCtg(catagory)
    }
    fun switchPosNegNeu(isPosNegNeu:Boolean = true){
        if(isPosNegNeu)thingsRepository.switchToPosNegNeu()
        else thingsRepository.switchToNotPosNegNeu()
        enabledThngs = thingsRepository.enabledThings
        disabledThngs = thingsRepository.disabledThings
        typesAndCounts = thingsRepository.typesAndCounts
    }
}*/
