package ru.effectivemobile.db.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.effectivemobile.db.data.dao.*
import ru.effectivemobile.db.data.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Flower::class, Bouquet::class, BouquetFlower::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun flowerDao(): FlowerDao
    abstract fun bouquetDao(): BouquetDao
    abstract fun bouquetFlowerDao(): BouquetFlowerDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE flowers ADD COLUMN country_of_origin TEXT")
                database.execSQL("ALTER TABLE bouquets ADD COLUMN packaging_type TEXT")

                database.execSQL("UPDATE flowers SET country_of_origin = 'Неизвестная страна' WHERE country_of_origin IS NULL")
                database.execSQL("UPDATE bouquets SET packaging_type = 'Стандартная упаковка' WHERE packaging_type IS NULL")
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "flower_shop.db"
                ).addCallback(DatabaseCallback())
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database)
                    }
                }
            }

            suspend fun populateInitialData(db: AppDatabase) {
                val flowerDao = db.flowerDao()
                val bouquetDao = db.bouquetDao()
                val bouquetFlowerDao = db.bouquetFlowerDao()


                val flowers = listOf(
                    Flower(name = "Белая роза", stock = 50, countryOfOrigin = "Нидерланды"),
                    Flower(name = "Красная роза", stock = 45, countryOfOrigin = "Эквадор"),
                    Flower(name = "Розовая роза", stock = 30, countryOfOrigin = "Нидерланды"),
                    Flower(name = "Жёлтая роза", stock = 20, countryOfOrigin = "Нидерланды"),
                    Flower(name = "Тюльпан", stock = 100, countryOfOrigin = "Нидерланды"),
                    Flower(name = "Лилия", stock = 25, countryOfOrigin = "Нидерланды"),
                    Flower(name = "Хризантема", stock = 40, countryOfOrigin = "Нидерланды"),
                    Flower(name = "Гербера", stock = 35, countryOfOrigin = "Нидерланды"),
                    Flower(name = "Орхидея", stock = 15, countryOfOrigin = "Нидерланды"),
                    Flower(name = "Пион", stock = 28, countryOfOrigin = "Нидерланды")
                )
                val flowerIds = flowerDao.insertAll(flowers)


                val bouquets = listOf(
                    Bouquet(name = "Нежность", price = 15.99, description = "3 белые розы, 2 тюльпана", packagingType = "Крафтовая бумага"),
                    Bouquet(name = "Страсть", price = 25.50, description = "10 красных роз", packagingType = "Корзина"),
                    Bouquet(name = "Весенний", price = 19.90, description = "5 тюльпанов, 3 герберы", packagingType = "Плёнка")
                )
                val bouquetIds = bouquetDao.insertAll(bouquets)


                bouquetFlowerDao.insert(BouquetFlower(bouquetIds[0], flowerIds[0], 3))
                bouquetFlowerDao.insert(BouquetFlower(bouquetIds[0], flowerIds[4], 2))

                bouquetFlowerDao.insert(BouquetFlower(bouquetIds[1], flowerIds[1], 10))

                bouquetFlowerDao.insert(BouquetFlower(bouquetIds[2], flowerIds[4], 5))
                bouquetFlowerDao.insert(BouquetFlower(bouquetIds[2], flowerIds[7], 3))
            }
        }
    }
}

