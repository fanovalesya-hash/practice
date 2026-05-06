package ci.nsu.mobile.main.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.database.AppDatabase
import ci.nsu.mobile.main.data.model.DepositCalculation
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.domain.DepositCalculator
import kotlinx.coroutines.launch

class DepositViewModel : ViewModel() {

    // Получаем репозиторий (так как у нас синглтон, это безопасно)
    private val repository = DepositRepository(AppDatabase.getDatabase())

    // === СОСТОЯНИЕ (State) ===
    // Эти переменные автоматически обновляют экран при изменении

    // Экран 1: Ввод
    var initialAmount by mutableStateOf("")

    var periodMonths by mutableStateOf("")

    // Экран 2: Настройки
    var monthlyTopUp by mutableStateOf("")

    var selectedRate by mutableStateOf<Double?>(null)

    // Результат
    var resultFinalAmount by mutableStateOf<Double?>(null)

    var resultTotalInterest by mutableStateOf<Double?>(null)

    var errorMessage by mutableStateOf<String?>(null)

    var historyList by mutableStateOf<List<DepositCalculation>>(emptyList())

    var selectedCalculation by mutableStateOf<DepositCalculation?>(null)

    // === ДЕЙСТВИЯ (Actions) ===

    // Обновление стартового взноса
    fun updateInitialAmount(value: String) {
        initialAmount = value
        errorMessage = null
    }

    // Обновление срока и автоматический расчёт ставки
    fun updatePeriod(value: String) {
        periodMonths = value
        val months = value.toIntOrNull()

        // Автоматический выбор ставки по условию ТЗ
        if (months != null) {
            selectedRate = DepositCalculator.getRateForPeriod(months)
        }
        errorMessage = null
    }

    fun setError(message: String) {
        errorMessage = message
    }
    // Обновление ежемесячного пополнения
    fun updateMonthlyTopUp(value: String) {
        monthlyTopUp = value
        errorMessage = null
    }

    // --- ПЕРЕХОД К РАСЧЁТУ ---
    fun calculateResult() {
        // 1. Валидация
        val startAmount = initialAmount.toDoubleOrNull()
        val months = periodMonths.toIntOrNull()
        val topUp = monthlyTopUp.toDoubleOrNull()

        if (startAmount == null || startAmount <= 0) {
            errorMessage = "Введите корректную сумму стартового взноса"
            return
        }
        if (months == null || months <= 0) {
            errorMessage = "Введите корректный срок вклада"
            return
        }
        if (selectedRate == null) {
            errorMessage = "Не удалось определить ставку для указанного срока"
            return
        }

        // 2. Расчёт
        val (final, interest, _) = DepositCalculator.calculate(
            initialAmount = startAmount,
            periodMonths = months,
            annualRate = selectedRate!!,
            monthlyTopUp = topUp
        )

        // 3. Сохранение результатов в состояние
        resultFinalAmount = final
        resultTotalInterest = interest
        errorMessage = null
    }

    // --- СОХРАНЕНИЕ В БАЗУ ---
    fun saveCalculation() {
        val startAmount = initialAmount.toDoubleOrNull()
        val months = periodMonths.toIntOrNull()
        val topUp = monthlyTopUp.toDoubleOrNull()

        if (startAmount != null && months != null && resultFinalAmount != null && resultTotalInterest != null) {

            // Запускаем корутину, чтобы не блокировать экран
            viewModelScope.launch {
                val calculation = ci.nsu.mobile.main.data.model.DepositCalculation(
                    initialAmount = startAmount,
                    periodMonths = months,
                    interestRate = selectedRate ?: 0.0,
                    monthlyTopUp = topUp,
                    finalAmount = resultFinalAmount!!,
                    interestEarned = resultTotalInterest!!,
                    calculationDate = System.currentTimeMillis()
                )

                try {
                    repository.addCalculation(calculation)
                    Log.d("DepositViewModel", "Сохранено в базу!")
                } catch (e: Exception) {
                    Log.e("DepositViewModel", "Ошибка сохранения: ${e.message}")
                }
            }
        }
    }

    fun loadHistory() {
        viewModelScope.launch {
            try {
                historyList = repository.getAllCalculations()
            } catch (e: Exception) {
                Log.e("DepositViewModel", "Ошибка загрузки истории: ${e.message}")
            }
        }
    }

    fun loadCalculationById(id: Long) {
        viewModelScope.launch {
            try {
                selectedCalculation = repository.getCalculationById(id)
            } catch (e: Exception) {
                Log.e("DepositViewModel", "Ошибка загрузки расчёта: ${e.message}")
            }
        }
    }
    fun clearSelectedCalculation() {
        selectedCalculation = null
    }

}