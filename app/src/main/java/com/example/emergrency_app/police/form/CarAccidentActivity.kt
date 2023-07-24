package com.example.emergrency_app.police.form

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.example.emergrency_app.R

class CarAccidentActivity : AppCompatActivity() {

    private lateinit var locationEditText: EditText
    private lateinit var carNumEditText: EditText
    private lateinit var injuryEditText: EditText
    private lateinit var infoDriversEditText: EditText
    private lateinit var suspectEditText: EditText
    private lateinit var sendInfoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_police_car_accident)

        locationEditText = findViewById(R.id.locationEditText)
        carNumEditText = findViewById(R.id.carNumEditText)
        injuryEditText = findViewById(R.id.injuryEditText)
        infoDriversEditText = findViewById(R.id.infoDriversEditText)
        suspectEditText = findViewById(R.id.suspectEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        sendInfoButton.setOnClickListener {
            val location = locationEditText.text.toString()
            val carNum = carNumEditText.text.toString()
            val injury = injuryEditText.text.toString()
            val infoDrivers = infoDriversEditText.text.toString()
            val suspect = suspectEditText.text.toString()

            // TODO: Obraditi unesene informacije, spremiti u bazu podataka na firebase

            val logMessage = "Lokacija: $location\nBroj automobila: $carNum\nPovrede: $injury\nInformacije: $infoDrivers\nSumnjive aktivnosti: $suspect"
            Log.d("Informacije", logMessage)
        }
    }
}