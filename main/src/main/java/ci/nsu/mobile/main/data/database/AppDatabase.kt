package ci.nsu.mobile.main.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ci.nsu.mobile.main.DepositApplication
import ci.nsu.mobile.main.data.dao.DepositDao
import ci.nsu.mobile.main.data.model.DepositCalculation

@Database(entities = [DepositCalculation::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // Получаем доступ к DAO
    abstract fun depositDao(): DepositDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Функция для получения единственного экземпляра базы данных
        fun getDatabase(): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    DepositApplication.instance, // Берем контекст из нашего Application
                    AppDatabase::class.java,
                    "deposit_database" // Имя файла базы
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}