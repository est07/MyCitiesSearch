package com.example.mycitiessearch

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.example.mycitiessearch.domain.models.CityModel
import com.example.mycitiessearch.presentation.compose.CitiesMapScreen
import org.junit.Rule
import org.junit.Test

class MapScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun validateMapScreenTest(): Unit = with(composeTestRule) {
        var navigateBack  = false
        val city = CityModel(
            id = 1,
            name = "City A",
            country = "Country A",
            isFavorite = false,
            lon = 106.232341,
            lat = 36.167531
        )
        setContent {
            CitiesMapScreen(
                city = city,
                navigateBack = { navigateBack = true }
            )
        }

        onNodeWithTag("MapToolbarTitle").assertIsDisplayed()
        onNodeWithTag("MapToolbarBackButton").assertIsDisplayed()
        onNodeWithText(
            context.getString(R.string.map_toolbar_title)
        ).assertIsDisplayed()
        onNodeWithTag("GoogleMapView").assertIsDisplayed()
        onNodeWithTag("MapToolbarBackButton").performClick()
        assert(navigateBack)
    }
}