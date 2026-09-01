package com.nastech.nia.ui.screens.passwords

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nastech.nia.core.security.BreachChecker
import com.nastech.nia.core.security.PasswordStrengthAnalyzer

class PasswordCheckViewModel : ViewModel() {

    private val _level = mutableStateOf(PasswordStrengthAnalyzer.Level.WEAK)
    val level: State<PasswordStrengthAnalyzer.Level> = _level

    private val _entropy = mutableStateOf(0)
    val entropy: State<Int> = _entropy

    private val _breached = mutableStateOf(false)
    val breached: State<Boolean> = _breached

    private val _checked = mutableStateOf(false)
    val checked: State<Boolean> = _checked

    fun analyze(password: String) {
        _level.value = PasswordStrengthAnalyzer.analyze(password)
        _entropy.value = PasswordStrengthAnalyzer.entropy(password)
        _checked.value = password.isNotBlank()
    }

    fun checkBreach(password: String) {
        _breached.value = BreachChecker.isPasswordBreached(password)
        _checked.value = true
    }

    fun scorePct(): Int = when (_level.value) {
        PasswordStrengthAnalyzer.Level.WEAK -> 20
        PasswordStrengthAnalyzer.Level.FAIR -> 45
        PasswordStrengthAnalyzer.Level.GOOD -> 70
        PasswordStrengthAnalyzer.Level.STRONG -> 95
    }

    companion object {
        fun factory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T = PasswordCheckViewModel() as T
            }
    }
}