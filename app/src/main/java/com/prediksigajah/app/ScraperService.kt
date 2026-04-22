package com.prediksigajah.app

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

data class KeluaranData(
    val pasaran: String,
    val tanggal: String,
    val angka: String
)

object ScraperService {
    private const val URL = "https://hongkong-pools-1001.dishubkaranganyar.com/"

    suspend fun ambilKeluaran(): List<KeluaranData> = withContext(Dispatchers.IO) {
        val hasil = mutableListOf<KeluaranData>()
        try {
            val doc = Jsoup.connect(URL)
                .userAgent("Mozilla/5.0 (Linux; Android 10)")
                .timeout(15000)
                .get()

            val rows = doc.select("table tr")
            for (row in rows) {
                val cols = row.select("td")
                if (cols.size >= 3) {
                    val pasaran = cols[0].text()
                    val tanggal = cols[1].text()
                    val angka = cols[2].text()
                    if (pasaran.isNotEmpty() && angka.isNotEmpty()) {
                        hasil.add(KeluaranData(pasaran, tanggal, angka))
                    }
                }
            }

            val urutan = listOf("SYDNEY", "SINGAPURA", "HONGKONG")
            hasil.sortedBy { k ->
                val idx = urutan.indexOfFirst { k.pasaran.uppercase().contains(it) }
                if (idx >= 0) idx else 99
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
