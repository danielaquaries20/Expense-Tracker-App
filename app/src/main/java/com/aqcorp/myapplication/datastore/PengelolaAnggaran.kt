package com.aqcorp.myapplication.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 1. Membuat wujud brankas (file fisik di dalam memori HP) bernama "pengaturan_user"
val Context.dataStore by preferencesDataStore(name = "pengaturan_user")

class PengelolaAnggaran(private val context: Context) {

    // 2. Membuat "Label Laci" agar data kita tidak tertukar
    companion object {
        val BATAS_ANGGARAN = intPreferencesKey("batas_anggaran")
    }

    // 3. Fungsi untuk MEMBACA data secara terus-menerus (Flow)
    val batasAnggaranFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        // Jika laci kosong (user belum pernah seting anggaran), beri nilai default 0
        preferences[BATAS_ANGGARAN] ?: 100000
    }

    // 4. Fungsi untuk MENYIMPAN data baru
    // Pakai 'suspend' karena proses menyimpan ke memori fisik butuh waktu (Coroutines)
    suspend fun simpanAnggaran(nominal: Int) {
        context.dataStore.edit { preferences ->
            preferences[BATAS_ANGGARAN] = nominal
        }
    }
}