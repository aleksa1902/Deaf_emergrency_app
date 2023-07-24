package com.example.emergrency_app.ambulance.form

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.example.emergrency_app.R

class MedicalEmergencyActivity : AppCompatActivity() {

    private lateinit var symptomsEditText: EditText
    private lateinit var consciousEditText: EditText
    private lateinit var breathingEditText: EditText
    private lateinit var painEditText: EditText
    private lateinit var additionalInfoEditText: EditText
    private lateinit var sendInfoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambulance_emergency)

        symptomsEditText = findViewById(R.id.symptomsEditText)
        consciousEditText = findViewById(R.id.consciousEditText)
        breathingEditText = findViewById(R.id.breathingEditText)
        painEditText = findViewById(R.id.painEditText)
        additionalInfoEditText = findViewById(R.id.additionalInfoEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        sendInfoButton.setOnClickListener {
            val conscious = consciousEditText.text.toString()
            val breathing = breathingEditText.text.toString()
            val pain = painEditText.text.toString()
            val additionalInfo = additionalInfoEditText.text.toString()
            val symptoms = symptomsEditText.text.toString()

            // TODO: Obraditi unesene informacije, spremiti u bazu podataka na firebase

            val logMessage = "Da li je pri svesti: $conscious\nDa li osoba dise: $breathing\nDa li oseca bol: $pain\nInformacije: $additionalInfo\nSimptomi: $symptoms"
            Log.d("Informacije", logMessage)
        }
    }
}