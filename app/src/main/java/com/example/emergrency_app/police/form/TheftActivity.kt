package com.example.emergrency_app.police.form

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.example.emergrency_app.R

class TheftActivity : AppCompatActivity() {

    private lateinit var timeEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var injuryEditText: EditText
    private lateinit var susSituationEditText: EditText
    private lateinit var suspectEditText: EditText
    private lateinit var sendInfoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_police_theft)

        timeEditText = findViewById(R.id.timeEditText)
        locationEditText = findViewById(R.id.locationEditText)
        injuryEditText = findViewById(R.id.injuryEditText)
        susSituationEditText = findViewById(R.id.susSituationEditText)
        suspectEditText = findViewById(R.id.suspectEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        sendInfoButton.setOnClickListener {
            val time = timeEditText.text.toString()
            val location = locationEditText.text.toString()
            val injury = injuryEditText.text.toString()
            val susSituation = susSituationEditText.text.toString()
            val suspect = suspectEditText.text.toString()

            // TODO: Obraditi unesene informacije, spremiti u bazu podataka na firebase

            val logMessage = "Vreme: $time\nLokacija: $location\nImovina: $injury\nSumnjive aktivnosti: $susSituation\nOpis počinitelja: $suspect"
            Log.d("Informacije", logMessage)
        }
    }
}