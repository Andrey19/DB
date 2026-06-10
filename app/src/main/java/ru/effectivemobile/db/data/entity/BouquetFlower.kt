package ru.effectivemobile.db.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "bouquet_flowers",
    primaryKeys = ["bouquet_id", "flower_id"],
    foreignKeys = [
        ForeignKey(
            entity = Bouquet::class,
            parentColumns = ["id"],
            childColumns = ["bouquet_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Flower::class,
            parentColumns = ["id"],
            childColumns = ["flower_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("flower_id")]
)
data class BouquetFlower(
    val bouquet_id: Long,
    val flower_id: Long,
    val quantity: Int
)