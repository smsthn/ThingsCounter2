package com.smsthn.thingscounter.Data.Entities

import androidx.room.*
import androidx.versionedparcelable.VersionedParcelize

@VersionedParcelize
@Entity(tableName = "OneHistory",indices = [Index("thing_id")],foreignKeys = [ForeignKey(entity = Thing::class,
        parentColumns = ["id"], childColumns = ["thing_id"],onDelete = ForeignKey.CASCADE,onUpdate = ForeignKey.NO_ACTION)])
data class OneHistory(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    @ColumnInfo(name = "thing_id")
    val thingId:Long = 0,
    @ColumnInfo(name = "cycle_num")
    var cycleNum:Int,
    @ColumnInfo(name = "cycle_freq")
    var cycleFreq:Int,
    @ColumnInfo(name = "countTxt")
    var count:Int,
    @ColumnInfo(name = "goalTxt")
    var goal:Int,
    @ColumnInfo(name = "date")
    val date:Long = 0
)