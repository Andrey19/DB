package ru.effectivemobile.db.data.repository

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import ru.effectivemobile.db.data.dao.BouquetDao
import ru.effectivemobile.db.data.dao.BouquetFlowerDao
import ru.effectivemobile.db.data.dao.BouquetWithAvailability
import ru.effectivemobile.db.data.dao.FlowerDao
import ru.effectivemobile.db.data.database.AppDatabase
import ru.effectivemobile.db.data.entity.Flower

class FlowerShopRepository(
    private val database: AppDatabase,
    private val flowerDao: FlowerDao,
    private val bouquetDao: BouquetDao,
    private val bouquetFlowerDao: BouquetFlowerDao
) {

    fun getAllFlowers(): Flow<List<Flower>> = flowerDao.getAllFlow()


    suspend fun getBouquetsWithAvailability(): List<BouquetWithAvailability> {
        return bouquetDao.getAllWithAvailability()
    }


    suspend fun getAvailableQuantity(bouquetId: Long): Int {
        return bouquetDao.getAvailableQuantity(bouquetId) ?: 0
    }


    suspend fun purchaseBouquet(bouquetId: Long, count: Int = 1): Boolean {
        return try {
            database.withTransaction {

                val composition = bouquetFlowerDao.getComposition(bouquetId)
                if (composition.isEmpty()) return@withTransaction false


                for (item in composition) {
                    val flower = flowerDao.getById(item.flower_id)
                        ?: throw IllegalStateException("Цветок не найден")
                    val required = item.quantity * count
                    if (flower.stock < required) {
                        throw IllegalStateException("Недостаточно ${flower.name}")
                    }
                }


                for (item in composition) {
                    val updated = flowerDao.decreaseStockIfEnough(item.flower_id, item.quantity * count)
                    if (updated == 0) throw IllegalStateException("Ошибка списания")
                }
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}