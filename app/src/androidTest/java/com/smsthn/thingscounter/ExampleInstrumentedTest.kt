/*
package com.smsthn.thingscounter



import androidx.preference.PreferenceManager
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

*/
/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 *//*

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Before
    fun launchActivity(){
*/
/*
        ActivityScenario.launch(MainActivity::class.java)
*//*


    }
    @Test
    fun useAppContext() {
        */
/*val ctx =  InstrumentationRegistry.getInstrumentation().context
        val pref = PreferenceManager.getDefaultSharedPreferences(ctx)
        assert(pref.contains(ctx.getString(R.string.settings_allow_notificaitons_key)))*//*

        */
/*ActivityScenario.launch(MainActivity::class.java)
            .use({ scenario -> scenario.onActivity({ activity ->
                val pref = PreferenceManager.getDefaultSharedPreferences(activity)
                assert(pref.contains(activity.getString(R.string.settings_allow_notificaitons_key)))
            }) })*//*

    }
}
*/
