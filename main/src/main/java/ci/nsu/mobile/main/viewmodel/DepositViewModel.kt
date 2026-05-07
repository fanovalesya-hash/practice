package ci.nsu.mobile.main.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.AppDatabase
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.data.DepositRepository
import ci.nsu.mobile.main.data.DepositCalculator
import kotlinx.coroutines.launch

class DepositViewModel : ViewModel() {

    // Репозиторий для работы с базой данных
    private val repository = DepositRepository(AppDatabase.getDatabase())


    var initialAmount by mutableStateOf("")      // Стартовый взнос
    var periodMonths by mutableStateOf("")       // Срок в месяцах


    var monthlyTopUp by mutableStateOf("")       // Ежемесячное пополнение
    var selectedRate by mutableStateOf<Double?>(null)  // Выбранная ставка


    var resultFinalAmount by mutableStateOf<Double?>(null)    // Итоговая сумма
    var resultTotalInterest by mutableStateOf<Double?>(null)  // Начисленные проценты


    var errorMessage by mutableStateOf<String?>(null)
    var historyList by mutableStateOf<List<DepositCalculation>>(emptyList())
    var selectedCalculation by mutableStateOf<DepositCalculation?>(null)


    fun updateInitialAmount(value: String) {
        initialAmount = value
        errorMessage = null
    }

    fun updatePeriod(value: String) {
        periodMonths = value
        // Автоматически подбираем ставку по сроку
        selectedRate = value.toIntOrNull()?.let { DepositCalculator.getRateForPeriod(it) }
        errorMessage = null
    }

    fun updateMonthlyTopUp(value: String) {
        monthlyTopUp = value
        errorMessage = null
    }


    fun calculateResult() {
        val startAmount = initialAmount.toDoubleOrNull()
        val months = periodMonths.toIntOrNull()
        val topUp = monthlyTopUp.toDoubleOrNull()

        // Проверка валидности данных
        when {
            startAmount == null || startAmount <= 0 -> {
                errorMessage = "Введите корректную сумму"
                return
            }
            months == null || months <= 0 -> {
                errorMessage = "Введите корректный срок"
                return
            }
            selectedRate == null -> {
                errorMessage = "Не удалось определить ставку"
                return
            }
        }

        // Считаем итог
        val (final, interest, _) = DepositCalculator.calculate(
            initialAmount = startAmount,
            periodMonths = months,
            annualRate = selectedRate!!,
            monthlyTopUp = topUp
        )

        resultFinalAmount = final
        resultTotalInterest = interest
        errorMessage = null
    }


    fun saveCalculation() {
        val startAmount = initialAmount.toDoubleOrNull()
        val months = periodMonths.toIntOrNull()
        val topUp = monthlyTopUp.toDoubleOrNull()

        if (startAmount != null && months != null && resultFinalAmount != null) {
            viewModelScope.launch {
                repository.addCalculation(
                    DepositCalculation(
                        initialAmount = startAmount,
                        periodMonths = months,
                        interestRate = selectedRate ?: 0.0,
                        monthlyTopUp = topUp,
                        finalAmount = resultFinalAmount!!,
                        interestEarned = resultTotalInterest!!,
                        calculationDate = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    fun loadHistory() {
        viewModelScope.launch {
            historyList = repository.getAllCalculations()
        }
    }

    fun loadCalculationById(id: Long) {
        viewModelScope.launch {
            selectedCalculation = repository.getCalculationById(id)
        }
    }
}