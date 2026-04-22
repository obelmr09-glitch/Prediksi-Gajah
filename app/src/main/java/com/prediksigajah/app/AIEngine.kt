package com.prediksigajah.app

import android.content.Context
import android.content.SharedPreferences

class AIEngine(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("ai_model", Context.MODE_PRIVATE)

    fun train(pasaran: String, history: List<String>) {
        val frekuensi = IntArray(10)
        history.forEach { angka ->
            angka.forEach { ch ->
                if (ch.isDigit()) frekuensi[ch.digitToInt()]++
            }
        }
        val editor = prefs.edit()
        for (i in 0..9) {
            val key = "${pasaran}_digit_$i"
            val bobotLama = prefs.getFloat(key, 1.0f)
            val bobotBaru = (bobotLama * 0.7f) + (frekuensi[i] * 0.3f)
            editor.putFloat(key, bobotBaru)
        }
        editor.putLong("${pasaran}_last_train", System.currentTimeMillis())
        editor.apply()
    }

    fun prediksi(pasaran: String, angkaTerakhir: String): List<Int> {
        val bobot = FloatArray(10) { prefs.getFloat("${pasaran}_digit_$it", 1.0f) }
        val kandidatMetode = MetodePrediksi.prediksiKombinasi(angkaTerakhir)
        kandidatMetode.forEach { bobot[it] += 2.0f }

        return bobot.mapIndexed { idx, b -> idx to b }
            .sortedByDescending { it.second }
            .take(6)
            .map { it.first }
            .sorted()
    }

    fun getLastTrainTime(pasaran: String): Long {
        return prefs.getLong("${pasaran}_last_train", 0L)
    }
}
