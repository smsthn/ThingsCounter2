package com.smsthn.thingscounter

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import androidx.lifecycle.LifecycleRegistry
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.smsthn.thingscounter.CustomViews.CustomStyles.getStringArrayInLocale
import java.util.*
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.smsthn.thingscounter.Data.Entities.Thing
import com.smsthn.thingscounter.Data.ThingsDb
import kotlin.random.Random
import android.app.PendingIntent
import android.content.Intent
import com.smsthn.thingscounter.BroadCasts.*
import com.smsthn.thingscounter.SharedData.*


class MainActivity : AppCompatActivity(), LifecycleOwner {
    private lateinit var mLifecycleRegistry: LifecycleRegistry
    private lateinit var appBarConfiguration: AppBarConfiguration

    private var localTypes: Array<String>? = arrayOf()
    private var engTypes: Array<String>? = arrayOf()

    private var localCtgs: Array<String>? = arrayOf()
    private var engCtgs: Array<String>? = arrayOf()

    private var down = false

    override fun attachBaseContext(newBase: Context?) {

        super.attachBaseContext(changeLang(newBase!!, Locale(LanguageSharedData(newBase).get_lang_code())))
    }


    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        buildNotificationGeneral(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        checkNotifAndCycleRepeated()


        /* Here Init Ctgs And Types*/
        localTypes = if (MiscSharedData(this).is_pos_neg_neu_allowed()) {
            arrayOf(getString(R.string.positive), getString(R.string.negative), getString(R.string.neutral))
        } else getStringArrayInLocale(this, "colors_arr")
        engTypes = if (MiscSharedData(this).is_pos_neg_neu_allowed()) {
            arrayOf("Positive", "Negative", "Neutral")
        } else getStringArrayInLocale(this, "colors_arr", "en")
        localCtgs = getStringArrayInLocale(this, "Catagories")

        engCtgs = getStringArrayInLocale(this, "Catagories", "en")


        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713")

        val host: NavHostFragment = supportFragmentManager.findFragmentById(R.id.host_fragment) as NavHostFragment

        val navController = host.navController
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.thing_frag_dest))
       setupActionBarWithNavController(navController, appBarConfiguration)
        /*
               bar.setupWithNavController(navController)*/
        /*navigation.setupWithNavController(navController)*/

        /*initappbar()*/






        mLifecycleRegistry = LifecycleRegistry(this)
        mLifecycleRegistry.markState(Lifecycle.State.CREATED)

        Handler(mainLooper).postDelayed({
            val adRequest = AdRequest.Builder().build()
            mainAdView?.loadAd(adRequest)
        }, 2000)
    }

    fun checkNotifAndCycleRepeated(){
        AsyncTask.execute {
            val cyc = PendingIntent.getBroadcast(
                this, THING_CYCLE_REQ_CODE,
                Intent(this,ThingBroadcastReciever::class.java).apply { action = THING_CYCLE_INTENT_ACTION },
                PendingIntent.FLAG_NO_CREATE
            )
            val notif = PendingIntent.getBroadcast(
                this, DAILY_REM_REQ_CODE,
                Intent(this,ThingBroadcastReciever::class.java),
                PendingIntent.FLAG_NO_CREATE
            )
            NotificationSharedData(this).apply {
                if(get_allow_nots()){
                    if(notif == null) buildNotificationGeneral(this@MainActivity)
                } else {
                    if(notif != null) {
                        cancelRepeatedPendingIntent(this@MainActivity,ThingBroadcastReciever::class.java)
                        cancelNotification(this@MainActivity)
                    }
                }
            }
            CycleSharedData(this).apply {
                if(get_allowed_cycle()){
                    if(cyc == null) initCycleOrganizerBroadCast(this@MainActivity,get_hour(),get_minute())
                }else {
                    if(cyc != null) {
                        cancelRepeatedPendingIntent(this@MainActivity,ThingBroadcastReciever::class.java,
                            THING_CYCLE_REQ_CODE,
                            THING_CYCLE_INTENT_ACTION)

                    }
                }
            }
        }
    }

    /*fun initappbar(): Unit {
        bar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.things_nav -> goHome()
                R.id.charts_nav -> goToCharts()
                R.id.options_nav -> goToOptions()
                *//*R.id.options_nav2->{
                    val bottomNavDrawerFragment = ThingDetailsFragment()
                    bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
                }*//*
            }
            true
        }
        bar.setNavigationOnClickListener {
            //TODO ADD OPTION TO OPEN NAVIGATOIN BAR HERE
        }
    }*/

   /* private fun goHome() {


        *//*bar.alpha = 1f
        fab.alpha = 1f
        bar.isClickable = true
        fab.isClickable = true*//*
        val nav = findNavController(R.id.host_fragment)
        when (nav.currentDestination?.id) {
            R.id.thing_frag_dest -> nav.navigate(R.id.thing_frag_dest)
           *//* R.id.charts_frag_dest -> nav.navigate(R.id.action_charts_things)*//*
            R.id.option_frag_dest -> nav.navigate(R.id.action_options_thing)
            else ->nav.navigate(R.id.thing_frag_dest)
        }
    }

    fun goToCharts(): Unit {

        *//*bar.alpha = 1f
        fab.alpha = 1f
        bar.isClickable = true
        fab.isClickable = true*//*
        val nav = findNavController(R.id.host_fragment)
        when (nav.currentDestination?.id) {
            R.id.thing_frag_dest -> nav.navigate(R.id.action_things_charts)
            R.id.charts_frag_dest -> return
            R.id.option_frag_dest -> nav.navigate(R.id.action_options_charts)
            else ->nav.navigate(R.id.charts_frag_dest)
        }
    }

    fun goToOptions(): Unit {


        val nav = findNavController(R.id.host_fragment)
        when (nav.currentDestination?.id) {
            R.id.thing_frag_dest -> {
                *//*nav.navigate(R.id.action_thing_frag_dest_to_option_frag_dest)*//*
                nav.navigate(R.id.go_to_perfer)
            }
            R.id.charts_frag_dest ->*//* nav.navigate(R.id.action_charts_frag_dest_to_option_frag_dest)*//*nav.navigate(R.id.go_to_perfer)
            R.id.option_frag_dest -> return
        }

    }*/


    override fun onSupportNavigateUp() = findNavController(R.id.host_fragment).navigateUp()


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.getItemId()
        when (id) {

            R.id.testaddthing -> testAddThing()
            R.id.testaddmanythings -> testAddManythings()
            R.id.testdeleteall -> testDeleteAllThings()
            R.id.testresetthing -> testResetThings()
            R.id.checknotreminder -> testCheckNotificationReminderActive()
            R.id.checkcyclereminder -> testCheckCycleReminderActive()
        }
        return true

    }
    /**
     * Here Are The Test Functions
     */
    fun testAddThing() {
        AsyncTask.execute {
            val db = ThingsDb.getAppDataBase(this)
            val dao = db?.thingDao()
            val t = Thing(
                type = engTypes!![Random.nextInt(engTypes!!.size)],
                catagory = engCtgs!![Random.nextInt(engCtgs!!.size)],
                goal = Random.nextInt(1, 300),
                count = Random.nextInt(320), name = "thing" + Random.nextInt(), enabled = Random.nextBoolean()

            )
            dao?.insertThing(
                t
            )
        }
    }

    fun testAddManythings() {
        AsyncTask.execute {
            val things = mutableListOf<Thing>()
            for (i in 1..100) {
                things.add(
                    Thing(
                        type = engTypes!![Random.nextInt(engTypes!!.size)],
                        catagory = engCtgs!![Random.nextInt(engCtgs!!.size)],
                        goal = Random.nextInt(300),
                        count = Random.nextInt(320), name = "thing" + Random.nextInt(), enabled = Random.nextBoolean()

                    )
                )
            }
            val db = ThingsDb.getAppDataBase(this)
            val dao = db?.thingDao()
            dao?.insetManyThings(
                things
            )
        }
    }

    fun testDeleteAllThings() {
        AsyncTask.execute {
            val db = ThingsDb.getAppDataBase(this)
            val dao = db?.thingDao()
            dao?.deleteAllThings()
        }
    }

    fun testResetThings() {
        resetThingsAndAddCycle(this)
    }
    fun testCheckNotificationReminderActive(){
        val bc = PendingIntent.getBroadcast(
            this, DAILY_REM_REQ_CODE,
            Intent(this,ThingBroadcastReciever::class.java),
            PendingIntent.FLAG_NO_CREATE
        )
        val alarmUp = bc != null

        if (alarmUp) {
           Toast.makeText(this,"Alarm was Active"+bc.creatorUserHandle!!.describeContents(),Toast.LENGTH_LONG).show()
        } else{
            Toast.makeText(this,"Nothing was Active",Toast.LENGTH_LONG).show()
        }
    }
    fun testCheckCycleReminderActive(){
        val bc = PendingIntent.getBroadcast(
            this, THING_CYCLE_REQ_CODE,
            Intent(this,ThingBroadcastReciever::class.java).apply { action = THING_CYCLE_INTENT_ACTION },
            PendingIntent.FLAG_NO_CREATE
        )
        val alarmUp = bc != null

        if (alarmUp) {
            Toast.makeText(this,"Alarm was Active"+bc.creatorUserHandle!!.describeContents(),Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this,"Nothing was Active",Toast.LENGTH_LONG).show()
        }
    }

}

