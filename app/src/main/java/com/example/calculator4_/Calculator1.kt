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

enum class ConductorType(val displayName: String) {
    UNSHIELDED("Неізольовані проводи та шини"),
    PAPER_AND_RUBBER_CABLES("Кабелі з паперовою і проводи з гумовою та полівінілхлоридною ізоляцією з жилами"),
    RUBBER_AND_PLASTIC_CABLES("Кабелі з гумовою та пластмасовою ізоляцією з жилами")
}

enum class ConductorMaterial(val displayName: String) {
    COPPER("Мідь"),
    ALUMINUM("Алюміній")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calculator1Screen(modifier: Modifier = Modifier) {
    var Unom by remember { mutableStateOf("10") }
    var Ik by remember { mutableStateOf("2.5") }
    var Sm by remember { mutableStateOf("1300") }
    var P_TP by remember { mutableStateOf("2000") }
    var tf by remember { mutableStateOf("2.5") }
    var Tm by remember { mutableStateOf("4000") }
    var Ct by remember { mutableStateOf("92") }

    var conductorTypeExpanded by remember { mutableStateOf(false) }
    var conductorType by remember { mutableStateOf(ConductorType.UNSHIELDED) }
    val conductorTypeOptions = ConductorType.entries

    var conductorMaterialExpanded by remember { mutableStateOf(false) }
    var conductorMaterial by remember { mutableStateOf(ConductorMaterial.ALUMINUM) }
    val conductorMaterialOptions = ConductorMaterial.entries

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
            value = Unom,
            onValueChange = { Unom = it },
            label = { Text("Unom (кВ)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = Sm,
            onValueChange = { Sm = it },
            label = { Text("Sm (кВ*А)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = Ik,
            onValueChange = { Ik = it },
            label = { Text("Ik (кА)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = P_TP,
            onValueChange = { P_TP = it },
            label = { Text("P_TP (кВ*А)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = tf,
            onValueChange = { tf = it },
            label = { Text("Tf (с)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = Tm,
            onValueChange = { Tm = it },
            label = { Text("Tm (год)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = Ct,
            onValueChange = { Ct = it },
            label = { Text("Cт") },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
            expanded = conductorTypeExpanded,
            onExpandedChange = { conductorTypeExpanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = conductorType.displayName,
                onValueChange = {},
                label = { Text("Тип провідника") },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = conductorTypeExpanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = conductorTypeExpanded,
                onDismissRequest = { conductorTypeExpanded = false }
            ) {
                conductorTypeOptions.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            conductorType = option
                            conductorTypeExpanded = false
                        },
                        text = { Text(option.displayName) }
                    )
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = conductorMaterialExpanded,
            onExpandedChange = { conductorMaterialExpanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = conductorMaterial.displayName,
                onValueChange = {},
                label = { Text("Матеріал провідника") },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = conductorMaterialExpanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = conductorMaterialExpanded,
                onDismissRequest = { conductorMaterialExpanded = false }
            ) {
                conductorMaterialOptions.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            conductorMaterial = option
                            conductorMaterialExpanded = false
                        },
                        text = { Text(option.displayName) }
                    )
                }
            }
        }

        Button(
            onClick = {
                val isValid = validateInputs(Unom, Ik, P_TP, Sm, Tm, tf, Ct)
                if (!isValid) {
                    showErrorDialog = true
                } else {
                    val (Im, Im_pa, Sek, Smin) = calculateResults(
                        Unom.toDouble(),
                        Sm.toDouble(),
                        Tm.toDouble(),
                        tf.toDouble(),
                        Ik.toDouble(),
                        Ct.toDouble(),
                        conductorType,
                        conductorMaterial
                    )

                    resultText = """
                        Iм: %.2f (А)
                        Iм.па: %.2f (А)
                        Sек: %.2f (мм²)
                        Smin: %.2f (мм²)
                    """.trimIndent().format(Im, Im_pa, Sek, Smin)

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

private fun calculateResults(
    Unom: Double,
    Sm: Double,
    Tm: Double,
    tf: Double,
    Ik: Double,
    Ct: Double,
    conductorType: ConductorType,
    conductorMaterial: ConductorMaterial
): List<Double> {
    val Im = Sm / 2.0 / sqrt(3.0) / Unom
    val Im_pa = 2 * Im
    val jek = getJek(conductorType, conductorMaterial, Tm)
    val Sek = if (jek > 0.0) Im / jek else 0.0

    val Smin = Ik * 1000 * sqrt(tf) / Ct

    return listOf(Im, Im_pa, Sek, Smin)
}

// Визначаємо значення економічної густини струму (jek) на основі типу і матеріалу провідника
fun getJek(conductorType: ConductorType, conductorMaterial: ConductorMaterial, Tm: Double): Double {
    val jekValues = mapOf(
        ConductorType.UNSHIELDED to mapOf(
            ConductorMaterial.COPPER to listOf(
                Pair(1000.0, 3000.0) to 2.5,
                Pair(3000.0, 5000.0) to 2.1,
                Pair(5000.0, Double.MAX_VALUE) to 1.8
            ),
            ConductorMaterial.ALUMINUM to listOf(
                Pair(1000.0, 3000.0) to 1.3,
                Pair(3000.0, 5000.0) to 1.1,
                Pair(5000.0, Double.MAX_VALUE) to 1.0
            )
        ),
        ConductorType.PAPER_AND_RUBBER_CABLES to mapOf(
            ConductorMaterial.COPPER to listOf(
                Pair(1000.0, 3000.0) to 3.0,
                Pair(3000.0, 5000.0) to 2.5,
                Pair(5000.0, Double.MAX_VALUE) to 2.0
            ),
            ConductorMaterial.ALUMINUM to listOf(
                Pair(1000.0, 3000.0) to 1.6,
                Pair(3000.0, 5000.0) to 1.4,
                Pair(5000.0, Double.MAX_VALUE) to 1.2
            )
        ),
        ConductorType.RUBBER_AND_PLASTIC_CABLES to mapOf(
            ConductorMaterial.COPPER to listOf(
                Pair(1000.0, 3000.0) to 3.5,
                Pair(3000.0, 5000.0) to 3.1,
                Pair(5000.0, Double.MAX_VALUE) to 2.7
            ),
            ConductorMaterial.ALUMINUM to listOf(
                Pair(1000.0, 3000.0) to 1.9,
                Pair(3000.0, 5000.0) to 1.7,
                Pair(5000.0, Double.MAX_VALUE) to 1.6
            )
        )
    )

    return jekValues[conductorType]?.get(conductorMaterial)
        ?.firstOrNull { (range, _) -> Tm in range.first..range.second }
        ?.second ?: throw IllegalArgumentException("No jek value found for the given parameters")
}


