package com.ginogipsy.captaingame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ginogipsy.captaingame.ui.theme.CaptainGameTheme
import kotlin.collections.mutableListOf
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

        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Movements: ${movements.intValue}", color = MaterialTheme.colorScheme.primary)
            Text(text = "Treasure Found: ${treasuresFound.intValue}", color = MaterialTheme.colorScheme.primary)
            Text(text = "Current Direction: ${direction.value}", color = MaterialTheme.colorScheme.primary)
            Text(text = stormOrTreasure.value, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.size(16.dp))
            Row {
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = {
                    movements.value++
                    direction.value = "North"
                    movementList.add("North")
                    if (Random.nextBoolean()) {
                        treasuresFound.value++
                        stormOrTreasure.value = "Found a Treasure!"
                    } else {
                        stormOrTreasure.value = "Storm Ahead!"
                    }
                }) {
                    Text(text = "Sail North")
                }
            }
            Row {
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = {
                    movements.value++
                    direction.value = "West"
                    movementList.add("West")
                    if (Random.nextBoolean()) {
                        treasuresFound.value++
                        stormOrTreasure.value = "Found a Treasure!"
                    } else {
                        stormOrTreasure.value = "Storm Ahead!"
                    }
                }) {
                    Text(text = "Sail West")
                }
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = {
                    movements.value++
                    direction.value = "East"
                    movementList.add("East")
                    if (Random.nextBoolean()) {
                        treasuresFound.value++
                        stormOrTreasure.value = "Found a Treasure!"
                    } else {
                        stormOrTreasure.value = "Storm Ahead!"
                    }
                }) {
                    Text(text = "Sail East")
                }
            }
            Row {
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = {
                    movements.value++
                    direction.value = "South"
                    movementList.add("South")
                    if (Random.nextBoolean()) {
                        treasuresFound.value++
                        stormOrTreasure.value = "Found a Treasure!"
                    } else {
                        stormOrTreasure.value = "Storm Ahead!"
                    }
                }) {
                    Text(text = "Sail South")
                }
            }
            Row {
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = {
                    movements.intValue = 0
                    direction.value = "North"
                    treasuresFound.intValue = 0
                    movementList.clear()
                    stormOrTreasure.value = ""
                }) {
                    Text(text = "Reset")
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            LazyColumn {
                item {
                    Text(text = "Past Directions:", color = MaterialTheme.colorScheme.primary)
                }
                // Mostra ogni movimento presente nella lista
                itemsIndexed(movementList) { index, movement ->
                    Text(text = "${index + 1}) $movement ", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

