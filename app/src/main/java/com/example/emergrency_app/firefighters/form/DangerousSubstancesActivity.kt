package com.example.emergrency_app.firefighters.form

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.emergrency_app.R

class DangerousSubstancesActivity : AppCompatActivity() {

    private lateinit var radioGroupDangerousSubstancesType: RadioGroup
    private lateinit var layoutSpilling: LinearLayout
    private lateinit var layoutRemoving: LinearLayout
    private lateinit var buttonSendInformation: Button

    private lateinit var thingPersonEditText: EditText
    private lateinit var locationThingEditText: EditText
    private lateinit var dangerEditText: EditText
    private lateinit var numEditText: EditText
    private lateinit var additionalInfoEditText: EditText

    private lateinit var hazardousEditText: EditText
    private lateinit var hazardousLocationEditText: EditText
    private lateinit var dangerSpreadEditText: EditText
    private lateinit var safeEditText: EditText
    private lateinit var numPersonEditText: EditText

    private var spilingThing: Boolean = false
    private var check: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firefighters_dangerous_substances)

        radioGroupDangerousSubstancesType = findViewById(R.id.radioGroupDangerousSubstancesType)
        layoutSpilling = findViewById(R.id.layoutSpilling)
        layoutRemoving = findViewById(R.id.layoutRemoving)
        buttonSendInformation = findViewById(R.id.buttonSendInformation)

        radioGroupDangerousSubstancesType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonSpilling -> {
                    layoutSpilling.visibility = View.VISIBLE
                    layoutRemoving.visibility = View.GONE

                    thingPersonEditText = findViewById(R.id.thingPersonEditText)
                    locationThingEditText = findViewById(R.id.locationThingEditText)
                    dangerEditText = findViewById(R.id.dangerEditText)
                    numEditText = findViewById(R.id.numEditText)
                    additionalInfoEditText = findViewById(R.id.additionalInfoEditText)

                    spilingThing = true
                    check = true
                }
                R.id.radioButtonRemoving -> {
                    layoutSpilling.visibility = View.GONE
                    layoutRemoving.visibility = View.VISIBLE

                    hazardousEditText = findViewById(R.id.hazardousEditText)
                    hazardousLocationEditText = findViewById(R.id.hazardousLocationEditText)
                    dangerSpreadEditText = findViewById(R.id.dangerSpreadEditText)
                    safeEditText = findViewById(R.id.safeEditText)
                    numPersonEditText = findViewById(R.id.numPersonEditText)

                    spilingThing = false
                    check = true
                }
            }
        }

        // Ovdje dodajte kod za obradu informacija kad korisnik pritisne "Posalji informacije"
        buttonSendInformation.setOnClickListener {
            if(check && spilingThing){
                val thingPerson = thingPersonEditText.text.toString()
                val locationThing = locationThingEditText.text.toString()
                val danger = dangerEditText.text.toString()
                val num = numEditText.text.toString()
                val additionalInfo = additionalInfoEditText.text.toString()

                val logMessage = "Koja stvar: $thingPerson\nLokacija: $locationThing\nOpasnost: $danger\nBroj osoba: $num\nDodatno: $additionalInfo"
                Log.d("Informacije", logMessage)
            }else{
                val hazardous = hazardousEditText.text.toString()
                val hazardousLocation = hazardousLocationEditText.text.toString()
                val dangerSpread = dangerSpreadEditText.text.toString()
                val safe = safeEditText.text.toString()
                val numPerson = numPersonEditText.text.toString()

                val logMessage = "Koja vrsta: $hazardous\nLokacija: $hazardousLocation\nSirenje: $dangerSpread\nBezbednost: $safe\nBroj osoba: $numPerson"
                Log.d("Informacije", logMessage)
            }

            // TODO: Obraditi unesene informacije, spremiti u bazu podataka na firebase
        }
    }
}