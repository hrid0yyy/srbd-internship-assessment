package com.example.app1

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SensorUiState(
    val accelerometerValues: Triple<Float, Float, Float> = Triple(0f, 0f, 0f),
    val temperatureValue: Float? = null,
    val gyroscopeValues: Triple<Float, Float, Float>? = null,
    val isTemperatureSensorAvailable: Boolean = true,
    val isGyroscopeAvailable: Boolean = true
)

class SensorViewModel(private val sensorManager: SensorManager) : ViewModel(), SensorEventListener {

    private val _uiState = MutableStateFlow(SensorUiState())
    val uiState = _uiState.asStateFlow()

    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val temperatureSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
    private val gyroscope: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    init {
        _uiState.value = _uiState.value.copy(
            isTemperatureSensorAvailable = temperatureSensor != null,
            isGyroscopeAvailable = gyroscope != null,
            temperatureValue = if (temperatureSensor != null) 0f else null,
            gyroscopeValues = if (gyroscope != null) Triple(0f, 0f, 0f) else null
        )
        registerListeners()
    }

    private fun registerListeners() {
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        temperatureSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        gyroscope?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                _uiState.value = _uiState.value.copy(
                    accelerometerValues = Triple(event.values[0], event.values[1], event.values[2])
                )
            }
            Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                _uiState.value = _uiState.value.copy(
                    temperatureValue = event.values[0]
                )
            }
            Sensor.TYPE_GYROSCOPE -> {
                _uiState.value = _uiState.value.copy(
                    gyroscopeValues = Triple(event.values[0], event.values[1], event.values[2])
                )
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Can be ignored for this example
    }
}

class SensorViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SensorViewModel::class.java)) {
            val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            @Suppress("UNCHECKED_CAST")
            return SensorViewModel(sensorManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
