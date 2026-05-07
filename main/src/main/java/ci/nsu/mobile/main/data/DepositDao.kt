package ci.nsu.mobile.main.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

//запросы к базе данных
@Dao
interface DepositDao {

    // Вставить запись в базу (если есть конфликт - заменить)
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCalculation(calculation: DepositCalculation)

    // Получить все записи, отсортированные по дате (новые сверху)
    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    suspend fun getAllCalculations(): List<DepositCalculation>

    // Получить одну запись по ID
    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    suspend fun getCalculationById(id: Long): DepositCalculation
}