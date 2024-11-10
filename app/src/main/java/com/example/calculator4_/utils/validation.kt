package com.example.calculator4_.utils

fun validateInputs(vararg values: String): Boolean {
    return values.all { it.toDoubleOrNull() != null }
}