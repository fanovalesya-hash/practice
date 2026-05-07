package ci.nsu.mobile.main.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ci.nsu.mobile.main.DepositApplication

//база данных (синглтон)
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
                    DepositApplication.Companion.instance, // Берем контекст из нашего Application
                    AppDatabase::class.java,
                    "deposit_database" // Имя файла базы
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}