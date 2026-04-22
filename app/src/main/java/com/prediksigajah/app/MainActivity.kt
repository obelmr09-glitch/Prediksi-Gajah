package com.prediksigajah.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnKeluaran).setOnClickListener {
            startActivity(Intent(this, KeluaranActivity::class.java))
        }
        findViewById<Button>(R.id.btnSydney).setOnClickListener {
            openPrediksi("SYDNEY")
        }
        findViewById<Button>(R.id.btnSingapura).setOnClickListener {
            openPrediksi("SINGAPURA")
        }
        findViewById<Button>(R.id.btnHongkong).setOnClickListener {
            openPrediksi("HONGKONG")
        }

        scheduleAITraining()
    }

    private fun openPrediksi(pasaran: String) {
        val intent = Intent(this, PrediksiActivity::class.java)
        intent.putExtra("PASARAN", pasaran)
        startActivity(intent)
    }

    private fun scheduleAITraining() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<AITrainingWorker>(6, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("ai_training", ExistingPeriodicWorkPolicy.KEEP, request)
    }
}
