package com.aqcorp.myapplication.database.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "pengeluaran")
data class PengeluaranEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val judul: String,
    val nominal: Int,
    val kategori: String
)
