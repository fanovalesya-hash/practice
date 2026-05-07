package ci.nsu.mobile.main.data


class DepositRepository(private val database: AppDatabase) {

    // Метод для добавления расчёта в историю
    suspend fun addCalculation(calculation: DepositCalculation) {
        database.depositDao().insertCalculation(calculation)
    }

    // Метод для получения всей истории расчётов
    suspend fun getAllCalculations(): List<DepositCalculation> {
        return database.depositDao().getAllCalculations()
    }

    // Метод для получения одного расчёта по ID
    suspend fun getCalculationById(id: Long): DepositCalculation {
        return database.depositDao().getCalculationById(id)
    }
}