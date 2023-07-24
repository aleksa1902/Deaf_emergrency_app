package com.example.emergrency_app.ambulance.form

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.example.emergrency_app.R

class InjuriesActivity : AppCompatActivity() {

    private lateinit var injuryEditText: EditText
    private lateinit var injuryTypeEditText: EditText
    private lateinit var injurySituationEditText: EditText
    private lateinit var bleedingEditText: EditText
    private lateinit var injuryAbleEditText: EditText
    private lateinit var sendInfoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambulance_injuries)

        injuryEditText = findViewById(R.id.injuryEditText)
        injuryTypeEditText = findViewById(R.id.injuryTypeEditText)
        injurySituationEditText = findViewById(R.id.injurySituationEditText)
        bleedingEditText = findViewById(R.id.bleedingEditText)
        injuryAbleEditText = findViewById(R.id.injuryAbleEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        sendInfoButton.setOnClickListener {
            val injury = injuryEditText.text.toString()
            val injuryType = injuryTypeEditText.text.toString()
            val injurySituation = injurySituationEditText.text.toString()
            val bleedingEditText = bleedingEditText.text.toString()
            val injuryAble = injuryAbleEditText.text.toString()

            // TODO: Obraditi unesene informacije, spremiti u bazu podataka na firebase

            val logMessage = "Koja vrsta povrede: $injury\nTeska povreda: $injuryType\nDa li je izgubila svest: $injurySituation\nKrvarenje: $bleedingEditText\nSposobna kretati se: $injuryAble"
            Log.d("Informacije", logMessage)
        }
    }
}