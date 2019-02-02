package com.smsthn.thingscounter

import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.widget.TimePicker
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.smsthn.thingscounter.BroadCasts.ThingBroadcastReciever
import com.smsthn.thingscounter.BroadCasts.buildNotificationGeneral
import com.smsthn.thingscounter.BroadCasts.cancelRepeatedPendingIntent
import com.smsthn.thingscounter.CustomViews.CustomStyles.getLightColor
import com.smsthn.thingscounter.CustomViews.CustomStyles.getPrimColor
import com.smsthn.thingscounter.CustomViews.CustomStyles.getStringArrayInLocale
import com.smsthn.thingscounter.CustomViews.Dialogs.buildAlertDialog
import com.smsthn.thingscounter.CustomViews.Dialogs.buildYesNoDialog
import com.smsthn.thingscounter.Data.ThingsDb
import com.smsthn.thingscounter.Fragments.TimePickerReason
import com.smsthn.thingscounter.SharedData.*


class ThingPreferenceFragment : PreferenceFragmentCompat() {
    private lateinit var lang: LanguageSharedData
    private lateinit var misc: MiscSharedData
    private lateinit var cycle: CycleSharedData
    private lateinit var notif: NotificationSharedData

    private lateinit var timepickerLsr: ThingsOnTimeSetListener
    private lateinit var timePickerDialog: TimePickerDialog

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        lang = LanguageSharedData(context!!)
        misc = MiscSharedData(context)
        cycle = CycleSharedData(context)
        notif = NotificationSharedData(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val langs = getStringArrayInLocale(context!!, "Catagories", "en")
        view!!.setBackgroundColor(getLightColor(context!!,"Black"))

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_notification, rootKey)

        (findPreference(getString(R.string.settings_allow_notificaitons_key))as SwitchPreference).apply {
            isChecked = notif.get_allow_nots()
            setOnPreferenceChangeListener { preference, newValue ->
                notif.set_allow_nots(newValue as Boolean)
                buildNotificationGeneral(context!!)
                true
            }
        }
        (findPreference(getString(R.string.settings_notoficaiton_ongoing_key)) as SwitchPreference).apply {
            isChecked = notif.get_ongoing()
            val time = (findPreference(getString(R.string.setting_notification_time_key)) as Preference)
            time.isEnabled = !notif.get_ongoing()
            setOnPreferenceChangeListener { preference, newValue ->
                notif.set_ongoing(newValue as Boolean)
                buildNotificationGeneral(context!!)
                time.isEnabled = !(newValue as Boolean)
                true
            }
        }

        (findPreference(getString(R.string.stng_posnegneu_key)) as ListPreference).apply {
            setValueIndex(
                if( MiscSharedData(context!!).is_pos_neg_neu_allowed())0 else 1
            )
            summary = value
            this.setOnPreferenceChangeListener { p, new ->
                when (new) {
                    getString(R.string.positive_negative_neutral) -> MiscSharedData(context!!).set_pos_neg_neu_allowed(
                        true
                    )
                    else -> MiscSharedData(context!!).set_pos_neg_neu_allowed(false)
                }
                if(notif.get_allow_nots()) buildNotificationGeneral(context!!)
                summary = value
                activity!!.recreate()

                true
            }
        }
        (findPreference(getString(R.string.settings_languagees_key)) as ListPreference).apply {
            val enlangs = getStringArrayInLocale(context,"languages_arr","en")
            val locallangs = getStringArrayInLocale(context,"languages_arr")

            LanguageSharedData(context).get_lang_code().also {lang->
                val index = when(lang){
                    "de"->1;"ar"->2;else->0
                }
                this.setValueIndex(index)
                summary = value
            }
            setOnPreferenceChangeListener { p, new ->
                when (new) {
                    locallangs!![2] -> LanguageSharedData(context).set_lang_code("ar")
                    locallangs[1] -> LanguageSharedData(context).set_lang_code("de")
                    else -> LanguageSharedData(context).set_lang_code("en")
                }
                activity!!.recreate()
                true
            }
        }
        timepickerLsr = ThingsOnTimeSetListener(notif, cycle) { h, m->
            arrayOf(R.string.setting_notification_time_key,R.string.settings_daily_reset_time_key).forEach {
                var hs = "";var ms = ""
                if(h<10)hs="0";hs = hs + h
                if(m<10)ms="0";ms = ms + m
                (findPreference(getString(it))as Preference).summary = hs + ":" + ms
            }}
        timePickerDialog = TimePickerDialog(context!!, timepickerLsr, 0, 0, true)
        (findPreference(getString(R.string.setting_notification_time_key)) as Preference)
            .apply {
                var hs = "";var ms = ""
                val h = notif.get_hour(); if(h.toInt()<10)hs="0";hs = hs + h
                val m = notif.get_min(); if(m.toInt()<10)ms="0";ms = ms + m
                summary = hs + ":" + ms
                setOnPreferenceClickListener {
                    timepickerLsr.reason = TimePickerReason.Notification
                    timePickerDialog.apply {
                        updateTime(notif.get_hour(), notif.get_min());show()
                    }
                    true
                }
            }
        (findPreference(getString(R.string.settings_daily_reset_time_key)) as Preference)
            .apply {
                var hs = "";var ms = ""
                val h = cycle.get_hour(); if(h.toInt()<10)hs="0";hs = hs + h
                val m = cycle.get_minute(); if(m.toInt()<10)ms="0";ms = ms + m
                summary = hs + ":" + ms
                setOnPreferenceClickListener {
                    timepickerLsr.reason = TimePickerReason.Cycle
                    timePickerDialog.apply {
                        updateTime(cycle.get_hour(), cycle.get_minute());show()
                    }
                    true
                }
            }
        (findPreference(getString(R.string.stng_allow_overcounting_key)) as SwitchPreference).apply {
            isChecked = misc.is_over_counting_allowed()
            fun stchk(value:Boolean){misc.set_overcounting_allowed(value)}
            setOnPreferenceChangeListener { preference, newValue ->
                if(!(newValue as Boolean)){
                    buildAlertDialog(activity!!, R.string.allow_overcounting_diag_msg,
                        R.string.allow_overcounting, {
                            AsyncTask.execute {
                                ThingsDb.INSTANCE!!.thingDao().setOvercountsToGoals()
                            }
                            stchk(newValue)
                        }, { isChecked = true ;stchk(true) })
                } else stchk(newValue)

                true
            }
        }
        (findPreference(getString(R.string.settings_allow_daily_reset_key)) as SwitchPreference).apply {
            isChecked = cycle.get_allowed_cycle()
            setOnPreferenceChangeListener{pref,newval->
                newval as Boolean
                if(newval != cycle.get_allowed_cycle()){
                    if(newval) initCycleOrganizerBroadCast(context!!,cycle.get_hour(),cycle.get_minute())
                    else cancelRepeatedPendingIntent(context!!,ThingBroadcastReciever::class.java,
                        THING_CYCLE_REQ_CODE, THING_CYCLE_INTENT_ACTION)
                    cycle.set_allowed_cycle(newval)
                }
                true
            }
        }
        //TODO ADD THE DATA DISPLAY TYPE STUFF
    }


    inner class ThingsOnTimeSetListener(private val notif: NotificationSharedData, private val cyc: CycleSharedData,private val onSettime:(Int,Int)->Unit) :
        TimePickerDialog.OnTimeSetListener {
        var reason = TimePickerReason.Cycle
        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
            if (reason == TimePickerReason.Cycle) {
                cyc.apply { set_hour(hourOfDay);set_minute(minute) }
                initCycleOrganizerBroadCast(context!!, hourOfDay, minute)
            } else {
                notif.apply { set_hour(hourOfDay);set_min(minute) }
                buildNotificationGeneral(context!!)
            }
            onSettime.invoke(hourOfDay,minute)
        }

    }
}

enum class TimePickerReason {
    Notification,
    Cycle
}