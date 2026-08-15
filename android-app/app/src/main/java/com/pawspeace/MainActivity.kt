package com.pawspeace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.pawspeace.ui.screens.HomeScreen
import com.pawspeace.ui.theme.PawsPeaceTheme
import com.pawspeace.viewmodel.HomeViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PawsPeaceTheme {
                HomeScreen(viewModel = viewModel)
            }
        }
    }
}
