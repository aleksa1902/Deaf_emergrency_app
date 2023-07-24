package com.example.emergrency_app.firefighters.form

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.example.emergrency_app.R

class GasActivity : AppCompatActivity() {

    private lateinit var odorEditText: EditText
    private lateinit var gasLocationEditText: EditText
    private lateinit var stuckEditText: EditText
    private lateinit var dangerEditText: EditText
    private lateinit var additionalInfoEditText: EditText
    private lateinit var sendInfoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firefighters_gas)

        odorEditText = findViewById(R.id.odorEditText)
        gasLocationEditText = findViewById(R.id.gasLocationEditText)
        stuckEditText = findViewById(R.id.stuckEditText)
        dangerEditText = findViewById(R.id.dangerEditText)
        additionalInfoEditText = findViewById(R.id.additionalInfoEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        sendInfoButton.setOnClickListener {
            val odor = odorEditText.text.toString()
            val gasLocation = gasLocationEditText.text.toString()
            val stuck = stuckEditText.text.toString()
            val danger = dangerEditText.text.toString()
            val additionalInfo = additionalInfoEditText.text.toString()

            // TODO: Obraditi unesene informacije, spremiti u bazu podataka na firebase

            val logMessage = "Miris: $odor\nLokacija plina: $gasLocation\nNeko zaglavljen: $stuck\nSirenje opasnost: $danger\nInfo opasni materijali: $additionalInfo"
            Log.d("Informacije", logMessage)
        }
    }
}