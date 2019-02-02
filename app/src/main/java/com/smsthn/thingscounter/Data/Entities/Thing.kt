package com.smsthn.thingscounter.Data.Entities

import androidx.room.*
import androidx.versionedparcelable.VersionedParcelize
import java.util.*


@VersionedParcelize
@Entity(tableName = "Thing",indices = [Index(value = ["id","name","type"])])
class Thing constructor(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    @ColumnInfo(name = "type")
    var type:String,
    @ColumnInfo(name = "name")
    var name:String,
    @ColumnInfo(name = "catagory")
    var catagory:String,
    @ColumnInfo(name = "enabled")
    var enabled:Boolean = true,
    count:Int = 0,
    @ColumnInfo(name = "goal")
    var goal:Int,
    @ColumnInfo(name = "repeat_freq")
    var repeatFreq:Int = 1,
    @ColumnInfo(name = "creation_date")
    var creationDate:Long = 0,
    @ColumnInfo(name = "cycles_completed")
    var currentCycle:Int = 1,
    @Embedded
    val sumHistory: SumHistory = SumHistory(
        sumGoal = goal
    ),
    @ColumnInfo(name = "currentDate")
    var currentDate:Long = 0){
    @ColumnInfo(name = "count")
    var count:Int = count
        set(value) {
            if(value < 0)field = 0
            else field = value
        }
    init {


        if(creationDate == 0.toLong()){

            creationDate = getCurrentDayAtNullHour()
        }
        if(currentDate == 0.toLong()){
            currentDate = creationDate
        }
    }
    fun resetThing(){
        sumHistory.sumCount+=count
        sumHistory.total++
        if(count >= goal)sumHistory.completed++
        sumHistory.sumGoal+=goal
        count = 0
        currentCycle++
        currentDate = getCurrentDayAtNullHour()
    }
    fun resetCurrentCycle(){
        count = 0
        currentDate = getCurrentDayAtNullHour()
    }
    fun resetEveryThingAndStartOver(){
        count = 0
        creationDate = getCurrentDayAtNullHour()
        currentDate = creationDate
        currentCycle = 1

    }
    private fun getCurrentDayAtNullHour():Long{
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY))
        cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE))
        cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND))
        cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND))
        return cal.timeInMillis
    }
    fun compareToThing(thing: Thing):Boolean{
        val n = name == thing.name
        val c = catagory == thing.catagory
        val t = type == thing.type
        val co = count == thing.count
        val go = goal == thing.goal
        val re = repeatFreq == thing.repeatFreq
        val cc = currentCycle == thing.currentCycle
        val shc = sumHistory.sumCount == thing.sumHistory.sumCount
        val shg = sumHistory.sumGoal == thing.sumHistory.sumGoal
        val sht = sumHistory.total == thing.sumHistory.total
        val shcmpl = sumHistory.completed == thing.sumHistory.completed
        return n && c  && t && co && go && re  &&cc  && shc  && shg  && sht && shcmpl
    }


}
fun cloneThing(thing: Thing): Thing {
    return Thing(
        id = thing.id,
        name = thing.name,
        count = thing.count,
        creationDate = thing.creationDate,
        goal = thing.goal,
        currentCycle = thing.currentCycle,
        catagory = thing.catagory,
        type = thing.type,
        repeatFreq = thing.repeatFreq,
        currentDate = thing.currentDate,
        sumHistory = SumHistory(
            total = thing.sumHistory.total,
            completed = thing.sumHistory.completed,
            sumCount = thing.sumHistory.sumGoal,
            sumGoal = thing.sumHistory.sumGoal
        )
    )
}