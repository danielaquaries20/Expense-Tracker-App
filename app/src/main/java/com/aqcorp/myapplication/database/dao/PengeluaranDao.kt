package com.aqcorp.myapplication.database.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.aqcorp.myapplication.database.entity.PengeluaranEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PengeluaranDao {

    // Menambah data transaksi baru
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPengeluaran(pengeluaran: PengeluaranEntity)

    // Membaca seluruh data pengeluaran secara real-time (urut dari yang terbaru)
    @Query("SELECT * FROM pengeluaran ORDER BY id DESC")
    fun getAllPengeluaran(): Flow<List<PengeluaranEntity>>

    // TAMBAHAN PERTEMUAN 10: Operasi Hapus
    @Delete
    suspend fun deletePengeluaran(pengeluaran: PengeluaranEntity)

    // TAMBAHAN PERTEMUAN 10: Query Agregat untuk Menghitung Total Nominal
    // Mengembalikan Int? karena nilai SUM bisa null jika tabel masih kosong
    @Query("SELECT SUM(nominal) FROM pengeluaran")
    fun getTotalPengeluaran(): Flow<Int?>
}