package com.smsthn.thingscounter.Data.Daos

import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.smsthn.thingscounter.Data.Entities.PrevHistories


@Dao
interface PrevHistoriesDao{
    @Insert(onConflict = SQLiteDatabase.CONFLICT_ABORT)
    fun addCtgToPrevHis(prevHistories: PrevHistories)
    @Update
    fun updatePrevHis(prevHistories: PrevHistories)
    @Query("UPDATE previous_histories SET positive_counts = positive_counts - :poscounts,positive_goals = positive_goals - :posgoals, positive_completed = positive_completed - :poscompleted, positive_total = positive_total - :postotal WHERE catagory = :ctg")
    fun substractfromPosOfCth(poscounts:Int,posgoals:Int,poscompleted:Int,postotal:Int,ctg:String)
	@Query("UPDATE previous_histories SET negative_counts = negative_counts - :negcounts,negative_goals = negative_goals - :neggoals, negative_completed = negative_completed - :negcompleted, negative_total = negative_total - :negtotal WHERE catagory = :ctg")
	fun substractfromNegOfCth(negcounts:Int,neggoals:Int,negcompleted:Int,negtotal:Int,ctg:String)
	@Query("UPDATE previous_histories SET neutral_counts = neutral_counts - :neucounts,neutral_goals = neutral_goals - :neugoals, neutral_completed = neutral_completed - :neucompleted, neutral_total = neutral_total - :neutotal WHERE catagory = :ctg")
	fun substractfromNeuOfCth(neucounts:Int,neugoals:Int,neucompleted:Int,neutotal:Int,ctg:String)
    @Query("SELECT * FROM previous_histories WHERE catagory = 'All'")
    fun getPrevHisForAll():LiveData<PrevHistories>
    @Query("SELECT * FROM previous_histories WHERE catagory = :catagory")
    fun getPrevHisForCatagory(catagory:String):LiveData<PrevHistories>
    @Query("SELECT * FROM previous_histories")
    fun getAllPrevHissLstNotLive():List<PrevHistories>
    @Query("SELECT * FROM previous_histories WHERE catagory =:ctg LIMIT 1")
    fun getSpecificCtgPrevHisNotLive(ctg:String): PrevHistories?
    @Query("SELECT * FROM previous_histories WHERE catagory = 'All' LIMIT 1")
    fun getPrevHisofCtgAllNotLive(): PrevHistories?
	@Query("SELECT * FROM previous_histories WHERE catagory = 'All' LIMIT 1")
	fun getPrevHisofCtgAllLive(): LiveData<List<PrevHistories>>
    @Query("UPDATE previous_histories SET positive_counts = 0,positive_goals = 0,positive_completed = 0,positive_total = 0,negative_counts = 0,negative_goals = 0,negative_completed = 0,negative_total = 0,neutral_counts = 0,neutral_goals = 0,neutral_completed = 0,neutral_total = 0")
    fun resetAllPrevHis()
    /*@Query("SELECT * FROM previous_histories")
    fun deleteValuesFromAll()
    @Query("SELECT * FROM previous_histories")
    fun deleteValuesFromCtg()*/
}