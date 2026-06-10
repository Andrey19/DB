package ru.effectivemobile.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Embedded
import kotlinx.coroutines.flow.Flow
import ru.effectivemobile.db.data.entity.Bouquet


data class BouquetWithAvailability(
    @Embedded val bouquet: Bouquet,
    val availableQuantity: Int
)

@Dao
interface BouquetDao {

    @Insert
    suspend fun insertAll(bouquets: List<Bouquet>): List<Long>

    @Query("SELECT * FROM bouquets")
    fun getAllFlow(): Flow<List<Bouquet>>

    @Query("SELECT * FROM bouquets WHERE id = :id")
    suspend fun getById(id: Long): Bouquet?


    @Query("""
        SELECT MIN(f.stock / bf.quantity) 
        FROM bouquet_flowers bf
        JOIN flowers f ON bf.flower_id = f.id
        WHERE bf.bouquet_id = :bouquetId
    """)
    suspend fun getAvailableQuantity(bouquetId: Long): Int?


    @Query("""
        SELECT 
            b.id, b.name, b.price, b.description,
            MIN(f.stock / bf.quantity) AS availableQuantity
        FROM bouquets b
        JOIN bouquet_flowers bf ON b.id = bf.bouquet_id
        JOIN flowers f ON bf.flower_id = f.id
        GROUP BY b.id
        ORDER BY b.name
    """)
    suspend fun getAllWithAvailability(): List<BouquetWithAvailability>
}