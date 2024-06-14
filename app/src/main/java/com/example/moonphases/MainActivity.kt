package com.example.moonphases

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val moonPhaseApi = ApiClient.apiClient
        val phaseMessage = findViewById<TextView>(R.id.phase)
        val lightMessage = findViewById<TextView>(R.id.light)

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())

        GlobalScope.launch {
            val moonData = moonPhaseApi.getAstroData("Almaty", currentDate, "d2615712db7e4a449d3113038241604").body()
            Log.d("check: ", moonData!!.location.name)
            Log.d("check: ", moonData!!.location.country)
            Log.d("check: ", moonData!!.astronomy.astro.moon_phase)
            Log.d("check: ", currentDate)

            phaseMessage.text = moonData!!.astronomy.astro.moon_phase
            val illuminationValue = moonData!!.astronomy.astro.moon_illumination.toString()
            lightMessage.text = "Moon Illumination $illuminationValue %"
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}