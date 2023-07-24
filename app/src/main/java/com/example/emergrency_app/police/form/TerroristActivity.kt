package com.example.emergrency_app.police.form

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.example.emergrency_app.R

class TerroristActivity : AppCompatActivity() {

    private lateinit var threatsEditText: EditText
    private lateinit var additionalInfoEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var youEditText: EditText
    private lateinit var suspectEditText: EditText
    private lateinit var sendInfoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_police_terrorist)

        locationEditText = findViewById(R.id.locationEditText)
        threatsEditText = findViewById(R.id.threatsEditText)
        additionalInfoEditText = findViewById(R.id.additionalInfoEditText)
        youEditText = findViewById(R.id.youEditText)
        suspectEditText = findViewById(R.id.suspectEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        sendInfoButton.setOnClickListener {
            val location = locationEditText.text.toString()
            val threats = threatsEditText.text.toString()
            val additionalInfo = additionalInfoEditText.text.toString()
            val youEditText = youEditText.text.toString()
            val suspect = suspectEditText.text.toString()

            // TODO: Obraditi unesene informacije, spremiti u bazu podataka na firebase

            val logMessage = "Lokacija: $location\nPretnje: $threats\nDodatni opis: $additionalInfo\nSvedoci: $youEditText\nNeobicne aktivnosti: $suspect"
            Log.d("Informacije", logMessage)
        }
    }
}