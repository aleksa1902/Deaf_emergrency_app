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

class MentalCrisesActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 123

    private lateinit var symptomsEditText: EditText
    private lateinit var durationEditText: EditText
    private lateinit var historyEditText: EditText
    private lateinit var medicinesEditText: EditText
    private lateinit var mentalSafeEditText: EditText
    private lateinit var sendInfoButton: Button

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambulance_mental_crises)

        symptomsEditText = findViewById(R.id.symptomsEditText)
        durationEditText = findViewById(R.id.durationEditText)
        historyEditText = findViewById(R.id.historyEditText)
        medicinesEditText = findViewById(R.id.medicinesEditText)
        mentalSafeEditText = findViewById(R.id.mentalSafeEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        db = FirebaseFirestore.getInstance()

        sendInfoButton.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            val currentUser: FirebaseUser? = auth.currentUser
            val userId: String? = currentUser?.uid

            val data = mapOf(
                "Koliko dugo su prisutni ti simptomi?" to durationEditText.text.toString(),
                "Je li osoba već bila dijagnosticirana s mentalnim poremećajem?" to historyEditText.text.toString(),
                "Je li osoba pod terapijom ili uzima lekove?" to medicinesEditText.text.toString(),
                "Je li osoba trenutno sigurna za sebe ili druge?" to mentalSafeEditText.text.toString(),
                "Koje simptome osoba doživljava (depresija, anksioznost, sucidne misli itd.)?" to symptomsEditText.text.toString()
            )

            val ambulanceData = AmbulanceData()
            ambulanceData.userId = userId.toString()
            ambulanceData.type = "medical_crises"
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
                            FirebaseHelper.saveData(data, "ambulance", this@MentalCrisesActivity)
                        }else{
                            SmsHelper.sendSMS(data, GeoPoint(location.latitude, location.longitude), this@MentalCrisesActivity)
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
                    "Koliko dugo su prisutni ti simptomi?" to durationEditText.text.toString(),
                    "Je li osoba već bila dijagnosticirana s mentalnim poremećajem?" to historyEditText.text.toString(),
                    "Je li osoba pod terapijom ili uzima lekove?" to medicinesEditText.text.toString(),
                    "Je li osoba trenutno sigurna za sebe ili druge?" to mentalSafeEditText.text.toString(),
                    "Koje simptome osoba doživljava (depresija, anksioznost, sucidne misli itd.)?" to symptomsEditText.text.toString()
                )

                val ambulanceData = AmbulanceData()
                ambulanceData.userId = userId.toString()
                ambulanceData.type = "medical_crises"
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