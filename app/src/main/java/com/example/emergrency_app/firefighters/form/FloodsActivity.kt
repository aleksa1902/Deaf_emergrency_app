package com.example.emergrency_app.firefighters.form

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.example.emergrency_app.R

class FloodsActivity : AppCompatActivity() {

    private lateinit var floodLocationEditText: EditText
    private lateinit var floodNumEditText: EditText
    private lateinit var stuckEditText: EditText
    private lateinit var dangerEditText: EditText
    private lateinit var additionalInfoEditText: EditText
    private lateinit var sendInfoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firefighters_floods)

        floodLocationEditText = findViewById(R.id.floodLocationEditText)
        floodNumEditText = findViewById(R.id.floodNumEditText)
        stuckEditText = findViewById(R.id.stuckEditText)
        dangerEditText = findViewById(R.id.dangerEditText)
        additionalInfoEditText = findViewById(R.id.additionalInfoEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        sendInfoButton.setOnClickListener {
            val floodLocation = floodLocationEditText.text.toString()
            val floodNum = floodNumEditText.text.toString()
            val stuck = stuckEditText.text.toString()
            val danger = dangerEditText.text.toString()
            val additionalInfo = additionalInfoEditText.text.toString()

            // TODO: Obraditi unesene informacije, spremiti u bazu podataka na firebase

            val logMessage = "Lokacija: $floodLocation\nKoliko osoba: $floodNum\nNeko zaglavljen: $stuck\nSirenje opasnost: $danger\nInfo opasni materijali: $additionalInfo"
            Log.d("Informacije", logMessage)
        }
    }
}