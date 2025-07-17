package com.example.mycitiessearch.presentation.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.example.mycitiessearch.R
import com.example.mycitiessearch.domain.models.CityModel
import com.example.mycitiessearch.presentation.ui.theme.MyCitiesSearchTheme

private const val CITY_TOOLBAR_TAG = "MapToolbarTitle"
private const val CITY_TOOLBAR_SEARCH_TITLE_TAG = "CityToolbarSearchTitle"
private const val CITY_TOOLBAR_SEARCH_ICON_TAG = "CityToolbarSearchIcon"
private const val CITY_TOOLBAR_FILTER_ICON_TAG = "CityToolbarFilterIcon"
private const val CITY_TOOLBAR_CLEAR_ICON_TAG = "CityToolbarClearIcon"
private const val CITY_DETAIL_DIALOG_TAG = "CityDetailDialog"
private const val CITY_DETAIL_DIALOG_CLOSE_ICON_TAG = "CityDetailDialogCloseIcon"
private const val CITY_DETAIL_DIALOG_CITY_ICON_TAG = "CityDetailDialogCityIcon"
private const val CITY_DETAIL_DIALOG_FAVORITE_ICON_TAG = "CityDetailDialogFavoriteIcon"
private const val CITY_DETAIL_DIALOG_CITY_TITLE_TAG = "CityDetailDialogCityTitle"
private const val CITY_DETAIL_DIALOG_CITY_TEXT_TAG = "CityDetailDialogCityText"
private const val CITY_DETAIL_DIALOG_COUNTRY_TITLE_TAG = "CityDetailDialogCountryTitle"
private const val CITY_DETAIL_DIALOG_COUNTRY_TEXT_TAG = "CityDetailDialogCountryText"
private const val CITY_DETAIL_DIALOG_LAT_TITLE_TAG = "CityDetailDialogLatTitle"
private const val CITY_DETAIL_DIALOG_LONG_TITLE_TAG = "CityDetailDialogLongTitle"
private const val CITY_ALERT_DIALOG_TAG = "CityAlertDialog"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchToolbar(
    searchText: String,
    isFavoritesFilter: Boolean,
    onSearchTextChanged: (String) -> Unit,
    onFilterClicked: (Boolean) -> Unit
) {
    var openFilterDialog by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.size_110dp))
            .testTag(CITY_TOOLBAR_TAG),
        title = {
            SearchBar(
                searchText = searchText,
                onSearchTextChanged = onSearchTextChanged,
                isFavoritesFilter = isFavoritesFilter,
                onFilterClicked = {
                    openFilterDialog = true
                }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White
        )
    )

    if (openFilterDialog) {
        ConfirmDialog(
            title = stringResource(R.string.default_dialog_title),
            description = stringResource(
                if (isFavoritesFilter) {
                    R.string.filter_dialog_disable_text
                } else {
                    R.string.filter_dialog_enable_text
                }
            ),
            confirmButtonText = stringResource(
                if (isFavoritesFilter) {
                    R.string.filter_dialog_disable
                } else {
                    R.string.filter_dialog_enable
                }
            ),
            dismissButtonText = stringResource(R.string.filter_dialog_close),
            onConfirm = {
                onFilterClicked(isFavoritesFilter.not())
                openFilterDialog = false
            },
            onDismiss = { openFilterDialog = false }
        )
    }
}

@Composable
fun SearchBar(
    searchText: String,
    isFavoritesFilter: Boolean,
    onSearchTextChanged: (String) -> Unit,
    onFilterClicked: () -> Unit
) {
    var isTextPresent by rememberSaveable { mutableStateOf(searchText.isNotEmpty()) }

    OutlinedTextField(
        value = searchText,
        onValueChange = {
            onSearchTextChanged(it)
            isTextPresent = it.isNotEmpty()
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = dimensionResource(id = R.dimen.size_8dp),
                horizontal = dimensionResource(id = R.dimen.size_4dp)
            )
            .testTag(CITY_TOOLBAR_SEARCH_TITLE_TAG),
        leadingIcon = {
            Icon(
                modifier = Modifier.testTag(CITY_TOOLBAR_SEARCH_ICON_TAG),
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(
                    R.string.toolbar_content_description_search_icon
                ),
                tint = Color.Gray
            )
        },
        textStyle = MaterialTheme.typography.titleMedium,
        trailingIcon = {
            Row {
                if (isTextPresent) {
                    IconButton(
                        modifier = Modifier.testTag(CITY_TOOLBAR_CLEAR_ICON_TAG),
                        onClick = {
                            onSearchTextChanged(String())
                            isTextPresent = false
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription =
                                stringResource(
                                    id = R.string.toolbar_content_description_clear_icon
                                ),
                            tint = Color.Gray
                        )
                    }
                }
                IconButton(
                    modifier = Modifier.testTag(CITY_TOOLBAR_FILTER_ICON_TAG),
                    onClick = onFilterClicked
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_filter_list_24),
                        contentDescription =
                            stringResource(
                                id = R.string.toolbar_content_description_filter_icon
                            ),
                        tint = if (isFavoritesFilter) Color.Blue else Color.Gray
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        placeholder = {
            Text(stringResource(R.string.toolbar_placeholder)) },
        singleLine = true,
        shape = MaterialTheme.shapes.medium,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Gray,
            unfocusedIndicatorColor = Color.Gray,
            disabledIndicatorColor = Color.Gray
        ),
    )
}

@Composable
fun CityDetailDialog(
    city: CityModel,
    onDismissRequest: () -> Unit
) {
    val scrollState = rememberScrollState()

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .testTag(CITY_DETAIL_DIALOG_TAG)
                .fillMaxWidth()
                .padding(
                    dimensionResource(id = R.dimen.size_16dp)
                ),
            shape = RoundedCornerShape(
                dimensionResource(id = R.dimen.size_16dp)
            ),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.size_8dp))
                    .verticalScroll(scrollState)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(
                        modifier = Modifier.testTag(CITY_DETAIL_DIALOG_CLOSE_ICON_TAG),
                        onClick = onDismissRequest
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription =
                                stringResource(
                                    id = R.string.city_dialog_close_icon
                                ),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                Image(
                    painter = painterResource(id = R.drawable.icon_location_city_24),
                    contentDescription = stringResource(R.string.city_dialog_city_image),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.size_150dp))
                        .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                        .testTag(CITY_DETAIL_DIALOG_CITY_ICON_TAG),
                    contentScale = ContentScale.Fit
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = dimensionResource(id = R.dimen.size_16dp)),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Icon(
                        imageVector =
                            if (city.isFavorite == true) {
                                Icons.Default.Favorite
                            } else {
                                Icons.Default.FavoriteBorder
                            },
                        contentDescription =
                            stringResource(
                                if (city.isFavorite == true) {
                                    R.string.city_item_content_description_delete_favorites
                                } else {
                                    R.string.city_item_content_description_add_favorites
                                }
                            ),
                        tint =
                            if (city.isFavorite == true) {
                                Color.DarkGray
                            } else {
                                Color.Gray
                            },
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.size_28dp))
                            .testTag(CITY_DETAIL_DIALOG_FAVORITE_ICON_TAG)
                    )
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

                Text(
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(id = R.dimen.size_16dp)
                    ).testTag(CITY_DETAIL_DIALOG_CITY_TITLE_TAG),
                    text = stringResource(id = R.string.city_item_city_name),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
                        .testTag(CITY_DETAIL_DIALOG_CITY_TEXT_TAG),
                    text = city.name ?: String(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
                        .testTag(CITY_DETAIL_DIALOG_COUNTRY_TITLE_TAG),
                    text = stringResource(id = R.string.city_item_country),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
                        .testTag(CITY_DETAIL_DIALOG_COUNTRY_TEXT_TAG),
                    text = city.country,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                Text(
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(id = R.dimen.size_16dp)
                    ).testTag(CITY_DETAIL_DIALOG_LAT_TITLE_TAG),
                    text = stringResource(
                        id = R.string.city_item_latitude,
                        city.lat
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(id = R.dimen.size_16dp)
                    ).testTag(CITY_DETAIL_DIALOG_LONG_TITLE_TAG),
                    text = stringResource(
                        id = R.string.city_item_longitude,
                        city.lon
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ConfirmDialog(
    title: String,
    description: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.testTag(CITY_ALERT_DIALOG_TAG),
        onDismissRequest = {
            onDismiss()
        },
        title = { Text(text = title) },
        text = { Text(text = description) },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) { Text(confirmButtonText) }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) { Text(dismissButtonText) }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchToolbarPreview() {
    MaterialTheme {
        SearchToolbar(
            searchText = String(),
            isFavoritesFilter = false,
            onSearchTextChanged = { },
            onFilterClicked = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CityDetailDialogPreview() {
    MyCitiesSearchTheme {
        CityDetailDialog(
            CityModel(
                id = 1,
                name = "City A",
                country = "Country A",
                isFavorite = false,
                lon = 106.232341,
                lat = 36.167531
            ),
            onDismissRequest = { }
        )
    }
}