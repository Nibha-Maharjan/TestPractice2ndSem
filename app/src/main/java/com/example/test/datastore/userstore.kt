package com.example.test.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

class UserStore(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("UserData")

        val CITY_NAME = stringPreferencesKey("city_name")
        val DATE = stringPreferencesKey("date")
        val TEMPERATURE = stringPreferencesKey("temperature")
        val WIND = stringPreferencesKey("wind")
        val DESCRIPTION = stringPreferencesKey("description")
    }

    val getDetails: Flow<String> = context.dataStore.data
        .map { preferences ->
            val cityName = preferences[CITY_NAME] ?: ""
            val date = preferences[DATE] ?: ""
            val temperature = preferences[TEMPERATURE] ?: ""
            val wind = preferences[WIND] ?: ""
            val description = preferences[DESCRIPTION] ?: ""

            "City Name: $cityName\nDate: $date\nTemperature: $temperature\nWind: $wind\nDescription: $description"
        }

    suspend fun saveDetails(cityName: String, date: Date, temperature: Int, wind: String, description: String) {
        context.dataStore.edit { preferences ->
            preferences[CITY_NAME] = cityName
            preferences[DATE] = date.toString()
            preferences[TEMPERATURE] = temperature.toString()
            preferences[WIND] = wind
            preferences[DESCRIPTION] = description
        }
    }
}
