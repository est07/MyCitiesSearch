package com.example.mycitiessearch.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.mycitiessearch.domain.models.CityModel

@Composable
fun CitiesMapScreen(city: CityModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "Map Screen", fontSize = 20.sp)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = city.name ?: String())
        Text(text = city.country)
        Text(text = city.lat.toString())
        Text(text = city.lon.toString())
    }
}