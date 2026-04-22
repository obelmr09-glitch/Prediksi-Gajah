package com.prediksigajah.app

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class PrediksiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prediksi)

        val pasaran = intent.getStringExtra("PASARAN") ?: "SYDNEY"
        val tvJudul = findViewById<TextView>(R.id.tvJudul)
        val tvHasil = findViewById<TextView>(R.id.tvHasil)
        tvJudul.text = "Prediksi $pasaran"

        val ai = AIEngine(this)

        lifecycleScope.launch {
            val data = ScraperService.ambilKeluaran()
                .filter { it.pasaran.uppercase().contains(pasaran) }

            if (data.isEmpty()) {
                tvHasil.text = "Data keluaran $pasaran belum tersedia.\nCek koneksi internet."
                return@launch
            }

            ai.train(pasaran, data.map { it.angka })

            val angkaTerakhir = data.first().angka
            val prediksi = ai.prediksi(pasaran, angkaTerakhir)

            val ml = MetodePrediksi.hitungMistikLama(angkaTerakhir)
            val mb = MetodePrediksi.hitungMistikBaru(angkaTerakhir)
            val idx = MetodePrediksi.hitungIndeks(angkaTerakhir)

            tvHasil.text = """
                📌 Angka Terakhir: $angkaTerakhir
                
                🔮 Mistik Lama: $ml
                ✨ Mistik Baru: $mb
                🎯 Indeks: $idx
                
                🤖 AI + Kombinasi:
                ${prediksi.joinToString(" - ")}
                
                ⏱ Terakhir dilatih:
                ${java.util.Date(ai.getLastTrainTime(pasaran))}
            """.trimIndent()
        }
    }
}
