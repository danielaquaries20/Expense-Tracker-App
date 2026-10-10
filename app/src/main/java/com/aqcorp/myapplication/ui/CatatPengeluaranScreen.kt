package com.aqcorp.myapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aqcorp.myapplication.viewmodel.PengeluaranViewModel

@Composable
fun CatatPengeluaranScreen(
    modifier: Modifier = Modifier,
    viewModel: PengeluaranViewModel = viewModel()
) {
    val daftarPengeluaran by viewModel.daftarPengeluaran.collectAsState()

    var inputJudul by remember { mutableStateOf("") }
    var inputNominal by remember { mutableStateOf("") }
    var inputKategori by remember { mutableStateOf("Makanan") }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Catat Pengeluaran Baru", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = inputJudul,
            onValueChange = { inputJudul = it },
            label = { Text("Nama Pengeluaran (e.g. Nasi Goreng)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = inputNominal,
            onValueChange = { inputNominal = it },
            label = { Text("Nominal (Rp)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                viewModel.tambahPengeluaran(inputJudul, inputNominal, inputKategori)
                inputJudul = ""
                inputNominal = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan ke Room Database")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Riwayat Pengeluaran", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        // LazyColumn untuk menampilkan data Room secara efisien
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(daftarPengeluaran, key = { it.id }) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(item.judul, fontWeight = FontWeight.Bold)
                            Text(item.kategori, style = MaterialTheme.typography.bodySmall)
                        }
                        Text("Rp ${item.nominal}", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}