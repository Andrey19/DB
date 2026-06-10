package ru.effectivemobile.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.effectivemobile.db.data.entity.BouquetFlower

@Dao
interface BouquetFlowerDao {

    @Insert
    suspend fun insert(bouquetFlower: BouquetFlower)

    @Query("SELECT * FROM bouquet_flowers WHERE bouquet_id = :bouquetId")
    suspend fun getComposition(bouquetId: Long): List<BouquetFlower>
}