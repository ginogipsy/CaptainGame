package com.ginogipsy.captaingame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
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
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center, // Centra il contenuto verticalmente
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Statistiche in alto
            Text(text = "Movements: ${movements.intValue}", style = MaterialTheme.typography.headlineSmall)
            Text(text = "Treasure Found: ${treasuresFound.intValue}", style = MaterialTheme.typography.headlineSmall)

            // Messaggio dell'evento (Tempesta/Tesoro) con spazio dedicato
            Box(modifier = Modifier.height(60.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = stormOrTreasure.value,
                    color = when {
                        stormOrTreasure.value.contains("Treasure") -> Color.Green
                        stormOrTreasure.value.contains("Storm") -> Color.Red
                        else -> MaterialTheme.colorScheme.primary
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp)) // Spazio extra prima dei bottoni

            // --- AREA PULSANTI ---
            Button(onClick = { buttonClick("North") }) { Text("Sail North") }

            Row(horizontalArrangement = Arrangement.Center) {
                Button(onClick = { buttonClick("West") }) { Text("Sail West") }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { buttonClick("East") }) { Text("Sail East") }
            }

            Button(onClick = { buttonClick("South") }) { Text("Sail South") }
            // ---------------------

            Spacer(modifier = Modifier.height(24.dp))

            // Lista dei movimenti con peso 1f per occupare lo spazio rimanente in basso
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        text = "Log di Bordo",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                itemsIndexed(movementList) { index, movement ->
                    Text(text = "${index + 1}) $movement")
                }
            }
        }
    }
}

