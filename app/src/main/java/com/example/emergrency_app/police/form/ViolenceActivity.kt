package com.example.emergrency_app.police.form

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.emergrency_app.MainActivity
import com.example.emergrency_app.R
import com.example.emergrency_app.helper.FirebaseHelper
import com.example.emergrency_app.helper.SmsHelper
import com.example.emergrency_app.police.data.FamilyViolenceData
import com.example.emergrency_app.police.data.PublicViolenceData
import com.example.emergrency_app.police.data.TheftData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class ViolenceActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 123

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
                    buttonSendInformation.visibility = View.VISIBLE

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
                    buttonSendInformation.visibility = View.VISIBLE

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
                val data = FamilyViolenceData(
                    familyViolencePersonEditText.text.toString(),
                    familyInjuryEditText.text.toString(),
                    youEditText.text.toString(),
                    susSituationEditText.text.toString(),
                    suspectEditText.text.toString(),
                    familyLocationEditText.text.toString(),
                    null
                )

                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    if (FirebaseHelper.isInternetConnected(this)) {
                        getCurrentLocationAndSendDataFamily(data, true)

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // No internet, send live location as SMS
                        getCurrentLocationAndSendDataFamily(data, false)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                }
            }else{
                val data = PublicViolenceData(
                    publicLocationEditText.text.toString(),
                    publicNumberEditText.text.toString(),
                    publicYouEditText.text.toString(),
                    publicInfoEditText.text.toString(),
                    publicAdditionalInfoEditText.text.toString(),
                    null
                )

                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    if (FirebaseHelper.isInternetConnected(this)) {
                        getCurrentLocationAndSendDataPublic(data, true)

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // No internet, send live location as SMS
                        getCurrentLocationAndSendDataPublic(data, false)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                }
            }
        }
    }

    private fun getCurrentLocationAndSendDataFamily(data: FamilyViolenceData, net: Boolean) {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {
            locationManager.requestSingleUpdate(
                LocationManager.GPS_PROVIDER,
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        if(net){
                            data.geografskaLokacija = GeoPoint(location.latitude, location.longitude)
                            FirebaseHelper.saveData(data, "family_violence", this@ViolenceActivity)
                        }else{
                            SmsHelper.sendSMS(data, GeoPoint(location.latitude, location.longitude), this@ViolenceActivity)
                        }

                        locationManager.removeUpdates(this)
                    }
                },
                null
            )
        } catch (ex: SecurityException) {
            Toast.makeText(this, "Dozvolite pristup vašoj lokaciji.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentLocationAndSendDataPublic(data: PublicViolenceData, net: Boolean) {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {
            locationManager.requestSingleUpdate(
                LocationManager.GPS_PROVIDER,
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        if(net){
                            data.geografskaLokacija = GeoPoint(location.latitude, location.longitude)
                            FirebaseHelper.saveData(data, "public_violence", this@ViolenceActivity)
                        }else{
                            SmsHelper.sendSMS(data, GeoPoint(location.latitude, location.longitude), this@ViolenceActivity)
                        }

                        locationManager.removeUpdates(this)
                    }
                },
                null
            )
        } catch (ex: SecurityException) {
            Toast.makeText(this, "Dozvolite pristup vašoj lokaciji.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(familyViolence){
                    val data = FamilyViolenceData(
                        familyViolencePersonEditText.text.toString(),
                        familyInjuryEditText.text.toString(),
                        youEditText.text.toString(),
                        susSituationEditText.text.toString(),
                        suspectEditText.text.toString(),
                        familyLocationEditText.text.toString(),
                        null
                    )

                    if(FirebaseHelper.isInternetConnected(this)){
                        getCurrentLocationAndSendDataFamily(data, true)
                    }else{
                        getCurrentLocationAndSendDataFamily(data, false)
                    }
                }else{
                    val data = PublicViolenceData(
                        publicLocationEditText.text.toString(),
                        publicNumberEditText.text.toString(),
                        publicYouEditText.text.toString(),
                        publicInfoEditText.text.toString(),
                        publicAdditionalInfoEditText.text.toString(),
                        null
                    )

                    if(FirebaseHelper.isInternetConnected(this)){
                        getCurrentLocationAndSendDataPublic(data, true)
                    }else{
                        getCurrentLocationAndSendDataPublic(data, false)
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    "Dozvolite pristup vašoj lokaciji.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}