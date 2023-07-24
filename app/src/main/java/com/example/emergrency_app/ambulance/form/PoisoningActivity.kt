package com.example.emergrency_app.ambulance.form

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.example.emergrency_app.R

class PoisoningActivity : AppCompatActivity() {

    private lateinit var poisoningThingEditText: EditText
    private lateinit var poisoningWhenEditText: EditText
    private lateinit var situationEditText: EditText
    private lateinit var feelingEditText: EditText
    private lateinit var additionalInfoEditText: EditText
    private lateinit var sendInfoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambulance_poisoning)

        poisoningThingEditText = findViewById(R.id.poisoningThingEditText)
        poisoningWhenEditText = findViewById(R.id.poisoningWhenEditText)
        situationEditText = findViewById(R.id.situationEditText)
        feelingEditText = findViewById(R.id.feelingEditText)
        additionalInfoEditText = findViewById(R.id.additionalInfoEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        sendInfoButton.setOnClickListener {
            val poisoningThing = poisoningThingEditText.text.toString()
            val poisoningWhen = poisoningWhenEditText.text.toString()
            val feeling = feelingEditText.text.toString()
            val additionalInfo = additionalInfoEditText.text.toString()
            val situation = situationEditText.text.toString()

            // TODO: Obraditi unesene informacije, spremiti u bazu podataka na firebase

            val logMessage = "Stvar: $poisoningThing\nKada: $poisoningWhen\nKako se oseca: $feeling\nInformacije: $additionalInfo\nDa li je svesna: $situation"
            Log.d("Informacije", logMessage)
        }
    }
}