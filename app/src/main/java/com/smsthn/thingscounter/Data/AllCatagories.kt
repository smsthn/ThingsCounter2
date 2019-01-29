package com.smsthn.thingscounter.Data

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "AllCatagories")
class Catagoty(@ColumnInfo(name = "id")@PrimaryKey(autoGenerate = true)val id:Long = 0, @ColumnInfo(name ="ctg")val ctg:String)

@Dao
interface CatagoryDao{
    @Insert
    fun insetCatagory(catagoty: Catagoty)
    @Query("SELECT * FROM AllCatagories ORDER BY ctg")
    fun getAllCatagories():LiveData<MutableList<Catagoty>>
    @Query ("SELECT COUNT(*) FROM AllCatagories")
    fun getCtgCount():LiveData<Int>
}