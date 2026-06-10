package ru.effectivemobile.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "bouquets")
data class Bouquet(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val price: Double,
    val description: String? = null,
    @ColumnInfo(name = "packaging_type")
    val packagingType: String? = null
)