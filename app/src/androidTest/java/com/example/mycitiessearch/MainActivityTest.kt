package com.example.mycitiessearch

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.example.mycitiessearch.presentation.ErrorView
import com.example.mycitiessearch.presentation.LoadingScreen
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun validateLoaderTest(): Unit = with(composeTestRule) {
        setContent {
            LoadingScreen()
        }

        onNodeWithTag("LoaderTag").assertIsDisplayed()
    }

    @Test
    fun validateErrorViewTest(): Unit = with(composeTestRule) {
        var retrySelected  = false
        setContent {
            ErrorView {
                retrySelected = true
            }
        }

        onNodeWithTag("NetworkImageError").assertIsDisplayed()
        onNodeWithTag("NetworkErrorDescription").assertIsDisplayed()
        onNodeWithText(
            context.getString(R.string.response_error_network_error)
        ).assertIsDisplayed()
        onNodeWithText(
            context.getString(R.string.response_error_btn_retry)
        ).assertIsDisplayed()
        onNodeWithTag("NetworkErrorButton").assertIsDisplayed()
        onNodeWithTag("NetworkErrorButton").performClick()
        assert(retrySelected)
    }
}