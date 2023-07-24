package com.example.emergrency_app.ambulance.form

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.example.emergrency_app.R

class ChildbirthActivity : AppCompatActivity() {

    private lateinit var laboursEditText: EditText
    private lateinit var durationLaboursEditText: EditText
    private lateinit var complicationsEditText: EditText
    private lateinit var waterBreakingEditText: EditText
    private lateinit var additionalInfoEditText: EditText
    private lateinit var sendInfoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambulance_childbirth)

        laboursEditText = findViewById(R.id.laboursEditText)
        durationLaboursEditText = findViewById(R.id.durationLaboursEditText)
        complicationsEditText = findViewById(R.id.complicationsEditText)
        waterBreakingEditText = findViewById(R.id.waterBreakingEditText)
        additionalInfoEditText = findViewById(R.id.additionalInfoEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        sendInfoButton.setOnClickListener {
            val duration = durationLaboursEditText.text.toString()
            val complications = complicationsEditText.text.toString()
            val waterBreaking = waterBreakingEditText.text.toString()
            val additionalInfo = additionalInfoEditText.text.toString()
            val labours = laboursEditText.text.toString()

            // TODO: Obraditi unesene informacije, spremiti u bazu podataka na firebase

            val logMessage = "Koliko dugo: $duration\nKomplikacije: $complications\nVodenjak: $waterBreaking\nPrethodni problemi: $additionalInfo\nTrudovi: $labours"
            Log.d("Informacije", logMessage)
        }
    }
}