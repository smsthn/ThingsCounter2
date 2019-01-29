package com.smsthn.thingscounter.Fragments

import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.smsthn.thingscounter.BroadCasts.buildNotificationGeneral
import com.smsthn.thingscounter.BroadCasts.cancelNotification
import com.smsthn.thingscounter.BroadCasts.setRepeatedPendingIntent
import com.smsthn.thingscounter.CustomViews.Dialogs.buildYesNoDialog
import com.smsthn.thingscounter.Data.ThingsDb
import com.smsthn.thingscounter.R
import com.smsthn.thingscounter.SharedData.*


class OptionsFragment : ThingAbsFragment() {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var lang: LanguageSharedData
    private lateinit var misc: MiscSharedData
    private lateinit var cycle: CycleSharedData
    private lateinit var notif: NotificationSharedData

    private lateinit var langspnr: Spinner
    private lateinit var allownotswitch: Switch
    private lateinit var allowovercountswitch: Switch
    private lateinit var nottimetxt: TextView
    private lateinit var cycletimetxt: TextView
    private lateinit var notbtn: Button
    private lateinit var cyclebtn: Button
    private lateinit var nottyperad: RadioGroup
    private lateinit var notdatatyperad: RadioGroup
    private lateinit var posnegneurad: RadioGroup
    private lateinit var nottimebtn: Button
    private lateinit var cycletimebtn: Button
    private lateinit var timepickerLsr:ThingsOnTimeSetListener
    private lateinit var timePickerDialog: TimePickerDialog



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_options, container, false)

        langspnr = view.findViewById(R.id.Languages_Spinner)
        allownotswitch = view.findViewById(R.id.AllowNotificationSwitch)
        allowovercountswitch = view.findViewById(R.id.Allow_Overcounting_Switch)
        nottimetxt = view.findViewById(R.id.NotTimeTxt)
        cycletimetxt = view.findViewById(R.id.CycleTimeTxt)
        notbtn = view.findViewById(R.id.NotificationChangeTimeBtn)
        cyclebtn = view.findViewById(R.id.CycleTimeChangeBtn)
        nottyperad = view.findViewById(R.id.NotificationTypeRadioGroup)
        notdatatyperad = view.findViewById(R.id.NotificationDataTypeRadioGroup)
        posnegneurad = view.findViewById(R.id.Pos_Neg_Neu_RadioGroup)
        nottimebtn = view.findViewById(R.id.NotificationChangeTimeBtn)
        cycletimebtn = view.findViewById(R.id.CycleTimeChangeBtn)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        langspnr.apply {
            val l = lang.get_lang_code()
            setSelection(
                when (l) {
                    "en" -> 0;"de" -> 1;"ar" -> 2;else -> 0
                }
            )
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    when (position) {
                        0 -> "en";1 -> "de";2 -> "ar";else -> "en"
                    }.apply { if (this == l) return;lang.set_lang_code(this);activity!!.recreate() }
                }
            }
        }
        posnegneurad.apply {
            val p = misc.is_pos_neg_neu_allowed()
            check(if (p) R.id.Pos_Neg_Neu_RadBtn else R.id.Not_Pos_Neg_Neu_RadBtn)
            setOnCheckedChangeListener { group, checkedId ->
                if (checkedId == R.id.Pos_Neg_Neu_RadBtn) misc.set_pos_neg_neu_allowed(true)
                else misc.set_pos_neg_neu_allowed(false)//TODO maybe add func to change top color
            }
        }
        allowovercountswitch.apply {
            isChecked = misc.is_over_counting_allowed()
            setOnCheckedChangeListener { buttonView, isChecked ->
                if (!isChecked) {
                    buildYesNoDialog(activity!!, R.string.allow_overcounting_diag_msg,
                        R.string.allow_overcounting, {
                            AsyncTask.execute {
                                ThingsDb.INSTANCE!!.thingDao().setOvercountsToGoals()
                            }
                        }, { allowovercountswitch.isChecked = !isChecked })
                } else {

                }
                misc.set_overcounting_allowed(isChecked)
            }
        }
        allownotswitch.apply {
            isChecked = (notif.get_allow_nots())
            setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    notif.set_allow_nots(isChecked)
                    buildNotificationGeneral(context!!)
                    nottyperad.isEnabled = true;notdatatyperad.isEnabled = true
                    if(notif.get_ongoing())nottimebtn.isEnabled = true
                } else {
                    notif.set_allow_nots(isChecked)
                    nottyperad.isEnabled = false;notdatatyperad.isEnabled = false
                    nottimebtn.isEnabled = false
                    buildNotificationGeneral(context)
                }

            }
        }
        nottyperad.apply {
            check(if (notif.get_ongoing())R.id.Not_Type_Ongoing else R.id.Not_Type_Daily)
            setOnCheckedChangeListener { g, id ->
                if(id == R.id.Not_Type_Ongoing){
                    nottimebtn.isEnabled = false
                    notif.set_ongoing(true)
                    buildNotificationGeneral(context!!)
                } else {
                    nottimebtn.isEnabled = false
                    notif.set_ongoing(false)
                    buildNotificationGeneral(context!!)
                }
            }
        }
        timepickerLsr = ThingsOnTimeSetListener(notif,cycle)
        timePickerDialog = TimePickerDialog(context!!,timepickerLsr,0,0,true)
        nottimebtn.setOnClickListener { timepickerLsr.reason = TimePickerReason.Notification
            timePickerDialog.apply { updateTime(notif.get_hour(),notif.get_min());show() }
        }
        cycletimebtn.setOnClickListener { timepickerLsr.reason = TimePickerReason.Cycle
            timePickerDialog.apply { updateTime(cycle.get_hour(),cycle.get_minute());show() }
        }
    }



    override fun onAttach(context: Context?) {
        super.onAttach(context)
        context ?: throw NullPointerException(this.javaClass.name + "OnAttach Context Was Null")
        lang = LanguageSharedData(context)
        misc = MiscSharedData(context)
        cycle = CycleSharedData(context)
        notif = NotificationSharedData(context)
    }


    interface OnFragmentInteractionListener {

        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            OptionsFragment()
    }


    inner class ThingsOnTimeSetListener(private val notif:NotificationSharedData,private val cyc:CycleSharedData) : TimePickerDialog.OnTimeSetListener{
        var reason = TimePickerReason.Cycle
        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
            if(reason == TimePickerReason.Cycle){
                cyc.apply { set_hour(hourOfDay);set_minute(minute) }
                initCycleOrganizerBroadCast(context!!,hourOfDay,minute)
            } else {
                notif.apply { set_hour(hourOfDay);set_min(minute) }
                buildNotificationGeneral(context!!)
            }
        }

    }
}


/*fun TimePickerDialog.createThingDialog(context: Context,reson:TimePickerReason):TimePickerDialog{
    val notif = NotificationSharedData(context)
    val cyc = CycleSharedData(context)
    val h = if (reson == TimePickerReason.Cycle) cyc.get_hour() else notif.get_hour()
    val m = if (reson == TimePickerReason.Cycle) cyc.get_minute() else notif.get_min()

    return TimePickerDialog(context,null,h,m,true)
}*/

enum class TimePickerReason{
    Notification,
    Cycle
}


