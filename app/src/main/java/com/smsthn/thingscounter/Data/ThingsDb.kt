package com.smsthn.thingscounter.Data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.smsthn.thingscounter.Data.Entities.OneHistory
import com.smsthn.thingscounter.Data.Entities.Thing
import androidx.sqlite.db.SupportSQLiteDatabase
import com.smsthn.thingscounter.Data.Daos.*
import com.smsthn.thingscounter.Data.Entities.PrevHistories
import com.smsthn.thingscounter.R


@Database(entities = [Thing::class, OneHistory::class, Catagoty::class, PrevHistories::class],version = 1,exportSchema = true)
abstract class ThingsDb:RoomDatabase(){
    abstract fun thingDao(): ThingDao
    abstract fun posNegNeuThingDao(): ThingDaoPosNegNeu
    abstract fun NotPosNegNeuThingDao(): ThingDao_NotPosNegNeu
    abstract fun oneHistoryDao(): OneHistoryDao
    abstract fun allCatagoriesDao(): CatagoryDao
    abstract fun prevHistoriesDao(): PrevHistoriesDao
    init {

    }
    companion object {
        var INSTANCE: ThingsDb? = null

        fun getAppDataBase(context: Context): ThingsDb? {
            if (INSTANCE == null){
                synchronized(ThingsDb::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, ThingsDb::class.java, "ThingsDb")
                        .addCallback(object :RoomDatabase.Callback(){

                            override fun onOpen(db: SupportSQLiteDatabase) {
                                super.onOpen(db)
                                val cv1 = ContentValues()
                                cv1.apply {
                                    put("id",0)
                                    put("catagory","All")
                                    put("positive_counts",0)
                                    put("negative_counts",0)
                                    put("neutral_counts",0)
                                    put("positive_goals",0)
                                    put("negative_goals",0)
                                    put("neutral_goals",0)
                                    put("positive_completed",0)
                                    put("negative_completed",0)
                                    put("neutral_completed",0)
                                    put("positive_total",0)
                                    put("negative_total",0)
                                    put("neutral_total",0)

                                }
                                db.insert("Previous_Histories",SQLiteDatabase.CONFLICT_IGNORE, cv1)
                                var i = 1
                                for(ctg in context.resources.getStringArray(R.array.Catagories)){
                                    val prevhs = PrevHistories(ctg = ctg)
                                    val cv = ContentValues()
                                    cv.apply {
                                        put("id",i++)
                                        put("catagory",ctg)
                                        put("positive_counts",0)
                                        put("negative_counts",0)
                                        put("neutral_counts",0)
                                        put("positive_goals",0)
                                        put("negative_goals",0)
                                        put("neutral_goals",0)
                                        put("positive_completed",0)
                                        put("negative_completed",0)
                                        put("neutral_completed",0)
                                        put("positive_total",0)
                                        put("negative_total",0)
                                        put("neutral_total",0)
                                        db.insert("Previous_Histories",SQLiteDatabase.CONFLICT_IGNORE,cv)
                                    }
                                }

                            }
                        })
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}
/*val migration = object :Migration(2,3){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS 'Previous_Histories' (" +
                "id INTEGER NOT NULL PRIMARY KEY," +
                "catagory VARCHAR(250) NOT NULL," +
                "positive_counts INTEGER NOT NULL," +
                "negative_counts INTEGER NOT NULL," +
                "neutral_counts INTEGER NOT NULL," +
                "positive_goals INTEGER NOT NULL," +
                "negative_goals INTEGER NOT NULL," +
                "neutral_goals INTEGER NOT NULL," +
                "positive_completed  INTEGER NOT NULL," +
                "negative_completed  INTEGER NOT NULL," +
                "neutral_completed  INTEGER NOT NULL," +
                "positive_total INTEGER NOT NULL," +
                "negative_total INTEGER NOT NULL," +
                "neutral_total INTEGER NOT NULL"+
                ")")
        database.execSQL("CREATE INDEX index_OneHistory_thing_id ON  OneHistory(thing_id)")
        database.execSQL("CREATE INDEX index_Previous_Histories_id_catagory ON  Previous_Histories(id,catagory)")
    }

}*/
