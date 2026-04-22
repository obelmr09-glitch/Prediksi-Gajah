package com.prediksigajah.app

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class AITrainingWorker(ctx: Context, params: WorkerParameters) :
    CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        return try {
            val data = ScraperService.ambilKeluaran()
            val ai = AIEngine(applicationContext)
            listOf("SYDNEY", "SINGAPURA", "HONGKONG").forEach { ps ->
                val filtered = data.filter { it.pasaran.uppercase().contains(ps) }
                if (filtered.isNotEmpty()) ai.train(ps, filtered.map { it.angka })
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
