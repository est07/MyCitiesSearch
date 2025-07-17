package com.example.mycitiessearch

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.test.platform.app.InstrumentationRegistry
import com.example.mycitiessearch.domain.models.CityModel
import com.example.mycitiessearch.presentation.compose.PortraitScreen
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class CitiesScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun validateCitiesListTest(): Unit = with(composeTestRule) {
        val cities =
            listOf(
                CityModel(
                    id = 1,
                    name = "City A",
                    country = "Country A",
                    isFavorite = false,
                    lon = 106.232341,
                    lat = 36.167531
                ),
                CityModel(
                    id = 2,
                    name = "City B",
                    country = "Country B",
                    isFavorite = true,
                    lon = 1.0,
                    lat = 1.0
                )
            )
        setContent {
            PortraitScreen(
                cities = flowOf(PagingData.from(cities)).collectAsLazyPagingItems(),
                searchText = "",
                isFavoritesFilter = false,
                onSearchTextChanged = {},
                navigateToMap = {},
                onFilterClicked = {},
                onFavoriteSelected = {}
            )
        }

        onNodeWithTag(
            "CityToolbarSearchTitle"
        ).assertExists()

        cities.forEach { item ->
            onNodeWithText(item.name?:String()).assertIsDisplayed()
            onNodeWithText(item.country).assertIsDisplayed()
            onNodeWithText(item.name?:String()).assertIsDisplayed()
            onNodeWithText(context.getString(
                R.string.city_item_latitude_and_longitude,
                item.lat.toString(),
                item.lon.toString()
            )).assertIsDisplayed()
        }
    }

    @Test
    fun validateSelectCityTest(): Unit = with(composeTestRule) {
        var showMapView  = false

        val city = CityModel(
            id = 1,
            name = "City A",
            country = "Country A",
            isFavorite = false,
            lon = 106.232341,
            lat = 36.167531
        )

        setContent {
            PortraitScreen(
                cities = flowOf(PagingData.from(listOf(city))).collectAsLazyPagingItems(),
                searchText = "",
                isFavoritesFilter = false,
                onSearchTextChanged = {},
                navigateToMap = {showMapView = true},
                onFilterClicked = {},
                onFavoriteSelected = {}
            )
        }

        onNodeWithTag("CityToolbarSearchTitle").assertIsDisplayed()
        onNodeWithTag("CityItemFavoriteIcon").assertIsDisplayed()
        onNodeWithTag("CityItemSeeButton").assertIsDisplayed()
        onNodeWithTag("CityItemSeeButton").performClick()

        onNodeWithTag("CityDetailDialog").assertIsDisplayed()
        onNodeWithTag("CityDetailDialogCloseIcon").assertIsDisplayed()
        onNodeWithTag("CityDetailDialogCityIcon").assertIsDisplayed()
        onNodeWithTag("CityDetailDialogFavoriteIcon").assertIsDisplayed()
        onNodeWithTag("CityDetailDialogCityTitle").assertIsDisplayed()
        onNodeWithTag("CityDetailDialogCityText").assertIsDisplayed()

        onNodeWithTag("CityDetailDialogCountryTitle").assertIsDisplayed()
        onNodeWithTag("CityDetailDialogCountryText").assertIsDisplayed()
        onNodeWithTag("CityDetailDialogLatTitle").assertIsDisplayed()
        onNodeWithTag("CityDetailDialogLongTitle").assertIsDisplayed()
        onNodeWithTag("CityDetailDialogCloseIcon").performClick()
        onNodeWithTag("CityItem").assertIsDisplayed()
        onNodeWithTag("CityItem").performClick()
        assert(showMapView)
    }

    @Test
    fun validateSelectFavoriteCityTest(): Unit = with(composeTestRule) {
        var isFavoriteSelected  = false

        val city = CityModel(
            id = 1,
            name = "City A",
            country = "Country A",
            isFavorite = false,
            lon = 106.232341,
            lat = 36.167531
        )

        setContent {
            PortraitScreen(
                cities = flowOf(PagingData.from(listOf(city))).collectAsLazyPagingItems(),
                searchText = "",
                isFavoritesFilter = false,
                onSearchTextChanged = {},
                navigateToMap = {},
                onFilterClicked = {},
                onFavoriteSelected = { isFavoriteSelected = true }
            )
        }

        onNodeWithTag("CityToolbarSearchTitle").assertIsDisplayed()
        onNodeWithTag("CityItemFavoriteIcon").assertIsDisplayed()
        onNodeWithTag("CityItemSeeButton").assertIsDisplayed()
        onNodeWithTag("CityItemFavoriteIcon").performClick()

        onNodeWithTag("CityAlertDialog").assertIsDisplayed()

        onNodeWithText(
            context.getString(R.string.default_dialog_title)
        ).assertIsDisplayed()
        onNodeWithText(
            context.getString(
                R.string.favorite_dialog_enable_text,
                city.name ?: String()
            )
        ).assertIsDisplayed()
        onNodeWithText(
            context.getString(R.string.favorite_dialog_enable)
        ).assertIsDisplayed()
        onNodeWithText(
            context.getString(R.string.favorite_dialog_disable)
        ).assertIsDisplayed()
        onNodeWithText(
            context.getString(R.string.favorite_dialog_enable)
        ).performClick()

        onNodeWithTag("CityItem").assertIsDisplayed()
        assert(isFavoriteSelected)
    }

    @Test
    fun validateSelectNoFavoriteCityTest(): Unit = with(composeTestRule) {
        var isFavoriteSelected  = false

        val city = CityModel(
            id = 1,
            name = "City A",
            country = "Country A",
            isFavorite = true,
            lon = 106.232341,
            lat = 36.167531
        )

        setContent {
            PortraitScreen(
                cities = flowOf(PagingData.from(listOf(city))).collectAsLazyPagingItems(),
                searchText = "",
                isFavoritesFilter = false,
                onSearchTextChanged = {},
                navigateToMap = {},
                onFilterClicked = {},
                onFavoriteSelected = { isFavoriteSelected = true }
            )
        }

        onNodeWithTag("CityToolbarSearchTitle").assertIsDisplayed()
        onNodeWithTag("CityItemFavoriteIcon").assertIsDisplayed()
        onNodeWithTag("CityItemSeeButton").assertIsDisplayed()
        onNodeWithTag("CityItemFavoriteIcon").performClick()

        onNodeWithTag("CityAlertDialog").assertIsDisplayed()

        onNodeWithText(
            context.getString(R.string.default_dialog_title)
        ).assertIsDisplayed()
        onNodeWithText(
            context.getString(
                R.string.favorite_dialog_disable_text,
                city.name ?: String()
            )
        ).assertIsDisplayed()
        onNodeWithText(
            context.getString(R.string.favorite_dialog_enable)
        ).assertIsDisplayed()
        onNodeWithText(
            context.getString(R.string.favorite_dialog_disable)
        ).assertIsDisplayed()
        onNodeWithText(
            context.getString(R.string.favorite_dialog_enable)
        ).performClick()

        onNodeWithTag("CityItem").assertIsDisplayed()
        assert(isFavoriteSelected)
    }

    @Test
    fun validateCityToolbar_with_isFavoritesFilter_false_Test(): Unit = with(composeTestRule) {
        var isFilterSelected  = false

        val city = CityModel(
            id = 1,
            name = "City A",
            country = "Country A",
            isFavorite = false,
            lon = 106.232341,
            lat = 36.167531
        )

        setContent {
            PortraitScreen(
                cities = flowOf(PagingData.from(listOf(city))).collectAsLazyPagingItems(),
                searchText = "",
                isFavoritesFilter = false,
                onSearchTextChanged = {},
                navigateToMap = {},
                onFilterClicked = { isFilterSelected = true },
                onFavoriteSelected = {}
            )
        }

        onNodeWithText(
            context.getString(R.string.toolbar_placeholder)
        ).assertIsDisplayed()

        onNodeWithTag("CityToolbarSearchTitle").assertIsDisplayed()
        onNodeWithTag("CityToolbarFilterIcon").assertIsDisplayed()
        onNodeWithTag("CityToolbarFilterIcon").performClick()

        onNodeWithTag("CityAlertDialog").assertIsDisplayed()

        onNodeWithText(
            context.getString(R.string.default_dialog_title)
        ).assertIsDisplayed()
        onNodeWithText(
            context.getString(R.string.filter_dialog_enable_text)
        ).assertIsDisplayed()
        onNodeWithText(
            context.getString(R.string.filter_dialog_enable)
        ).assertIsDisplayed()
        onNodeWithText(
            context.getString(R.string.filter_dialog_enable)
        ).performClick()
        onNodeWithTag("CityItem").assertIsDisplayed()
        assert(isFilterSelected)
    }

    @Test
    fun validateCityToolbar_with_isFavoritesFilter_true_Test(): Unit = with(composeTestRule) {

        val city = CityModel(
            id = 1,
            name = "City A",
            country = "Country A",
            isFavorite = false,
            lon = 106.232341,
            lat = 36.167531
        )

        setContent {
            PortraitScreen(
                cities = flowOf(PagingData.from(listOf(city))).collectAsLazyPagingItems(),
                searchText = "",
                isFavoritesFilter = true,
                onSearchTextChanged = {},
                navigateToMap = {},
                onFilterClicked = {},
                onFavoriteSelected = {}
            )
        }

        onNodeWithText(
            context.getString(R.string.toolbar_placeholder)
        ).assertIsDisplayed()

        onNodeWithTag("CityToolbarSearchTitle").assertIsDisplayed()
        onNodeWithTag("CityToolbarFilterIcon").assertIsDisplayed()
        onNodeWithTag("CityToolbarFilterIcon").performClick()

        onNodeWithTag("CityAlertDialog").assertIsDisplayed()

        onNodeWithText(
            context.getString(R.string.default_dialog_title)
        ).assertIsDisplayed()
        onNodeWithText(
            context.getString(R.string.filter_dialog_disable_text)
        ).assertIsDisplayed()
        onNodeWithText(
            context.getString(R.string.filter_dialog_disable)
        ).assertIsDisplayed()
        onNodeWithText(
            context.getString(R.string.filter_dialog_disable)
        ).performClick()
        onNodeWithTag("CityItem").assertIsDisplayed()
    }
}