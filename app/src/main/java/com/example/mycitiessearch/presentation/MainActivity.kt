package com.example.mycitiessearch.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mycitiessearch.R
import com.example.mycitiessearch.presentation.navigation.NavigationWrapper
import com.example.mycitiessearch.presentation.states.CitiesListStates
import com.example.mycitiessearch.presentation.ui.theme.MyCitiesSearchTheme
import com.example.mycitiessearch.presentation.viewmodels.CitiesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val citiesViewModel: CitiesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            citiesViewModel.getCitiesList()
            MyCitiesSearchTheme {
                val getCitiesListService = citiesViewModel.citiesListState.collectAsStateWithLifecycle()

                when (getCitiesListService.value) {
                    CitiesListStates.Loading -> {
                        LoadingScreen()
                    }

                    is CitiesListStates.Error -> {
                        ErrorView(
                            onRetryClick = {
                                citiesViewModel.getCitiesList()
                            }
                        )
                    }

                    is CitiesListStates.Success -> {
                        NavigationWrapper(citiesViewModel)
                    }

                    else -> Unit
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(dimensionResource(R.dimen.size_56dp)),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = dimensionResource(R.dimen.size_6dp)
        )
    }
}

@Composable
fun ErrorView(
    onRetryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.size_24dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            painter = painterResource(R.drawable.icon_wifi_error_100),
            contentDescription = stringResource(
                R.string.response_error_content_description_error_network_icon
            ),
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(dimensionResource(R.dimen.size_100dp))
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.size_24dp)))

        Text(
            text = stringResource(R.string.response_error_network_error),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.size_32dp)))

        Button(
            onClick = onRetryClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.size_56dp)),
            shape = RoundedCornerShape(dimensionResource(R.dimen.size_12dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = stringResource(R.string.response_error_btn_retry),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCenteredCircularLoading() {
    MaterialTheme {
        LoadingScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewErrorView() {
    MaterialTheme {
        ErrorView(
            onRetryClick = {}
        )
    }
}
