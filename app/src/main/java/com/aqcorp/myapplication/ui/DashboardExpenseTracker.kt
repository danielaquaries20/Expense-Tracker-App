package com.aqcorp.myapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aqcorp.myapplication.viewmodel.ExpenseTrackerViewModel

@Composable
fun DashboardExpenseTracker(
    modifier: Modifier = Modifier,
    viewModel: ExpenseTrackerViewModel = viewModel()
) {
    val ringkasan by viewModel.ringkasanKeuangan.collectAsState()
    val daftarPengeluaran by viewModel.daftarPengeluaran.collectAsState()

    var inputJudul by remember { mutableStateOf("") }
    var inputNominal by remember { mutableStateOf("") }

    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp))
    {

        // KARTU INDIKATOR UTAMA (DataStore + Room)
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (ringkasan.isOverBudget) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (ringkasan.isOverBudget) "PERINGATAN: MENAMBUNG MELEBIHI LIMIT!" else "Sisa Anggaran Bulanan",
                    color = if (ringkasan.isOverBudget) Color.Red else Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Rp ${ringkasan.sisaSaldo}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (ringkasan.isOverBudget) Color.Red else Color(0xFF1B5E20)
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Batas: Rp ${ringkasan.batasAnggaran}", fontSize = 12.sp)
                    Text("Terpakai: Rp ${ringkasan.totalPengeluaran}", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Form Input Transaksi
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputJudul,
                onValueChange = { inputJudul = it },
                label = { Text("Transaksi") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = inputNominal,
                onValueChange = { inputNominal = it },
                label = { Text("Nominal") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.tambahPengeluaran(inputJudul, inputNominal, "Umum")
                inputJudul = ""
                inputNominal = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tambah Transaksi")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Daftar Riwayat dengan Fitur Hapus Item
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(daftarPengeluaran, key = { it.id }) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(item.judul, fontWeight = FontWeight.Bold)
                            Text("Rp ${item.nominal}", color = Color.Gray)
                        }
                        IconButton(onClick = { viewModel.hapusPengeluaran(item) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Hapus",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}