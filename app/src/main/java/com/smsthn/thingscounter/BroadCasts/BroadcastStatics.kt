package com.smsthn.thingscounter.BroadCasts

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.smsthn.thingscounter.CustomViews.CustomStyles.getDarkColor
import com.smsthn.thingscounter.R
import java.util.*

//TODO : FIX FOR NOTPOSNEGNEU
const val DAILY_REM_REQ_CODE=9695
const val TAG="Notification_Scheduler"

fun setRepeatedPendingIntent(context: Context, c:Class<*>, h:Int, m:Int, requestCode:Int = DAILY_REM_REQ_CODE, intentAction:String = ""){
    val cal = Calendar.getInstance()
    val cal2 = Calendar.getInstance()
    cal2.set(Calendar.HOUR_OF_DAY, h)
    cal2.set(Calendar.MINUTE, m)
    cal2.set(Calendar.SECOND, 0)
    cancelRepeatedPendingIntent(context, c)
    if(cal2.before(cal))cal2.add(Calendar.DATE,1)

    val receiver = ComponentName(context, c)
    val pm = context.packageManager

    pm.setComponentEnabledSetting(
        receiver,
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
        PackageManager.DONT_KILL_APP
    )


    val intent1 = Intent(context, c)
    if(requestCode != DAILY_REM_REQ_CODE){
        intent1.setAction(intentAction)
    }

    val pendingIntent =
        PendingIntent.getBroadcast(context, requestCode, intent1, PendingIntent.FLAG_UPDATE_CURRENT)

    val am = context.getSystemService(ALARM_SERVICE) as AlarmManager
    am.setInexactRepeating(
        AlarmManager.RTC_WAKEUP,
        cal2.getTimeInMillis(),
        AlarmManager.INTERVAL_DAY,
        pendingIntent
    )



}


fun cancelRepeatedPendingIntent(context: Context, c: Class<*>,requestCode: Int = DAILY_REM_REQ_CODE,intentAction: String = "") {

    val receiver = ComponentName(context, c)
    val pm = context.packageManager
    try {
    pm.setComponentEnabledSetting(
        receiver,
        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
        PackageManager.DONT_KILL_APP
    )

    val intent1 = Intent(context, c).apply { if(!intentAction.isNullOrBlank())action = intentAction }
    val pendingIntent =
        PendingIntent.getBroadcast(context,
            requestCode, intent1, PendingIntent.FLAG_UPDATE_CURRENT)
    val am = context.getSystemService(ALARM_SERVICE) as AlarmManager
    am.cancel(pendingIntent)

        pendingIntent.cancel()
    } catch (e:Exception){}
}

fun showNotification(context: Context,c:Class<*>,title:String,content:String,data:Array<String>,ongoing:Boolean = false,isPosNegNeu:Boolean = true,intData:Array<Int> = arrayOf(0)){



    val notView:RemoteViews
    if(isPosNegNeu){
        if(ongoing){
            notView = RemoteViews(context.packageName,R.layout.custom_notification_layout_ongoing)
            notView.setTextViewText(R.id.OngoingNotificationPositiveSum,data[0])
            notView.setTextViewText(R.id.OngoingNotificationNegativeSum,data[1])
            notView.setTextViewText(R.id.OngoingNotificationNeutralSum,data[2])

        } else{
            notView = RemoteViews(context.packageName,R.layout.custom_notification_layout)
            notView.setTextViewText(R.id.NotificationPositiveSum,data[0])
            notView.setTextViewText(R.id.NotificationNegativeSum,data[1])
            notView.setTextViewText(R.id.NotificationNeutralSum,data[2])
        }
    } else {
        notView = getNotPosNegNeuViews(context, intData, data) ?:return
    }


    val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val intent = Intent(context,c)
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

    val taskStackBuilder = TaskStackBuilder.create(context)
    taskStackBuilder.addParentStack(c)
    taskStackBuilder.addNextIntent(intent)

    val pIntent = taskStackBuilder.getPendingIntent(DAILY_REM_REQ_CODE,PendingIntent.FLAG_UPDATE_CURRENT)

    val notManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notBuilder:NotificationCompat.Builder
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelId = "THINGS_COUNTER_CHANNEL_ID"
        val channel = NotificationChannel(channelId,
            "Things Counter",
            NotificationManager.IMPORTANCE_LOW)
        notManager.createNotificationChannel(channel)
        notBuilder = NotificationCompat.Builder(context,channelId)
    } else{
        notBuilder = NotificationCompat.Builder(context)
    }
    val notification = notBuilder.setContentTitle(title).setContentText(content).setAutoCancel(true).setSmallIcon(
        R.mipmap.ic_launcher_round).setOngoing(ongoing).setContentIntent(pIntent).setCustomContentView(notView).build()
        notManager.notify(DAILY_REM_REQ_CODE,notification)
}
fun cancelNotification(context: Context){
    (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(DAILY_REM_REQ_CODE)
}

/**
 * data will have the qualified names of the types and it's length will determin how many views will be added.
 * while intdata has 2 datas for each string the first will be the progress and the second is the max
 */
private fun getNotPosNegNeuViews(context: Context,intData:Array<Int>,data:Array<String>):RemoteViews?{
    if(data.isNullOrEmpty())return null
    val view = RemoteViews(context.packageName,R.layout.custom_notification_layout_ongoing_pos_neg_neu)
    for(i in 0..data.size-1){
        val prog = intData[(i*2)]
        val max = intData[(i*2)+1]
        val prid = arrayOf(R.id.NotProgBar1,R.id.NotProgBar2,R.id.NotProgBar3)[i]
        view.setProgressBar(prid,max,prog,false)
        view.setInt(prid,"setBackgroundColor",(getDarkColor(context,data[i])))
        view.setViewVisibility(prid, View.VISIBLE)

    }
    if(data.size < 3){
        for(i in 2.downTo(data.size )){
            val prid = arrayOf(R.id.NotProgBar1,R.id.NotProgBar2,R.id.NotProgBar3)[i]
            view.setViewVisibility(prid, View.GONE)
        }
    }
    return view

}


