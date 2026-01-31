package com.example.app1

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SensorData(
    viewModel: SensorViewModel = viewModel(factory = SensorViewModelFactory(LocalContext.current.applicationContext))
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            SensorCard(
                "Accelerometer",
                listOf(
                    "X" to uiState.accelerometerValues.first,
                    "Y" to uiState.accelerometerValues.second,
                    "Z" to uiState.accelerometerValues.third
                )
            )
        }
        item {
            if (uiState.isTemperatureSensorAvailable) {
                uiState.temperatureValue?.let {
                    SensorCard("Temperature", listOf("Value" to it), "°C")
                }
            } else {
                SensorNotAvailableCard("Temperature")
            }
        }
        item {
            if (uiState.isGyroscopeAvailable) {
                uiState.gyroscopeValues?.let {
                    SensorCard(
                        "Gyroscope",
                        listOf(
                            "X" to it.first,
                            "Y" to it.second,
                            "Z" to it.third
                        )
                    )
                }
            } else {
                SensorNotAvailableCard("Gyroscope")
            }
        }
    }
}

@Composable
fun SensorCard(sensorName: String, values: List<Pair<String, Float>>, unit: String = "") {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "$sensorName Data:", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            values.forEach { (label, value) ->
                Row {
                    Text(text = "$label: ", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = String.format("%.2f%s", value, if (unit.isNotEmpty()) " $unit" else ""),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun SensorNotAvailableCard(sensorName: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "$sensorName sensor not available!", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
