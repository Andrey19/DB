package ru.effectivemobile.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo


@Entity(tableName = "flowers")
data class Flower(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    var stock: Int,
    @ColumnInfo(name = "country_of_origin")
    val countryOfOrigin: String? = null
)