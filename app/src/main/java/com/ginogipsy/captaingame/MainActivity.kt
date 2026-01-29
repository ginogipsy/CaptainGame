package com.ginogipsy.captaingame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ginogipsy.captaingame.ui.theme.CaptainGameTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CaptainGameTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CaptainGame()
                }
            }
        }
    }

    @Composable
    fun CaptainGame() {
        val treasuresFound = remember { mutableIntStateOf(0) }
        val movements = remember { mutableIntStateOf(0) }
        val direction = remember { mutableStateOf("North") }
        val stormOrTreasure = remember { mutableStateOf("") }
        val movementList = remember { mutableStateListOf<String>() }

        // Funzione definita PRIMA dell'uso nei Button
        fun buttonClick(movement: String) {
            movements.intValue++
            direction.value = movement
            movementList.add(movement)

            val randomInt = Random.nextInt(10)
            when (randomInt) {
                in 0..2 -> {
                    treasuresFound.intValue++
                    stormOrTreasure.value = "Found a Treasure! 💰"
                }

                in 3..7 -> stormOrTreasure.value = "Nothing found... 🌊"
                else -> {
                    stormOrTreasure.value = "Storm Ahead! ⛈️"
                    treasuresFound.intValue = (treasuresFound.intValue - 2).coerceAtLeast(0)
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top, // Inizia dall'alto
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Movements: ${movements.intValue}", color = MaterialTheme.colorScheme.primary)
            Text(text = "Treasure Found: ${treasuresFound.intValue}", color = MaterialTheme.colorScheme.primary)
            Text(text = "Current Direction: ${direction.value}", color = MaterialTheme.colorScheme.primary)

            // Messaggio colorato
            Text(
                text = stormOrTreasure.value,
                color = when {
                    stormOrTreasure.value.contains("Treasure") -> androidx.compose.ui.graphics.Color(0xFF4CAF50) // Verde
                    stormOrTreasure.value.contains("Storm") -> androidx.compose.ui.graphics.Color.Red
                    else -> MaterialTheme.colorScheme.secondary
                }
            )

            Row {
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = {
                    buttonClick("North")
                }) {
                    Text(text = "Sail North")
                }
            }
            Row {
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = {
                    buttonClick("West")
                }) {
                    Text(text = "Sail West")
                }
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = {
                    buttonClick("East")
                }) {
                    Text(text = "Sail East")
                }
            }
            Row {
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = {
                    buttonClick("South")
                }) {
                    Text(text = "Sail South")
                }
            }

            Button(onClick = {
                movements.intValue = 0
                direction.value = "North"
                treasuresFound.intValue = 0
                movementList.clear()
                stormOrTreasure.value = ""
            }) {
                Text(text = "Reset Game")
            }

            Spacer(modifier = Modifier.size(16.dp))

            // LazyColumn con peso per scorrere correttamente
            LazyColumn(modifier = Modifier.weight(1f)) {
                item {
                    Text(text = "Past Directions:", style = MaterialTheme.typography.titleMedium)
                }
                itemsIndexed(movementList) { index, movement ->
                    Text(text = "${index + 1}) $movement ", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

