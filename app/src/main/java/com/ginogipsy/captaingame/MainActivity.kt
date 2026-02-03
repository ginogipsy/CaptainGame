package com.ginogipsy.captaingame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.ginogipsy.captaingame.ui.theme.CaptainGameTheme
import kotlinx.coroutines.launch
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
        var treasuresFound by remember { mutableIntStateOf(0) }
        val movements = remember { mutableIntStateOf(0) }
        val direction = remember { mutableStateOf("North") }
        val stormOrTreasure = remember { mutableStateOf("") }
        val movementList = remember { mutableStateListOf<String>() }
        val haptic = LocalHapticFeedback.current
        val shakeOffset = remember { Animatable(0f) } // Gestisce lo spostamento laterale
        val scope = rememberCoroutineScope() // Necessario per lanciare animazioni

        // Funzione definita PRIMA dell'uso nei Button
        fun buttonClick(movement: String) {
            movements.intValue++
            direction.value = movement
            movementList.add(movement)

            val randomInt = Random.nextInt(10)
            when (randomInt) {
                in 0..2 -> {
                    treasuresFound++
                    stormOrTreasure.value = "Found a Treasure! 💰"
                    // Vibrazione corta per il tesoro
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }

                in 3..7 -> stormOrTreasure.value = "Nothing found... 🌊"
                else -> {
                    stormOrTreasure.value = "Storm Ahead! ⛈️"
                    treasuresFound = (treasuresFound - 2).coerceAtLeast(0)
                    // Vibrazione doppia o diversa per la tempesta (se supportata)
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                    // --- ANIMAZIONE SHAKE ---
                    scope.launch {
                        repeat(1) { // Oscilla 1 volta
                            shakeOffset.animateTo(
                                20f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy)
                            )
                            shakeOffset.animateTo(
                                -20f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy)
                            )
                        }
                        shakeOffset.animateTo(0f) // Torna al centro
                    }
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- SEZIONE STATISTICHE ---
            Text(text = "Movements: ${movements.intValue}")
            Text(text = "Treasure Found: $treasuresFound")
            Text(text = "Direction: ${direction.value}")

            Spacer(modifier = Modifier.height(24.dp)) // Spazio dopo le scritte

            // Messaggio dell'evento (Tempesta/Tesoro) con spazio dedicato
            Box(modifier = Modifier.height(60.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = stormOrTreasure.value,
                    modifier = Modifier.offset(x = shakeOffset.value.dp), // Applica lo scuotimento qui!
                    color = when {
                        stormOrTreasure.value.contains("Treasure") -> Color(0xFF4CAF50)
                        stormOrTreasure.value.contains("Storm") -> Color.Red
                        else -> MaterialTheme.colorScheme.primary
                    },
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Spacer(modifier = Modifier.height(40.dp)) // Grande spazio prima dei bottoni

            // --- AREA PULSANTI (Layout a Croce) ---
            Button(onClick = { buttonClick("North") }) { Text("Sail North") }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { buttonClick("West") }) { Text("Sail West") }
                Spacer(modifier = Modifier.width(32.dp)) // Più spazio tra i bottoni laterali
                Button(onClick = { buttonClick("East") }) { Text("Sail East") }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { buttonClick("South") }) { Text("Sail South") }

            Spacer(modifier = Modifier.height(32.dp)) // Spazio prima della lista
            Row (verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = {
                    movements.intValue = 0
                    direction.value = "North"
                    treasuresFound = 0
                    movementList.clear()
                    stormOrTreasure.value = ""
                }) {
                    Text(text = "Reset Game")
                }
            }


            // --- LISTA STORICO ---
            LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
                item { Text("Bord Log:", style = MaterialTheme.typography.labelLarge) }
                itemsIndexed(movementList) { index, m ->
                    Text("${index + 1}) $m", modifier = Modifier.padding(4.dp))
                }
            }
        }
    }
}

