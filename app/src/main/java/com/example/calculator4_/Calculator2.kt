package com.example.calculator4_

import com.example.calculator4_.utils.validateInputs
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.*

@Composable
fun Calculator2Screen(modifier: Modifier = Modifier) {

    var Ucn by remember { mutableStateOf("10.5") }
    var Sk by remember { mutableStateOf("200") }
    var Uk_perc by remember { mutableStateOf("10.5") }
    var S_nom_t by remember { mutableStateOf("6.3") }

    var resultText by remember { mutableStateOf("") }  
    var showResult by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) } 

    Column(
        modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 16.dp)
            .verticalScroll(rememberScrollState())  ,
            verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = Ucn,
            onValueChange = { Ucn = it },
            label = { Text("Ucn (кВ)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = Sk,
            onValueChange = { Sk = it },
            label = { Text("Sk (МВ*А)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = Uk_perc,
            onValueChange = { Uk_perc = it },
            label = { Text("Uk_perc (кВ)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = S_nom_t,
            onValueChange = { S_nom_t = it },
            label = { Text("S_nom_t (МВ*А)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val isValid = validateInputs(Ucn, Sk, Uk_perc, S_nom_t)
                if (!isValid) {
                    showErrorDialog = true
                } else {
                    val (Xc, Xt, X_sum, Ip0) = calculateResults(
                        Ucn.toDouble(),
                        Sk.toDouble(),
                        Uk_perc.toDouble(),
                        S_nom_t.toDouble()
                    )

                    resultText = """
                        Xс: %.2f (Ом)
                        Xт: %.2f (Ом)
                        XΣ: %.2f (Ом)
                        Iп0: %.2f (кА)
                    """.trimIndent().format(Xc, Xt, X_sum, Ip0)

                    showResult = true
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Обчислити")
        }

        if (showResult) {
            Text(
                text = resultText,
                modifier = Modifier.padding(top = 8.dp) 
            )
        }
    }

    if (showErrorDialog) ErrorDialog(onDismiss = { showErrorDialog = false })
}

private fun calculateResults(Ucn: Double, Sk: Double, Uk_perc: Double, S_nom_t: Double): List<Double> {
    val Xc = Ucn.pow(2) / Sk
    val Xt = Uk_perc * Ucn.pow(2) / S_nom_t / 100
    val X_sum = Xc + Xt
    val Ip0 = Ucn / (sqrt(3.0) * X_sum)

    return listOf(Xc, Xt, X_sum, Ip0)
}