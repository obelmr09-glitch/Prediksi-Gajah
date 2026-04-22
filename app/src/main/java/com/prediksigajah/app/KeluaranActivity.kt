package com.prediksigajah.app

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class KeluaranActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keluaran)

        val table = findViewById<TableLayout>(R.id.tableKeluaran)

        val header = TableRow(this)
        header.addView(cell("PASARAN", true))
        header.addView(cell("TANGGAL", true))
        header.addView(cell("ANGKA", true))
        table.addView(header)

        lifecycleScope.launch {
            val data = ScraperService.ambilKeluaran()
            if (data.isEmpty()) {
                Toast.makeText(this@KeluaranActivity,
                    "Gagal mengambil data. Cek koneksi.", Toast.LENGTH_LONG).show()
            }
            data.forEach { k ->
                val row = TableRow(this@KeluaranActivity)
                row.addView(cell(k.pasaran, false))
                row.addView(cell(k.tanggal, false))
                row.addView(cell(k.angka, false))
                table.addView(row)
            }
        }
    }

    private fun cell(text: String, isHeader: Boolean): TextView {
        return TextView(this).apply {
            this.text = text
            setPadding(16, 16, 16, 16)
            gravity = Gravity.CENTER
            setTextColor(if (isHeader) Color.YELLOW else Color.WHITE)
            textSize = if (isHeader) 16f else 14f
        }
    }
}
