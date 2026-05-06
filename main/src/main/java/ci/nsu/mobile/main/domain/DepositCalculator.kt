package ci.nsu.mobile.main.domain

object DepositCalculator {

    fun getRateForPeriod(months: Int): Double? {
        return when {
            months < 6 -> 15.0
            months in 6..11 -> 10.0
            months >= 12 -> 5.0
            else -> null // Если срок 0 или отрицательный
        }
    }


    fun calculate(
        initialAmount: Double,
        periodMonths: Int,
        annualRate: Double,
        monthlyTopUp: Double?
    ): Triple<Double, Double, Double> {
        var currentBalance = initialAmount
        var totalInterest = 0.0

        // Месячная ставка = годовая / 100 / 12 месяцев
        val monthlyRate = annualRate / 100.0 / 12.0

        // Проходим цикл по каждому месяцу срока
        repeat(periodMonths) {
            // 1. Начисляем проценты на текущую сумму
            val interest = currentBalance * monthlyRate
            totalInterest += interest
            currentBalance += interest

            // 2. Добавляем пополнение (если есть)
            monthlyTopUp?.let {
                currentBalance += it
            }
        }

        return Triple(currentBalance, totalInterest, annualRate)
    }
}