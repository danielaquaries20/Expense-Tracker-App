package com.aqcorp.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aqcorp.myapplication.database.AppDatabase
import com.aqcorp.myapplication.database.entity.PengeluaranEntity
import com.aqcorp.myapplication.datastore.PengelolaAnggaran
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Model Data Ringkasan Dashboard
data class RingkasanKeuangan(
    val batasAnggaran: Int = 0,
    val totalPengeluaran: Int = 0,
    val sisaSaldo: Int = 0,
    val isOverBudget: Boolean = false
)

class ExpenseTrackerViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).pengeluaranDao()
    private val pengelolaAnggaran = PengelolaAnggaran(application)

    val daftarPengeluaran = dao.getAllPengeluaran().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // MENGGABUNGKAN (COMBINE) DATASTORE & ROOM DATABASE
    val ringkasanKeuangan: StateFlow<RingkasanKeuangan> = combine(
        pengelolaAnggaran.batasAnggaranFlow,
        dao.getTotalPengeluaran().map { it ?: 0 }
    ) { batas, total ->
        val sisa = batas - total
        RingkasanKeuangan(
            batasAnggaran = batas,
            totalPengeluaran = total,
            sisaSaldo = sisa,
            isOverBudget = batas in 1..<total
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RingkasanKeuangan()
    )

    fun tambahPengeluaran(judul: String, nominalStr: String, kategori: String) {
        val nominal = nominalStr.toIntOrNull() ?: 0
        if (judul.isBlank() || nominal <= 0) return

        viewModelScope.launch {
            dao.insertPengeluaran(
                PengeluaranEntity(
                    judul = judul,
                    nominal = nominal,
                    kategori = kategori
                )
            )
        }
    }

    // Fungsi Hapus Item dari Room
    fun hapusPengeluaran(item: PengeluaranEntity) {
        viewModelScope.launch {
            dao.deletePengeluaran(item)
        }
    }
}