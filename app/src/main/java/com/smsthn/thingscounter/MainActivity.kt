package com.smsthn.thingscounter

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import androidx.lifecycle.LifecycleRegistry
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.smsthn.thingscounter.BroadCasts.buildNotificationGeneral
import com.smsthn.thingscounter.CustomViews.CustomStyles.getStringArrayInLocale
import com.smsthn.thingscounter.Fragments.ThingFragment
import com.smsthn.thingscounter.Fragments.ThingFragmentDirections
import com.smsthn.thingscounter.SharedData.LanguageSharedData
import com.smsthn.thingscounter.SharedData.MiscSharedData
import com.smsthn.thingscounter.SharedData.changeLang
import java.util.*
import android.view.MotionEvent
import android.view.GestureDetector.SimpleOnGestureListener
import android.text.method.Touch.onTouchEvent
import android.view.GestureDetector
import android.view.View.OnTouchListener
import android.widget.TextView
import com.smsthn.thingscounter.Data.Entities.Thing
import com.smsthn.thingscounter.Data.ThingsDb
import com.smsthn.thingscounter.SharedData.resetThingsAndAddCycle
import kotlin.random.Random


class MainActivity : AppCompatActivity(), LifecycleOwner {
    private lateinit var mLifecycleRegistry: LifecycleRegistry
    private lateinit var appBarConfiguration: AppBarConfiguration

    private var localTypes: Array<String>? = arrayOf()
    private var engTypes: Array<String>? = arrayOf()

    private var localCtgs: Array<String>? = arrayOf()
    private var engCtgs: Array<String>? = arrayOf()

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


        navigation.setupWithNavController(navController)

        initNavigation()




        mLifecycleRegistry = LifecycleRegistry(this)
        mLifecycleRegistry.markState(Lifecycle.State.CREATED)

        Handler(mainLooper).postDelayed({
            val adRequest = AdRequest.Builder().build()
            mainAdView?.loadAd(adRequest)
        }, 2000)
    }

    fun initNavigation(): Unit {
        navigation.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.things_nav -> goHome()
                R.id.charts_nav -> goToCharts()
                R.id.options_nav -> goToOptions()
            }
            true
        }
    }

    private fun goHome() {
        val nav = findNavController(R.id.host_fragment)
        when (nav.currentDestination?.id) {
            R.id.thing_frag_dest -> return//TODO ADD REFRESH FUNCTION
            R.id.charts_frag_dest -> nav.navigate(R.id.action_charts_things)
            R.id.option_frag_dest -> nav.navigate(R.id.action_options_thing)
        }
    }

    fun goToCharts(): Unit {
        val nav = findNavController(R.id.host_fragment)
        when (nav.currentDestination?.id) {
            R.id.thing_frag_dest -> nav.navigate(R.id.action_things_charts)
            R.id.charts_frag_dest -> return
            R.id.option_frag_dest -> nav.navigate(R.id.action_options_charts)
        }
    }

    fun goToOptions(): Unit {
        val nav = findNavController(R.id.host_fragment)
        when (nav.currentDestination?.id) {
            R.id.thing_frag_dest -> {
                nav.navigate(R.id.action_thing_frag_dest_to_option_frag_dest)
            }
            R.id.charts_frag_dest -> nav.navigate(R.id.action_charts_frag_dest_to_option_frag_dest)
            R.id.option_frag_dest -> return
        }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.host_fragment).navigateUp()


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.getItemId()
        when(id){
            R.id.go_to_add_frag->findNavController(R.id.host_fragment).navigate(R.id.action_thing_frag_dest_to_addThingFragment)
            R.id.testaddthing->testAddThing()
            R.id.testaddmanythings->testAddManythings()
            R.id.testdeleteall->testDeleteAllThings()
            R.id.testresetthing->testResetThings()
        }


        return /*item.onNavDestinationSelected(findNavController(R.id.host_fragment)) ||*/  super.onOptionsItemSelected(
            item
        )
    }

    /**
     * Here Are The Test Functions
     */
    fun testAddThing() {
        AsyncTask.execute {
            val db = ThingsDb.getAppDataBase(this)
            val dao = db?.thingDao()
            val t = Thing(
                type = engTypes!![Random.nextInt(engTypes!!.size )],
                catagory = engCtgs!![Random.nextInt(engCtgs!!.size )],
                goal = Random.nextInt(1,300),
                count = Random.nextInt(320), name = "thing" + Random.nextInt(), enabled = true

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
                        type = engTypes!![Random.nextInt(engTypes!!.size )],
                        catagory = engCtgs!![Random.nextInt(engCtgs!!.size )],
                        goal = Random.nextInt(300),
                        count = Random.nextInt(320), name = "thing" + Random.nextInt(), enabled = true

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
    fun testDeleteAllThings(){
        AsyncTask.execute {
            val db = ThingsDb.getAppDataBase(this)
            val dao = db?.thingDao()
            dao?.deleteAllThings()
        }
    }
    fun testResetThings(){
        resetThingsAndAddCycle(this)
    }
    fun showShadow(){
        val txt = findViewById<TextView>(R.id.gsgs)
        txt.visibility = if(txt.visibility == View.VISIBLE)View.GONE else View.VISIBLE
    }
}

