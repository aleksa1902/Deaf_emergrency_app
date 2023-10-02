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

class InjuriesActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 123

    private lateinit var injuryEditText: EditText
    private lateinit var injuryTypeEditText: EditText
    private lateinit var injurySituationEditText: EditText
    private lateinit var bleedingEditText: EditText
    private lateinit var injuryAbleEditText: EditText
    private lateinit var sendInfoButton: Button

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambulance_injuries)

        injuryEditText = findViewById(R.id.injuryEditText)
        injuryTypeEditText = findViewById(R.id.injuryTypeEditText)
        injurySituationEditText = findViewById(R.id.injurySituationEditText)
        bleedingEditText = findViewById(R.id.bleedingEditText)
        injuryAbleEditText = findViewById(R.id.injuryAbleEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        db = FirebaseFirestore.getInstance()

        sendInfoButton.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            val currentUser: FirebaseUser? = auth.currentUser
            val userId: String? = currentUser?.uid

            val data = mapOf(
                "Koje vrste povreda osoba ima?" to injuryEditText.text.toString(),
                "Koliko je teška povreda?" to injuryTypeEditText.text.toString(),
                "Je li osoba izgubila svest nakon povrede?" to injurySituationEditText.text.toString(),
                "Postoji li krvarenje i koliko je obilno?" to bleedingEditText.text.toString(),
                "Je li osoba sposobna kretati se ili stajati?" to injuryAbleEditText.text.toString()
            )

            val ambulanceData = AmbulanceData()
            ambulanceData.userId = userId.toString()
            ambulanceData.type = "injury"
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
                            FirebaseHelper.saveData(data, "ambulance", this@InjuriesActivity)
                        }else{
                            SmsHelper.sendSMS(data, GeoPoint(location.latitude, location.longitude), this@InjuriesActivity)
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
                    "Koje vrste povreda osoba ima?" to injuryEditText.text.toString(),
                    "Koliko je teška povreda?" to injuryTypeEditText.text.toString(),
                    "Je li osoba izgubila svest nakon povrede?" to injurySituationEditText.text.toString(),
                    "Postoji li krvarenje i koliko je obilno?" to bleedingEditText.text.toString(),
                    "Je li osoba sposobna kretati se ili stajati?" to injuryAbleEditText.text.toString()
                )

                val ambulanceData = AmbulanceData()
                ambulanceData.userId = userId.toString()
                ambulanceData.type = "injury"
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