package com.arrowwould.brafit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.arrowwould.brafit.model.BraSizeResult
import com.arrowwould.brafit.ui.screens.CalculatorScreen
import com.arrowwould.brafit.ui.screens.MeasurementGuideScreen
import com.arrowwould.brafit.ui.screens.ResultScreen
import com.arrowwould.brafit.ui.theme.BraFitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BraFitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BraFitApp()
                }
            }
        }
    }
}

@Composable
fun BraFitApp() {
    val navController = rememberNavController()
    var calculationResult by remember { mutableStateOf<BraSizeResult?>(null) }

    NavHost(navController = navController, startDestination = "calculator") {
        composable("calculator") {
            CalculatorScreen(
                onResultCalculated = { result ->
                    calculationResult = result
                    navController.navigate("result")
                },
                onShowMeasurementGuide = {
                    navController.navigate("measurement_guide")
                }
            )
        }

        composable("result") {
            calculationResult?.let { result ->
                ResultScreen(
                    result = result,
                    onBackToCalculator = { 
                        navController.popBackStack()
                    }
                )
            }
        }

        composable("measurement_guide") {
            MeasurementGuideScreen(
                onBackToCalculator = { 
                    navController.popBackStack() 
                }
            )
        }
    }
}