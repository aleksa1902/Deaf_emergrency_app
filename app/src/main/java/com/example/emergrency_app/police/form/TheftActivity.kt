package com.example.emergrency_app.police.form

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
import com.example.emergrency_app.helper.FirebaseHelper
import com.example.emergrency_app.helper.SmsHelper
import com.example.emergrency_app.police.data.PoliceData
import com.google.firebase.firestore.GeoPoint

class TheftActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 123

    private lateinit var timeEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var injuryEditText: EditText
    private lateinit var susSituationEditText: EditText
    private lateinit var suspectEditText: EditText
    private lateinit var sendInfoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_police_theft)

        timeEditText = findViewById(R.id.timeEditText)
        locationEditText = findViewById(R.id.locationEditText)
        injuryEditText = findViewById(R.id.injuryEditText)
        susSituationEditText = findViewById(R.id.susSituationEditText)
        suspectEditText = findViewById(R.id.suspectEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        sendInfoButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val userId: String? = sharedPreferences.getString("userId", null).toString()

            val data = mapOf(
                "Gde se tačno dogodila krađa/provala?" to locationEditText.text.toString(),
                "Kada se dogodila krađa/provala?" to timeEditText.text.toString(),
                "Kakva je bila priroda ukradene/uništene imovine?" to injuryEditText.text.toString(),
                "Jeste li primijetili bilo kakve sumnjive aktivnosti prije krađe/provale?" to susSituationEditText.text.toString(),
                "Da li imate li opis ili identifikaciju počinitelja?" to suspectEditText.text.toString()
            )

            val policeData = PoliceData()
            policeData.userId = userId.toString()
            policeData.type = "theft_activity"
            policeData.questions = data
            policeData.status = "in_progress"

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (FirebaseHelper.isInternetConnected(this)) {
                    getCurrentLocationAndSendData(policeData, true)

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // No internet, send live location as SMS
                    getCurrentLocationAndSendData(policeData, false)
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

    private fun getCurrentLocationAndSendData(data: PoliceData, net: Boolean) {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {
            locationManager.requestSingleUpdate(
                LocationManager.GPS_PROVIDER,
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        if(net){
                            data.geoLocation = GeoPoint(location.latitude, location.longitude)
                            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                            FirebaseHelper.saveDataPolice(data, sharedPreferences.getString("token", null).toString(), this@TheftActivity)
                        }else{
                            SmsHelper.sendSMS(data, GeoPoint(location.latitude, location.longitude), this@TheftActivity)
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
                val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                val userId: String? = sharedPreferences.getString("userId", null).toString()

                val data = mapOf(
                    "Gde se tačno dogodila krađa/provala?" to locationEditText.text.toString(),
                    "Kada se dogodila krađa/provala?" to timeEditText.text.toString(),
                    "Kakva je bila priroda ukradene/uništene imovine?" to injuryEditText.text.toString(),
                    "Jeste li primijetili bilo kakve sumnjive aktivnosti prije krađe/provale?" to susSituationEditText.text.toString(),
                    "Da li imate li opis ili identifikaciju počinitelja?" to suspectEditText.text.toString()
                )

                val policeData = PoliceData()
                policeData.userId = userId.toString()
                policeData.type = "theft_activity"
                policeData.questions = data
                policeData.status = "in_progress"

                if(FirebaseHelper.isInternetConnected(this)){
                    getCurrentLocationAndSendData(policeData, true)
                }else{
                    getCurrentLocationAndSendData(policeData, false)
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