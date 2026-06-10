package ru.effectivemobile.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.effectivemobile.db.data.entity.Flower

@Dao
interface FlowerDao {

    @Insert
    suspend fun insertAll(flowers: List<Flower>): List<Long>

    @Query("SELECT * FROM flowers")
    fun getAllFlow(): Flow<List<Flower>>   // Flow автоматически обновляется при изменениях

    @Query("SELECT * FROM flowers WHERE id = :flowerId")
    suspend fun getById(flowerId: Long): Flower?

    @Update
    suspend fun update(flower: Flower)

    @Query("UPDATE flowers SET stock = stock - :quantity WHERE id = :flowerId AND stock >= :quantity")
    suspend fun decreaseStockIfEnough(flowerId: Long, quantity: Int): Int
}