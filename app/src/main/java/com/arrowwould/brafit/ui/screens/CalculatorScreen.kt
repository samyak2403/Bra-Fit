package com.arrowwould.brafit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.arrowwould.brafit.model.BraSizeCalculator
import com.arrowwould.brafit.model.BraSizeResult
import com.google.rpc.Help

@Composable
fun CalculatorScreen(
    onResultCalculated: (BraSizeResult) -> Unit,
    onShowMeasurementGuide: () -> Unit
) {
    var bustMeasurement by remember { mutableStateOf("") }
    var underBustMeasurement by remember { mutableStateOf("") }
    var isInches by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Add unit suffix based on measurement type
    val unitSuffix = if (isInches) "in" else "cm"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCE4EC))  // Light pink background
            .padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Bra Size Calculator",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Measurement Unit",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF2196F3)  // Blue text color
                )
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Row(
                modifier = Modifier
                    .height(36.dp)
                    .clip(RoundedCornerShape(18.dp))
            ) {
                Box(
                    modifier = Modifier
                        .clickable { isInches = true }
                        .background(
                            if (isInches) Color(0xFFE91E63) else Color.Transparent
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Inches",
                        color = if (isInches) Color.White else Color.Gray
                    )
                }
                
                Box(
                    modifier = Modifier
                        .clickable { isInches = false }
                        .background(
                            if (!isInches) Color(0xFFE91E63) else Color.Transparent
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "CM",
                        color = if (!isInches) Color.White else Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Bust Size",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = bustMeasurement,
            onValueChange = { input ->
                // Only allow numbers and decimal point
                if (input.isEmpty() || input.matches(Regex("^\\d*\\.?\\d*$"))) {
                    bustMeasurement = input
                    errorMessage = null
                }
            },
            placeholder = { Text("Enter bust size") },
            suffix = { Text(unitSuffix, color = Color.Gray) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color(0xFFE91E63)
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Underbust Size",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = underBustMeasurement,
            onValueChange = { input ->
                // Only allow numbers and decimal point
                if (input.isEmpty() || input.matches(Regex("^\\d*\\.?\\d*$"))) {
                    underBustMeasurement = input
                    errorMessage = null
                }
            },
            placeholder = { Text("Enter underbust size") },
            suffix = { Text(unitSuffix, color = Color.Gray) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color(0xFFE91E63)
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        errorMessage?.let { message ->
            Text(
                text = message,
                color = Color(0xFFE91E63),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = {
                val bust = bustMeasurement.trim().toDoubleOrNull()
                val underbust = underBustMeasurement.trim().toDoubleOrNull()

                when {
                    bust == null || bustMeasurement.isEmpty() -> {
                        errorMessage = "Please enter a valid bust measurement"
                    }
                    underbust == null || underBustMeasurement.isEmpty() -> {
                        errorMessage = "Please enter a valid underbust measurement"
                    }
                    bust <= 0 || underbust <= 0 -> {
                        errorMessage = "Measurements must be greater than zero"
                    }
                    bust < underbust -> {
                        errorMessage = "Bust measurement must be larger than underbust measurement"
                    }
                    bust > 200 || underbust > 200 -> {
                        errorMessage = "Measurements seem too large. Please check your input"
                    }
                    else -> {
                        errorMessage = null
                        val result = BraSizeCalculator.calculateBraSize(
                            if (isInches) bust else bust * 0.393701, // CM to inches
                            if (isInches) underbust else underbust * 0.393701
                        )
                        onResultCalculated(result)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE91E63)  // Pink button color
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                "Calculate Size",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
} 