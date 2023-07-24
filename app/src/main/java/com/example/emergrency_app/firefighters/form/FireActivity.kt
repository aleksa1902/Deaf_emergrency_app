package com.example.emergrency_app.firefighters.form

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.example.emergrency_app.R

class FireActivity : AppCompatActivity() {

    private lateinit var fireLocationEditText: EditText
    private lateinit var fireWhatEditText: EditText
    private lateinit var stuckEditText: EditText
    private lateinit var dangerEditText: EditText
    private lateinit var additionalInfoEditText: EditText
    private lateinit var sendInfoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firefighters_fire)

        fireLocationEditText = findViewById(R.id.fireLocationEditText)
        fireWhatEditText = findViewById(R.id.fireWhatEditText)
        stuckEditText = findViewById(R.id.stuckEditText)
        dangerEditText = findViewById(R.id.dangerEditText)
        additionalInfoEditText = findViewById(R.id.additionalInfoEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        sendInfoButton.setOnClickListener {
            val fireLocation = fireLocationEditText.text.toString()
            val fireWhat = fireWhatEditText.text.toString()
            val stuck = stuckEditText.text.toString()
            val danger = dangerEditText.text.toString()
            val additionalInfo = additionalInfoEditText.text.toString()

            // TODO: Obraditi unesene informacije, spremiti u bazu podataka na firebase

            val logMessage = "Lokacija: $fireLocation\nSta gori: $fireWhat\nNeko zaglavljen: $stuck\nSirenje opasnost: $danger\nInfo opasni materijali: $additionalInfo"
            Log.d("Informacije", logMessage)
        }
    }
}