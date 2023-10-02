package com.example.emergrency_app.ambulance.form

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.emergrency_app.MainActivity
import com.example.emergrency_app.R
import com.example.emergrency_app.ambulance.data.AmbulanceData
import com.example.emergrency_app.helper.FirebaseHelper
import com.example.emergrency_app.helper.SmsHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class MedicalEmergencyActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 123

    private lateinit var symptomsEditText: EditText
    private lateinit var consciousEditText: EditText
    private lateinit var breathingEditText: EditText
    private lateinit var painEditText: EditText
    private lateinit var additionalInfoEditText: EditText
    private lateinit var sendInfoButton: Button

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambulance_emergency)

        symptomsEditText = findViewById(R.id.symptomsEditText)
        consciousEditText = findViewById(R.id.consciousEditText)
        breathingEditText = findViewById(R.id.breathingEditText)
        painEditText = findViewById(R.id.painEditText)
        additionalInfoEditText = findViewById(R.id.additionalInfoEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        db = FirebaseFirestore.getInstance()

        sendInfoButton.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            val currentUser: FirebaseUser? = auth.currentUser
            val userId: String? = currentUser?.uid

            val data = mapOf(
                "Je li osoba pri svesti?" to consciousEditText.text.toString(),
                "Diše li osoba normalno?" to breathingEditText.text.toString(),
                "Oseća li bol ili nelagodu?" to painEditText.text.toString(),
                "Je li osoba imala pre medicinske probleme?" to additionalInfoEditText.text.toString(),
                "Koje simptome osoba doživljava?" to symptomsEditText.text.toString()
            )

            val ambulanceData = AmbulanceData()
            ambulanceData.userId = userId.toString()
            ambulanceData.type = "medical_emergency"
            ambulanceData.questions = data

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (FirebaseHelper.isInternetConnected(this)) {
                    getCurrentLocationAndSendData(ambulanceData, true)

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // No internet, send live location as SMS
                    getCurrentLocationAndSendData(ambulanceData, false)
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

    private fun getCurrentLocationAndSendData(data: AmbulanceData, net: Boolean) {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {
            locationManager.requestSingleUpdate(
                LocationManager.GPS_PROVIDER,
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        if(net){
                            data.geoLocation = GeoPoint(location.latitude, location.longitude)
                            FirebaseHelper.saveData(data, "ambulance", this@MedicalEmergencyActivity)
                        }else{
                            SmsHelper.sendSMS(data, GeoPoint(location.latitude, location.longitude), this@MedicalEmergencyActivity)
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
                val auth = FirebaseAuth.getInstance()
                val currentUser: FirebaseUser? = auth.currentUser
                val userId: String? = currentUser?.uid

                val data = mapOf(
                    "Je li osoba pri svesti?" to consciousEditText.text.toString(),
                    "Diše li osoba normalno?" to breathingEditText.text.toString(),
                    "Oseća li bol ili nelagodu?" to painEditText.text.toString(),
                    "Je li osoba imala pre medicinske probleme?" to additionalInfoEditText.text.toString(),
                    "Koje simptome osoba doživljava?" to symptomsEditText.text.toString()
                )

                val ambulanceData = AmbulanceData()
                ambulanceData.userId = userId.toString()
                ambulanceData.type = "medical_emergency"
                ambulanceData.questions = data

                if(FirebaseHelper.isInternetConnected(this)){
                    getCurrentLocationAndSendData(ambulanceData, true)
                }else{
                    getCurrentLocationAndSendData(ambulanceData, false)
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