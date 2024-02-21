package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.test.datastore.UserStore
import com.example.test.ui.theme.TestTheme
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userStore = UserStore(context)

    var selectedCity by remember { mutableStateOf(CityData.cities[0]) }

    val temperature = selectedCity.temperature
    val wind = selectedCity.wind
    val description = selectedCity.description

    val details by userStore.getDetails.collectAsState(initial = "")
    var fetchedDetails by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            var expanded by remember { mutableStateOf(false) }

            Text(
                text = selectedCity.name,
                modifier = Modifier
                    .clickable { expanded = true }
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp)
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.primary)
            ) {
                CityData.cities.forEach { city ->
                    DropdownMenuItem(
                        onClick = {
                            selectedCity = city
                            expanded = false
                        },
                        text = { Text(text = city.name) },
                        modifier = Modifier.background(Color.LightGray) // Optional custom background
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))


        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            onClick = {
                scope.launch {

                    userStore.saveDetails(
                        selectedCity.name,
                        Date(),
                        selectedCity.temperature,
                        selectedCity.wind,
                        selectedCity.description
                    )
                }
            }
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Add to datastore", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            onClick = {
                fetchedDetails = details
            }
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Show Weather Info", fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = fetchedDetails, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }

}

data class City(val name: String, val temperature: Int, val wind: String, val description: String)

object CityData {
    val cities = listOf(
        City("Toronto", 25, "10 km/h", "Sunny day"),
        City("Vancouver", 18, "5 km/h", "Cloudy with showers"),
        City("Montreal", 10, "15 km/h", "Rainy"),
        City("Calgary", 5, "20 km/h", "Snowy"),
        City("Ottawa", -2, "5 km/h", "Clear and cold")
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}
