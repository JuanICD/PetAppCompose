package com.example.petapp.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen() {
    Scaffold() {
        LazyColumn() {
            items(10) { index ->
                Column {
                    Text("Item $index")
                }
            }
        }
    }
}