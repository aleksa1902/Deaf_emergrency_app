package com.example.emergrency_app.police.form

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.emergrency_app.MainActivity
import com.example.emergrency_app.R
import com.example.emergrency_app.police.data.FamilyViolenceData
import com.example.emergrency_app.police.data.PublicViolenceData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

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

    private lateinit var db: FirebaseFirestore

    private var familyViolence: Boolean = false
    private var check: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_police_violence)

        radioGroupViolenceType = findViewById(R.id.radioGroupViolenceType)
        layoutFamilyViolence = findViewById(R.id.layoutFamilyViolence)
        layoutPublicViolence = findViewById(R.id.layoutPublicViolence)
        buttonSendInformation = findViewById(R.id.buttonSendInformation)

        db = FirebaseFirestore.getInstance()

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

        buttonSendInformation.setOnClickListener {
            if(check && familyViolence){
                val familyViolencePerson = familyViolencePersonEditText.text.toString()
                val familyInjury = familyInjuryEditText.text.toString()
                val youEdit = youEditText.text.toString()
                val susSituation = susSituationEditText.text.toString()
                val suspect = suspectEditText.text.toString()
                val familyLocation = familyLocationEditText.text.toString()

                val liveLocation = Location("mock_provider")
                liveLocation.latitude = 37.7749
                liveLocation.longitude = -122.4194

                val familyViolenceData = FamilyViolenceData(
                    familyViolencePerson,
                    familyInjury,
                    youEdit,
                    susSituation,
                    suspect,
                    familyLocation,
                    GeoPoint(liveLocation.latitude, liveLocation.longitude)
                )
                db.collection("family_violence")
                    .add(familyViolenceData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Podaci su uspešno sačuvani u bazi.", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { error ->
                        Toast.makeText(this, "Došlo je do greške prilikom upisivanja podataka: ${error.message}", Toast.LENGTH_SHORT).show()
                    }

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }else{
                val publicLocation = publicLocationEditText.text.toString()
                val publicNumber = publicNumberEditText.text.toString()
                val publicYou = publicYouEditText.text.toString()
                val publicInfo = publicInfoEditText.text.toString()
                val publicAdditionalInfo = publicAdditionalInfoEditText.text.toString()

                val liveLocation = Location("mock_provider")
                liveLocation.latitude = 37.7749
                liveLocation.longitude = -122.4194

                val publicViolenceData = PublicViolenceData(
                    publicLocation,
                    publicNumber,
                    publicYou,
                    publicInfo,
                    publicAdditionalInfo,
                    GeoPoint(liveLocation.latitude, liveLocation.longitude)
                )
                db.collection("public_violence")
                    .add(publicViolenceData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Podaci su uspešno sačuvani u bazi.", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { error ->
                        Toast.makeText(this, "Došlo je do greške prilikom upisivanja podataka: ${error.message}", Toast.LENGTH_SHORT).show()
                    }

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}