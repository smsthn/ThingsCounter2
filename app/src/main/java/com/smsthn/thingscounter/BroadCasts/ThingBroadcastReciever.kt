package com.smsthn.thingscounter.BroadCasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.smsthn.thingscounter.Data.Entities.Thing
import com.smsthn.thingscounter.Data.ThingsDb
import com.smsthn.thingscounter.MainActivity
import com.smsthn.thingscounter.SharedData.*

//TODO FIX FOR NOTPOSNEGNEU
class ThingBroadcastReciever : BroadcastReceiver() {
    protected val Tag = "Alarm_Reciever"


    override fun onReceive(context: Context?, intent: Intent?) {
        var notificationSharedData: NotificationSharedData? = null
        var ongoing = false
        var sumCountsGoals = false
        var cycleSharedData: CycleSharedData? = null
        if (context != null && intent != null) {
            if (intent.action != null) {
                if (intent.action.equals(THING_CYCLE_INTENT_ACTION, true)) {
                    resetThingsAndAddCycle(context)
                    return
                }

                if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED, true)) {
                    notificationSharedData = NotificationSharedData(context)
                    ongoing = notificationSharedData.get_ongoing()
                    sumCountsGoals = notificationSharedData.get_data_Type() == 1
                    if (!ongoing) {
                        setRepeatedPendingIntent(
                            context,
                            ThingBroadcastReciever::class.java,
                            notificationSharedData.get_hour(),
                            notificationSharedData.get_min()
                        )
                    }
                    cycleSharedData = CycleSharedData(context)
                    initCycleOrganizerBroadCast(context, cycleSharedData.get_hour(), cycleSharedData.get_minute())
                }
            }
            if (notificationSharedData == null) {
                notificationSharedData = NotificationSharedData(context)
                ongoing = notificationSharedData.get_ongoing()
                sumCountsGoals = notificationSharedData.get_data_Type() == 1
            }
            val runnable = Runnable {
                val db = ThingsDb.getAppDataBase(context)
                if (db != null) {
                    val misc = MiscSharedData(context)
                    val posNegNeu = misc.is_pos_neg_neu_allowed()
                    if (posNegNeu) {
                        makeNotificationPosNegNeu(
                            context,
                            db,
                            ongoing,
                            sumCountsGoals
                        )
                    } else {
                        makeNotificationNotPosNegNeu(
                            context,
                            db,
                            ongoing,
                            notificationSharedData
                        )
                    }
                }
            }
            AsyncTask.execute(runnable)
        }
    }
}

fun makeNotificationPosNegNeu(context: Context, db: ThingsDb?, ongoing: Boolean, sumCountsGoals: Boolean) {
    val data = db?.posNegNeuThingDao()?.getAllThingsNotLiveData()
    if (data == null) {
        Log.e("makeNotification", "PosNegNeu Db Null");return
    }
    showNotification(
        context,
        MainActivity::class.java,
        "sadaa",
        "dadsa",
        getSums(data, sumCountsGoals),
        ongoing
    )
}

fun makeNotificationNotPosNegNeu(context: Context,
                                 db: ThingsDb,
                                 ongoing: Boolean,
                                 notificationSharedData: NotificationSharedData
) {
    val data = db.NotPosNegNeuThingDao().getTypesAndCountsNotLive().take(3)
    if (data.isNullOrEmpty()) {
        if ((context as? MainActivity?) != null)
            if (!context.isDestroyed) Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, "There are Currently No Things To Show The Count", Toast.LENGTH_SHORT).show()
                notificationSharedData.apply { set_allow_nots(false) }
                /*if ((context as? MainActivity?) != null) {
                    if (!context.isDestroyed) {
                        if (context.nav_view.checkedItem?.itemId == R.id.OptionNav) {
                            context.findViewById<Switch>(R.id.AllowNotificationSwitch)?.isChecked = false
                            context.findViewById<View>(R.id.NotTypeTableRow)?.visibility = View.GONE
                            context.findViewById<View>(R.id.NotTimeTableRow)?.visibility = View.GONE
                        }
                    }
                }*/
        }

        cancelNotification(context)
        cancelRepeatedPendingIntent(context, BroadcastReceiver::class.java)

    }else {
        val index = 0
        val intData = Array(data.size * 2) { v ->
            if (v % 2 == 0) data[(v / 2)].countsum
            else data[v / 2].goalsum
        }
        val strData = Array(data.size) { v -> data[v].type }
        showNotification(
            context,
            MainActivity::class.java,
            "sadaa",
            "dadsa",
            strData,
            ongoing,
            false,
            intData
        )
    }
}

fun buildNotificationGeneral(context: Context) {
    AsyncTask.execute {
        NotificationSharedData(context).apply {
            if (get_allow_nots()) {
                val db = ThingsDb.getAppDataBase(context)
                if (db != null) {
                    val ongoing = get_ongoing()
                    if(!ongoing){
                        cancelNotification(context)
                        setRepeatedPendingIntent(context,ThingBroadcastReciever::class.java,get_hour(),get_min())
                        return@execute
                    }
                    val sumCountsGoals = get_data_Type() == 1
                    val posNegNeu = MiscSharedData(context).is_pos_neg_neu_allowed()
                    if (posNegNeu) {
                        makeNotificationPosNegNeu(context, db, ongoing, sumCountsGoals)
                    } else {
                        makeNotificationNotPosNegNeu(context, db, ongoing, this)
                    }
                }
            }
            else {
                cancelNotification(context)
                cancelRepeatedPendingIntent(context,ThingBroadcastReciever::class.java)
            }
        }
    }
}

fun getSums(lst: MutableList<Thing>, allItems: Boolean = false): Array<String> {
    var poscompleted = 0
    var posnotCompleted = 0
    var postotal = 0
    var postotalSum = 0
    var postotalGoals = 0
    var negcompleted = 0
    var negnotCompleted = 0
    var negtotal = 0
    var negtotalSum = 0
    var negtotalGoals = 0
    var neucompleted = 0
    var neunotCompleted = 0
    var neutotal = 0
    var neutotalSum = 0
    var neutotalGoals = 0
    for (thing in lst) {
        if (thing.type.trim().toLowerCase() == "positive") {
            postotal++
            postotalSum += thing.count
            postotalGoals += thing.goal
            if (thing.count >= thing.goal) poscompleted++
            else posnotCompleted++
        } else if (thing.type.trim().toLowerCase() == "negative") {
            negtotal++
            negtotalSum += thing.count
            negtotalGoals += thing.goal
            if (thing.count >= thing.goal) negcompleted++
            else negnotCompleted++
        } else if (thing.type.trim().toLowerCase() == "neutral") {
            neutotal++
            neutotalSum += thing.count
            neutotalGoals += thing.goal
            if (thing.count >= thing.goal) neucompleted++
            else neunotCompleted++
        }
    }
    if (!allItems) {
        val pos = "" + poscompleted + "/" + postotal
        val neg = "" + negcompleted + "/" + negtotal
        val neu = "" + neucompleted + "/" + neutotal
        return arrayOf(pos, neg, neu)
    } else {
        val pos = "" + postotalSum + "/" + postotalGoals
        val neg = "" + negtotalSum + "/" + negtotalGoals
        val neu = "" + neutotalSum + "/" + neutotalGoals
        return arrayOf(pos, neg, neu)
    }
}