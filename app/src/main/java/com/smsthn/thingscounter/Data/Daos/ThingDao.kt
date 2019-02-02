package com.smsthn.thingscounter.Data.Daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.smsthn.thingscounter.Data.Entities.Thing

@Dao
interface ThingDao{
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertThing(thing: Thing)
    @Insert
    fun insetManyThings(things:Collection<Thing>)
    @Update
    fun updateThing(thing: Thing)
    @Update
    fun updateManyThings(things: Collection<Thing>)
    @Query("UPDATE thing SET count= :count WHERE id IN (SELECT id FROM thing WHERE id = :id LIMIT 1)")
    fun updateThingCount(id:Long, count:Int)
    @Query("UPDATE thing SET count= goal WHERE enabled = 1 AND count> goal")
    fun setOvercountsToGoals()
    @Delete
    fun deleteThing(thing: Thing)
    @Query("DELETE FROM Thing")
    fun deleteAllThings()
    @Query("SELECT * FROM Thing")
    fun getAllThings():LiveData<List<Thing>>
    @Query("SELECT * FROM Thing WHERE type = :type")
    fun getThingsBasedOnType(type:String):LiveData<MutableList<Thing>>
    @Query("SELECT * FROM Thing WHERE catagory = :ctg")
    fun getThingsBasedOnCatagory(ctg:String):LiveData<MutableList<Thing>>
    @Query("SELECT * FROM Thing ORDER BY type,name")
    fun getAllThingsSortedByName():LiveData<MutableList<Thing>>
    @Query("SELECT * FROM Thing WHERE enabled = 1 ORDER BY type,name")
    fun getAllEnabledThingsSortedByName():LiveData<MutableList<Thing>>
    @Query("SELECT * FROM Thing WHERE enabled = 0 ORDER BY type,name")
    fun getAllDisabledThingsSortedByName():LiveData<MutableList<Thing>>
    @Query("SELECT * FROM Thing ORDER BY creation_date DESC")
    fun getAllThingsSortedByDate():LiveData<MutableList<Thing>>
    @Query("SELECT * FROM thing WHERE id = :id")
    fun getOneThing(id:Long): Thing
    @Query("SELECT * FROM thing WHERE id = :id")
    fun getOneThingLive(id:Long): LiveData<Thing>
    @Query("SELECT COUNT(*) FROM Thing WHERE count>= goal AND type = :type")
    fun getNumThingWhereCountBiggerThanGoal(type: String):Int
    @Query("SELECT * FROM Thing")
    fun getAllThingsNotLiveData():MutableList<Thing>
    @Query("SELECT * FROM Thing WHERE enabled = 1")
    fun getAllEnabledThingsNotLiveData():MutableList<Thing>
    @Query("SELECT * FROM Thing WHERE enabled = 0")
    fun getAllDisabledThingsNotLiveData():MutableList<Thing>
    @Query ("SELECT type , COUNT(id) AS total,SUM(count) AS countsum,SUM(goal) AS goalsum,SUM(CASE WHEN count >= goal THEN 1 ELSE 0 END) as completed  FROM thing WHERE enabled = 1 GROUP BY type")
    fun getTypesAndCountsLive():LiveData<List<TypeAndCount>>
    @Query ("SELECT type , COUNT(id) AS total,SUM(count) AS countsum,SUM(goal) AS goalsum,SUM(CASE WHEN count >= goal THEN 1 ELSE 0 END) as completed  FROM thing WHERE enabled = 1 GROUP BY type")
    fun getTypesAndCountsNotLive():List<TypeAndCount>
}
data class TypeAndCount(var type:String,var total:Int= 0,var countsum:Int= 0,var goalsum:Int= 0,var completed:Int= 0)

@Dao
interface ThingDaoPosNegNeu: ThingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insertThing(thing: Thing)
    @Insert
    override fun insetManyThings(things:Collection<Thing>)
    @Update
    override fun updateThing(thing: Thing)
    @Update
    override fun updateManyThings(things: Collection<Thing>)
    @Query("UPDATE thing SET count= :count WHERE id IN (SELECT id FROM thing WHERE id = :id LIMIT 1)")
    override fun updateThingCount(id:Long, count:Int)
    @Query("UPDATE thing SET count= goal WHERE enabled = 1 AND count> goal AND type IN ('Positive','Negative','Neutral')")
    override fun setOvercountsToGoals()
    @Delete
    override fun deleteThing(thing: Thing)
    @Query("DELETE FROM Thing WHERE type IN ('Positive','Negative','Neutral')")
    override fun deleteAllThings()
    @Query("SELECT * FROM Thing WHERE type IN ('Positive','Negative','Neutral')")
    override fun getAllThings():LiveData<List<Thing>>
    @Query("SELECT * FROM Thing WHERE type = :type AND type IN ('Positive','Negative','Neutral')")
    override fun getThingsBasedOnType(type:String):LiveData<MutableList<Thing>>
    @Query("SELECT * FROM Thing WHERE catagory = :ctg AND type IN ('Positive','Negative','Neutral')")
    override fun getThingsBasedOnCatagory(ctg:String):LiveData<MutableList<Thing>>
    @Query("SELECT * FROM Thing WHERE type IN ('Positive','Negative','Neutral') ORDER BY type,name")
    override fun getAllThingsSortedByName():LiveData<MutableList<Thing>>
    @Query("SELECT * FROM Thing WHERE enabled = 1 AND type IN ('Positive','Negative','Neutral') ORDER BY type,name")
    override fun getAllEnabledThingsSortedByName():LiveData<MutableList<Thing>>
    @Query("SELECT * FROM Thing WHERE enabled = 0 AND type IN ('Positive','Negative','Neutral') ORDER BY type,name")
    override fun getAllDisabledThingsSortedByName():LiveData<MutableList<Thing>>
    @Query("SELECT * FROM Thing WHERE type IN ('Positive','Negative','Neutral') ORDER BY creation_date DESC")
    override fun getAllThingsSortedByDate():LiveData<MutableList<Thing>>
    @Query("SELECT * FROM thing WHERE id = :id AND type IN ('Positive','Negative','Neutral')")
    override fun getOneThing(id:Long): Thing
    @Query("SELECT * FROM thing WHERE id = :id")
    override fun getOneThingLive(id:Long): LiveData<Thing>
    @Query("SELECT COUNT(*) FROM Thing WHERE count >= goal AND type = :type AND type IN ('Positive','Negative','Neutral')")
    override fun getNumThingWhereCountBiggerThanGoal(type: String):Int
    @Query("SELECT * FROM Thing WHERE type IN ('Positive','Negative','Neutral')")
    override fun getAllThingsNotLiveData():MutableList<Thing>
    @Query("SELECT * FROM Thing WHERE enabled = 1 AND type IN ('Positive','Negative','Neutral')")
    override fun getAllEnabledThingsNotLiveData():MutableList<Thing>
    @Query("SELECT * FROM Thing WHERE enabled = 0 AND type IN ('Positive','Negative','Neutral')")
    override fun getAllDisabledThingsNotLiveData():MutableList<Thing>
    @Query ("SELECT type , COUNT(id) AS total,SUM(count) AS countsum,SUM(goal) AS goalsum,SUM(CASE WHEN count >= goal THEN 1 ELSE 0 END) as completed  FROM thing WHERE type IN ('Positive','Negative','Neutral') AND enabled = 1 GROUP BY type ")
    override fun getTypesAndCountsLive():LiveData<List<TypeAndCount>>
    @Query ("SELECT type , COUNT(id) AS total,SUM(count) AS countsum,SUM(goal) AS goalsum,SUM(CASE WHEN count >= goal THEN 1 ELSE 0 END) as completed  FROM thing WHERE type IN ('Positive','Negative','Neutral') AND enabled = 1 GROUP BY type ")
    override fun getTypesAndCountsNotLive():List<TypeAndCount>
}

@Dao
interface ThingDao_NotPosNegNeu: ThingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insertThing(thing: Thing)
    @Insert
    override fun insetManyThings(things:Collection<Thing>)
    @Update
    override fun updateThing(thing: Thing)
    @Update
    override fun updateManyThings(things: Collection<Thing>)
    @Query("UPDATE thing SET count = :count WHERE id IN (SELECT id FROM thing WHERE id = :id LIMIT 1)")
    override fun updateThingCount(id:Long, count:Int)
    @Query("UPDATE thing SET count = goal WHERE enabled = 1 AND count > goal AND type NOT IN ('Positive','Negative','Neutral')")
    override fun setOvercountsToGoals()
    @Delete
    override fun deleteThing(thing: Thing)
    @Query("DELETE FROM Thing WHERE type NOT IN ('Positive','Negative','Neutral')")
    override fun deleteAllThings()
    @Query("SELECT * FROM Thing WHERE type NOT IN ('Positive','Negative','Neutral')")
    override fun getAllThings():LiveData<List<Thing>>
    @Query("SELECT * FROM Thing WHERE type = :type AND type NOT IN ('Positive','Negative','Neutral')")
    override fun getThingsBasedOnType(type:String):LiveData<MutableList<Thing>>
    @Query("SELECT * FROM Thing WHERE catagory = :ctg AND type NOT IN ('Positive','Negative','Neutral')")
    override fun getThingsBasedOnCatagory(ctg:String):LiveData<MutableList<Thing>>
    @Query("SELECT * FROM Thing WHERE type NOT IN ('Positive','Negative','Neutral') ORDER BY type,name")
    override fun getAllThingsSortedByName():LiveData<MutableList<Thing>>
    @Query("SELECT * FROM Thing WHERE enabled = 1 AND type NOT IN ('Positive','Negative','Neutral') ORDER BY type,name")
    override fun getAllEnabledThingsSortedByName():LiveData<MutableList<Thing>>
    @Query("SELECT * FROM Thing WHERE enabled = 0 AND type NOT IN ('Positive','Negative','Neutral') ORDER BY type,name")
    override fun getAllDisabledThingsSortedByName():LiveData<MutableList<Thing>>
    @Query("SELECT * FROM Thing WHERE type NOT IN ('Positive','Negative','Neutral') ORDER BY creation_date DESC")
    override fun getAllThingsSortedByDate():LiveData<MutableList<Thing>>
    @Query("SELECT * FROM thing WHERE id = :id AND type NOT IN ('Positive','Negative','Neutral')")
    override fun getOneThing(id:Long): Thing
    @Query("SELECT * FROM thing WHERE id = :id")
    override fun getOneThingLive(id:Long): LiveData<Thing>
    @Query("SELECT COUNT(*) FROM Thing WHERE count>= goal AND type = :type AND type NOT IN ('Positive','Negative','Neutral')")
    override fun getNumThingWhereCountBiggerThanGoal(type: String):Int
    @Query("SELECT * FROM Thing WHERE type NOT IN ('Positive','Negative','Neutral')")
    override fun getAllThingsNotLiveData():MutableList<Thing>
    @Query("SELECT * FROM Thing WHERE enabled = 1 AND type NOT IN ('Positive','Negative','Neutral')")
    override fun getAllEnabledThingsNotLiveData():MutableList<Thing>
    @Query("SELECT * FROM Thing WHERE enabled = 0 AND type NOT IN ('Positive','Negative','Neutral')")
    override fun getAllDisabledThingsNotLiveData():MutableList<Thing>
    @Query ("SELECT type , COUNT(id) AS total,SUM(count) AS countsum,SUM(goal) AS goalsum,SUM(CASE WHEN count >= goal THEN 1 ELSE 0 END) as completed  FROM thing WHERE type NOT IN ('Positive','Negative','Neutral') AND enabled = 1 GROUP BY type ")
    override fun getTypesAndCountsLive():LiveData<List<TypeAndCount>>
    @Query ("SELECT type , COUNT(id) AS total,SUM(count) AS countsum,SUM(goal) AS goalsum,SUM(CASE WHEN count >= goal THEN 1 ELSE 0 END) as completed  FROM thing WHERE type NOT IN ('Positive','Negative','Neutral') AND enabled = 1 GROUP BY type")
    override fun getTypesAndCountsNotLive():List<TypeAndCount>
}
