package com.smsthn.thingscounter

import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.TimePicker
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.smsthn.thingscounter.BroadCasts.buildNotificationGeneral
import com.smsthn.thingscounter.CustomViews.CustomStyles.getLightColor
import com.smsthn.thingscounter.CustomViews.CustomStyles.getPrimColor
import com.smsthn.thingscounter.CustomViews.CustomStyles.getStringArrayInLocale
import com.smsthn.thingscounter.Fragments.TimePickerReason
import com.smsthn.thingscounter.SharedData.*


class NorificationSettings : PreferenceFragmentCompat() {
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
                summary = value

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
        timepickerLsr = ThingsOnTimeSetListener(notif, cycle)
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
    }


    inner class ThingsOnTimeSetListener(private val notif: NotificationSharedData, private val cyc: CycleSharedData) :
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
        }

    }
}

enum class TimePickerReason {
    Notification,
    Cycle
}