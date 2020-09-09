package com.travels.searchtravels

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.travels.searchtravels.activity.ChipActivity
import com.travels.searchtravels.activity.MainActivity
import com.travels.searchtravels.utils.Constants
import junit.framework.Assert.assertTrue
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeoutException


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

//нужно сделать метод loadByCategory публичным
//Здесь тест разных категорий и тест на корректоность данных о путешествии

@RunWith(AndroidJUnit4::class)
@LargeTest
class PhotosCategoriesTest {

    @get:Rule
    val mainActivity = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val chipActivity = ActivityScenarioRule(ChipActivity::class.java)

    private fun commonCategoryTest(categoryName: String, cityName: String) {
        val commonScenario = mainActivity.scenario
        commonScenario.onActivity { currentActivity -> currentActivity.loadByCategory(categoryName) }
        assertTrue("There is no $cityName Error", Constants.PICKED_CITY_RU == cityName)
        try {
            Thread.sleep(10000)
        } catch (ex: Exception) {
        }
        intended(hasComponent(ChipActivity::class.java.name))
        onView(withId(R.id.cityTV)).check(matches(withText(Constants.PICKED_CITY_RU)))
        commonScenario.recreate()
    }

    @Test
    fun testBeachPhotoCategory() {
        commonCategoryTest("beach", "Римини")
    }


    @Test
    fun testSeaPhotoCategory() {
        commonCategoryTest("sea", "Римини")
    }

    @Test
    fun testSnowPhotoCategory() {
        commonCategoryTest("snow", "Хельсинки")
    }

    @Test
    fun testMountainPhotoCategory() {
        commonCategoryTest("mountain", "Сочи")
    }

    @Test
    fun testOceanPhotoCategory() {
        commonCategoryTest("ocean", "Римини")
    }
    
    @Test
    fun testNomadApi(){
        val nomadScenario = chipActivity.scenario
        nomadScenario.onActivity { activity -> activity.getInfoNomad("Helsinki") }
        onView(withId(R.id.countryTV)).check(matches(withText("Finland")))
        onView(withId(R.id.cityTV)).check(matches(withText("Helsinki")))
    }
}

fun ViewInteraction.waitUntilInvisible(timeout: Long): ViewInteraction {
    val startTime = System.currentTimeMillis()
    val endTime = startTime + timeout

    do {
        try {
            check(matches(not(isDisplayed())))
            return this
        } catch (e: NoMatchingViewException) {
            Thread.sleep(50)
        }
    } while (System.currentTimeMillis() < endTime)

    throw TimeoutException()
}
