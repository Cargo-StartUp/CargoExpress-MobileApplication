package com.cargoexpress.app

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * This class contains an instrumented test that will execute on an Android device.
 *
 * For more information on Android testing, see the official documentation:
 * [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)// Specifies the test runner to use (AndroidJUnit4)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.cargoexpress.app", appContext.packageName)
    }
}
