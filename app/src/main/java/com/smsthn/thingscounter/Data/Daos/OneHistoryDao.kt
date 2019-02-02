package com.smsthn.thingscounter.Data.Daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.smsthn.thingscounter.Data.Entities.OneHistory

@Dao
interface OneHistoryDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOneHistory(oneHistory: OneHistory)
    @Insert
    fun addManyOneHistory(histories:Collection<OneHistory>)
    @Update
    fun updateHistory(oneHistory: OneHistory)
    @Delete
    fun deleteHistory(oneHistory: OneHistory)
    @Query("DELETE FROM OneHistory WHERE thing_id = :thingId")
    fun deleteWhereThingId(thingId: Long)
    @Query("SELECT * FROM OneHistory WHERE thing_id = :thingId ORDER BY date")
    fun getAllHistoryOf(thingId:Long):List<OneHistory>
    @Query("SELECT * FROM OneHistory WHERE thing_id = :thingId ORDER BY date")
    fun getAllHistoryOfAsLive(thingId:Long):LiveData<List<OneHistory>>
    @Query("SELECT SUM(countTxt) as countsum,SUM(goalTxt) as goalsum,SUM(CASE WHEN countTxt >= goalTxt THEN 1 ELSE 0 END) as completed,COUNT(id) as total FROM onehistory WHERE thing_id = :thingId")
    fun getCountsGoalsCompletedTotalFromOneHisNotLive(thingId: Long): SumOfOneHis?
    @Query("DELETE FROM onehistory")
    fun deleteAllOneHistory()
}


class SumOfOneHis(var countsum:Int,var goalsum:Int, var completed:Int, var total:Int)