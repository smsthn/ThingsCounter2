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
        view!!.setBackgroundColor(Color.WHITE)

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_notification, rootKey)
        (findPreference(getString(R.string.stng_posnegneu_key)) as ListPreference).apply {
            this.setOnPreferenceChangeListener { p, new ->
                when (new) {
                    getString(R.string.positive_negative_neutral) -> MiscSharedData(context!!).set_pos_neg_neu_allowed(
                        true
                    )
                    else -> MiscSharedData(context!!).set_pos_neg_neu_allowed(false)
                }
                true
            }
        }
        (findPreference(getString(R.string.settings_languagees_key)) as ListPreference).apply {
            setOnPreferenceChangeListener { p, new ->
                when (new) {
                    "Arabic" -> LanguageSharedData(context).set_lang_code("ar")
                    "German" -> LanguageSharedData(context).set_lang_code("de")
                    else -> LanguageSharedData(context).set_lang_code("en")
                }
                true
            }
        }
        timepickerLsr = ThingsOnTimeSetListener(notif, cycle)
        timePickerDialog = TimePickerDialog(context!!, timepickerLsr, 0, 0, true)
        (findPreference(getString(R.string.setting_notification_time_key)) as Preference)
            .setOnPreferenceClickListener {
                timepickerLsr.reason = TimePickerReason.Notification
                timePickerDialog.apply {
                    updateTime(notif.get_hour(), notif.get_min());show()
                }
                true
            }
        (findPreference(getString(R.string.settings_daily_reset_time_key)) as Preference)
            .setOnPreferenceClickListener {
                timepickerLsr.reason = TimePickerReason.Cycle
                timePickerDialog.apply {
                    updateTime(cycle.get_hour(), cycle.get_minute());show()
                }
                true
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