package com.smsthn.thingscounter.Data.Entities

import androidx.room.ColumnInfo
import androidx.versionedparcelable.VersionedParcelize

@VersionedParcelize
data class SumHistory(
    @ColumnInfo(name = "cycle_count")
    var cycleCount:Int = 0,
    @ColumnInfo(name = "sum_count")
    var sumCount:Int = 0,
    @ColumnInfo(name = "sum_goal")
    var sumGoal:Int

)