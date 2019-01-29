package com.smsthn.thingscounter.Data.Entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.versionedparcelable.VersionedParcelize

@VersionedParcelize
@Entity(tableName = "Previous_Histories",indices = [Index("id","catagory")])
data class PrevHistories (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id:Long = 0,
    @ColumnInfo(name = "catagory")
    @NonNull
    val ctg:String,
    @ColumnInfo(name = "positive_counts")
    var posCounts:Int = 0,
    @ColumnInfo(name = "negative_counts")
    var negCounts:Int = 0,
    @ColumnInfo(name = "neutral_counts")
    var neuCounts:Int = 0,
    @ColumnInfo(name = "positive_goals")
    var posGoals:Int = 0,
    @ColumnInfo(name = "negative_goals")
    var negGoals:Int = 0,
    @ColumnInfo(name = "neutral_goals")
    var neuGoals:Int = 0,
    @ColumnInfo(name = "positive_completed")
    var posCompleteds:Int = 0,
    @ColumnInfo(name = "negative_completed")
    var negCompleteds:Int = 0,
    @ColumnInfo(name = "neutral_completed")
    var neuCompleteds:Int = 0,
    @ColumnInfo(name = "positive_total")
    var posTotals:Int = 0,
    @ColumnInfo(name = "negative_total")
    var negTotals:Int = 0,
    @ColumnInfo(name = "neutral_total")
    var neuTotals:Int = 0
)