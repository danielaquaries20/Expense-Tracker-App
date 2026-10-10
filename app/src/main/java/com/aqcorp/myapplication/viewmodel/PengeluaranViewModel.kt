package com.aqcorp.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aqcorp.myapplication.database.AppDatabase
import com.aqcorp.myapplication.database.entity.PengeluaranEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PengeluaranViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).pengeluaranDao()

    // Memancar daftar transaksi dari Room Database ke UI
    val daftarPengeluaran: StateFlow<List<PengeluaranEntity>> = dao.getAllPengeluaran()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun tambahPengeluaran(judul: String, nominalStr: String, kategori: String) {
        val nominal = nominalStr.toIntOrNull() ?: 0
        if (judul.isBlank() || nominal <= 0) return

        viewModelScope.launch {
            val transaksiBaru = PengeluaranEntity(
                judul = judul,
                nominal = nominal,
                kategori = kategori
            )
            dao.insertPengeluaran(transaksiBaru)
        }
    }
}