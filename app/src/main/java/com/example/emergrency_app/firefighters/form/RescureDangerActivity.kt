package com.example.emergrency_app.firefighters.form

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.example.emergrency_app.R

class RescureDangerActivity : AppCompatActivity() {

    private lateinit var dangerNatureEditText: EditText
    private lateinit var numEditText: EditText
    private lateinit var currLocationEditText: EditText
    private lateinit var dangerEditText: EditText
    private lateinit var additionalInfoEditText: EditText
    private lateinit var sendInfoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firefighters_rescure_danger)

        dangerNatureEditText = findViewById(R.id.dangerNatureEditText)
        numEditText = findViewById(R.id.numEditText)
        currLocationEditText = findViewById(R.id.currLocationEditText)
        dangerEditText = findViewById(R.id.dangerEditText)
        additionalInfoEditText = findViewById(R.id.additionalInfoEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        sendInfoButton.setOnClickListener {
            val dangerNature = dangerNatureEditText.text.toString()
            val num = numEditText.text.toString()
            val currLocation = currLocationEditText.text.toString()
            val danger = dangerEditText.text.toString()
            val additionalInfo = additionalInfoEditText.text.toString()

            // TODO: Obraditi unesene informacije, spremiti u bazu podataka na firebase

            val logMessage = "Priroda opasnosti: $dangerNature\nBroj zivota: $num\nTacno mesto: $currLocation\nSirenje opasnost: $danger\nInfo opasni materijali: $additionalInfo"
            Log.d("Informacije", logMessage)
        }
    }
}