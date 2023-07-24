package com.example.emergrency_app.police.form

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.emergrency_app.R

class ViolenceActivity : AppCompatActivity() {

    private lateinit var radioGroupViolenceType: RadioGroup
    private lateinit var layoutFamilyViolence: LinearLayout
    private lateinit var layoutPublicViolence: LinearLayout
    private lateinit var buttonSendInformation: Button

    private lateinit var familyViolencePersonEditText: EditText
    private lateinit var familyInjuryEditText: EditText
    private lateinit var youEditText: EditText
    private lateinit var susSituationEditText: EditText
    private lateinit var suspectEditText: EditText
    private lateinit var familyLocationEditText: EditText

    private lateinit var publicLocationEditText: EditText
    private lateinit var publicNumberEditText: EditText
    private lateinit var publicYouEditText: EditText
    private lateinit var publicInfoEditText: EditText
    private lateinit var publicAdditionalInfoEditText: EditText

    private var familyViolence: Boolean = false
    private var check: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_police_violence)

        radioGroupViolenceType = findViewById(R.id.radioGroupViolenceType)
        layoutFamilyViolence = findViewById(R.id.layoutFamilyViolence)
        layoutPublicViolence = findViewById(R.id.layoutPublicViolence)
        buttonSendInformation = findViewById(R.id.buttonSendInformation)

        radioGroupViolenceType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonFamilyViolence -> {
                    layoutFamilyViolence.visibility = View.VISIBLE
                    layoutPublicViolence.visibility = View.GONE

                    familyViolencePersonEditText = findViewById(R.id.familyViolencePersonEditText)
                    familyInjuryEditText = findViewById(R.id.familyInjuryEditText)
                    youEditText = findViewById(R.id.youEditText)
                    susSituationEditText = findViewById(R.id.susSituationEditText)
                    suspectEditText = findViewById(R.id.suspectEditText)
                    familyLocationEditText = findViewById(R.id.familyLocationEditText)

                    familyViolence = true
                    check = true
                }
                R.id.radioButtonPublicViolence -> {
                    layoutFamilyViolence.visibility = View.GONE
                    layoutPublicViolence.visibility = View.VISIBLE

                    publicLocationEditText = findViewById(R.id.publicLocationEditText)
                    publicNumberEditText = findViewById(R.id.publicNumberEditText)
                    publicYouEditText = findViewById(R.id.publicYouEditText)
                    publicInfoEditText = findViewById(R.id.publicInfoEditText)
                    publicAdditionalInfoEditText = findViewById(R.id.publicAdditionalInfoEditText)

                    familyViolence = false
                    check = true
                }
            }
        }

        // Ovdje dodajte kod za obradu informacija kad korisnik pritisne "Posalji informacije"
        buttonSendInformation.setOnClickListener {
            if(check && familyViolence){
                val familyViolencePerson = familyViolencePersonEditText.text.toString()
                val familyInjury = familyInjuryEditText.text.toString()
                val youEdit = youEditText.text.toString()
                val susSituation = susSituationEditText.text.toString()
                val suspect = suspectEditText.text.toString()
                val familyLocation = familyLocationEditText.text.toString()

                val logMessage = "Ko: $familyViolencePerson\nLokacija: $familyLocation\nPovrede: $familyInjury\nSvedok: $youEdit\nOpis: $suspect\nsusSituation: $susSituation\n"
                Log.d("Informacije", logMessage)
            }else{
                val publicLocation = publicLocationEditText.text.toString()
                val publicNumber = publicNumberEditText.text.toString()
                val publicYou = publicYouEditText.text.toString()
                val publicInfo = publicInfoEditText.text.toString()
                val publicAdditionalInfo = publicAdditionalInfoEditText.text.toString()

                val logMessage = "Public location: $publicLocation\nKoliko ucesnika: $publicNumber\nSvedok: $publicYou\nSumnjive aktivnosti: $publicInfo\nJos info: $publicAdditionalInfo"
                Log.d("Informacije", logMessage)
            }

            // TODO: Obraditi unesene informacije, spremiti u bazu podataka na firebase
        }
    }
}