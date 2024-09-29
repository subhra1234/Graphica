package com.example.graphica.GraphView

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GraphV_ViewModel:ViewModel() {
    var zoom by mutableStateOf(0f)
}