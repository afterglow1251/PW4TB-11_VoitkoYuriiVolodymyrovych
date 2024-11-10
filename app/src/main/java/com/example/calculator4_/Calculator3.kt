package com.example.calculator4_

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calculator4_.utils.validateInputs
import kotlin.math.*


@Composable
fun Calculator3Screen(modifier: Modifier = Modifier) {

    var Uk_max by remember { mutableStateOf("11.1") }
    var Uv_n by remember { mutableStateOf("115") }
    var Un_n by remember { mutableStateOf("11") }
    var Snom_t by remember { mutableStateOf("6.3") }
    var Rc_n by remember { mutableStateOf("10.65") }
    var Rc_min by remember { mutableStateOf("34.88") }
    var Xc_n by remember { mutableStateOf("24.02") }
    var Xc_min by remember { mutableStateOf("65.68") }
    var L_l by remember { mutableStateOf("12.37") }
    var R_0 by remember { mutableStateOf("0.64") }
    var X_0 by remember { mutableStateOf("0.363") }

    var resultText by remember { mutableStateOf("") }  
    var showResult by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) } 

    Column(
        modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = Uk_max,
            onValueChange = { Uk_max = it },
            label = { Text("Uk_max (кВ)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = Uv_n,
            onValueChange = { Uv_n = it },
            label = { Text("Uv_n (кВ)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = Un_n,
            onValueChange = { Un_n = it },
            label = { Text("Un_n (кВ)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = Snom_t,
            onValueChange = { Snom_t = it },
            label = { Text("Snom_t (МВ*А)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = Rc_n,
            onValueChange = { Rc_n = it },
            label = { Text("Rc_n (Ом)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = Rc_min,
            onValueChange = { Rc_min = it },
            label = { Text("Rc_min (Ом)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = Xc_n,
            onValueChange = { Xc_n = it },
            label = { Text("Xc_n (Ом)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = Xc_min,
            onValueChange = { Xc_min = it },
            label = { Text("Xc_min (Ом)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = L_l,
            onValueChange = { L_l = it },
            label = { Text("L_l (км)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = R_0,
            onValueChange = { R_0 = it },
            label = { Text("R_0 (Ом)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = X_0,
            onValueChange = { X_0 = it },
            label = { Text("X_0 (Ом)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val isValid = validateInputs(
                    Uk_max,
                    Uv_n,
                    Un_n,
                    Snom_t,
                    Rc_n,
                    Rc_min,
                    Xc_n,
                    Xc_min
                )
                if (!isValid) {
                    showErrorDialog = true
                } else {
                    val results = calculateResults(
                        Uk_max.toDouble(),
                        Uv_n.toDouble(),
                        Un_n.toDouble(),
                        Snom_t.toDouble(),
                        Rc_n.toDouble(),
                        Rc_min.toDouble(),
                        Xc_n.toDouble(),
                        Xc_min.toDouble(),
                        L_l.toDouble(),
                        R_0.toDouble(),
                        X_0.toDouble()
                    )

                    resultText = """
                        Xт: %.2f (Ом)
                        Rш: %.2f (Ом)
                        Xш: %.2f (Ом)
                        Zщ: %.2f (Ом)
                        Rщ_min: %.2f (Ом)
                        Xш_min: %.2f (Ом)
                        Zш_min: %.2f (Ом)
                        I3ш: %.2f (А)
                        I2ш: %.2f (А)
                        I3ш_min: %.2f (А)
                        I2ш_min: %.2f (А)
                        kпр: %.2f 
                        Rшн: %.2f (Ом)
                        Xшн: %.2f (Ом)
                        Zшн: %.2f (Ом)
                        Rшн_min: %.2f (Ом)
                        Xшн_min: %.2f (Ом)
                        Zшн_min: %.2f (Ом)
                        I3шн: %.2f (А)
                        I2шн: %.2f (А)
                        I3шн_min: %.2f (А)
                        I2шн_min: %.2f (А)
                        Rл: %.2f (Ом)
                        Xл: %.2f (Ом)
                        RΣн: %.2f (Ом)
                        XΣн: %.2f (Ом)
                        ZΣн: %.2f (Ом)
                        RΣн_min: %.2f (Ом)
                        XΣn_min: %.2f (Ом)
                        ZΣn_min: %.2f (Ом)
                        I3лн: %.2f (А)
                        I2лн: %.2f (А)
                        I3лн_min: %.2f (А)
                        I2лн_min: %.2f (А)
                    """.trimIndent().format(
                        results[0], results[1], results[2], results[3], results[4],
                        results[5], results[6], results[7], results[8], results[9],
                        results[10], results[11], results[12], results[13], results[14],
                        results[15], results[16], results[17], results[18], results[19],
                        results[20], results[21], results[22], results[23], results[24],
                        results[25], results[26], results[27], results[28], results[29],
                        results[30], results[31], results[32], results[33]
                    )

                    showResult = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
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

private fun calculateResults(
    Uk_max: Double,
    Uv_n: Double,
    Un_n: Double,
    Snom_t: Double,
    Rc_n: Double,
    Rc_min: Double,
    Xc_n: Double,
    Xc_min: Double,
    L_l: Double,
    R_0: Double,
    X_0: Double
): List<Double> {
    val Xt = Uk_max * Uv_n.pow(2) / 100 / Snom_t

    val Rsh = Rc_n

    val Xsh = Xc_n + Xt

    val Zsh = sqrt(Rsh.pow(2) + Xsh.pow(2))
    val Rsh_min = Rc_min
    val Xsh_min = Xc_min + Xt
    val Zsh_min = sqrt(Rsh_min.pow(2) + Xsh_min.pow(2))
    val Ish3 = Uv_n * 1000 / sqrt(3.0) / Zsh
    val Ish2 = Ish3 * sqrt(3.0) / 2
    val Ish_min3 = Uv_n * 1000 / sqrt(3.0) / Zsh_min
    val Ish_min2 = Ish_min3 * sqrt(3.0) / 2

    val kpr = Un_n.pow(2) / Uv_n.pow(2)

    val Rsh_n = Rsh * kpr
    val Xsh_n = Xsh * kpr
    val Zsh_n = sqrt(Rsh_n.pow(2) + Xsh_n.pow(2))
    val Rsh_n_min = Rsh_min * kpr
    val Xsh_n_min = Xsh_min * kpr
    val Zsh_n_min = sqrt(Rsh_n_min.pow(2) + Xsh_n_min.pow(2))

    val Ish_n3 = Un_n * 1000 / sqrt(3.0) / Zsh_n
    val Ish_n2 = Ish_n3 * sqrt(3.0) / 2
    val Ish_n_min3 = Un_n * 1000 / sqrt(3.0) / Zsh_n_min
    val Ish_n_min2 = Ish_n_min3 * sqrt(3.0) / 2

    val R_l = L_l * R_0
    val X_l = L_l * X_0

    val R_sum_n = R_l + Rsh_n
    val X_sum_n = X_l + Xsh_n
    val Z_sum_n = sqrt(R_sum_n.pow(2) + X_sum_n.pow(2))



    val R_sum_n_min = R_l + Rsh_n_min
    val X_sum_n_min = X_l + Xsh_n_min

    val Z_sum_n_min = sqrt(R_sum_n_min.pow(2) + X_sum_n_min.pow(2))

    val I_l_n3 = Un_n * 1000 / sqrt(3.0) / Z_sum_n
    val I_l_n2 = I_l_n3 * sqrt(3.0) / 2
    val I_l_n_min3 = Un_n * 1000 / sqrt(3.0) / Z_sum_n_min
    val I_l_n_min2 = I_l_n_min3 * sqrt(3.0) / 2

    return listOf(
        Xt, Rsh, Xsh, Zsh, Rsh_min,
        Xsh_min, Zsh_min, Ish3, Ish2, Ish_min3,
        Ish_min2, kpr, Rsh_n, Xsh_n, Zsh_n,
        Rsh_n_min, Xsh_n_min, Zsh_n_min, Ish_n3, Ish_n2,
        Ish_n_min3, Ish_n_min2, R_l, X_l, R_sum_n,
        X_sum_n, Z_sum_n, R_sum_n_min, X_sum_n_min, Z_sum_n_min,
        I_l_n3, I_l_n2, I_l_n_min3, I_l_n_min2
    )
}
