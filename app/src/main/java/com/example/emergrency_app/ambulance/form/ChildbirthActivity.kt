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
import com.google.firebase.firestore.GeoPoint

class ChildbirthActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 123

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
            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val userId: String? = sharedPreferences.getString("userId", null).toString()

            val data = mapOf(
                "Koliko traju trudovi?" to durationLaboursEditText.text.toString(),
                "Postoje li komplikacije ili posebne okolnosti vezane uz trudnoću?" to complicationsEditText.text.toString(),
                "Jeste li primijetili probijanje vodenjaka?" to waterBreakingEditText.text.toString(),
                "Je li trudnica imala prethodne probleme s trudnoćom ili porodom?" to additionalInfoEditText.text.toString(),
                "Koliko su česti trudovi?" to laboursEditText.text.toString()
            )

            val ambulanceData = AmbulanceData()
            ambulanceData.userId = userId.toString()
            ambulanceData.type = "childbirth"
            ambulanceData.questions = data
            ambulanceData.status = "in_progress"

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
                            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                            FirebaseHelper.saveDataAmbulance(data, sharedPreferences.getString("token", null).toString(), this@ChildbirthActivity)
                        }else{
                            SmsHelper.sendSMS(data, GeoPoint(location.latitude, location.longitude), this@ChildbirthActivity)
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
                    "Koliko traju trudovi?" to durationLaboursEditText.text.toString(),
                    "Postoje li komplikacije ili posebne okolnosti vezane uz trudnoću?" to complicationsEditText.text.toString(),
                    "Jeste li primijetili probijanje vodenjaka?" to waterBreakingEditText.text.toString(),
                    "Je li trudnica imala prethodne probleme s trudnoćom ili porodom?" to additionalInfoEditText.text.toString(),
                    "Koliko su česti trudovi?" to laboursEditText.text.toString()
                )

                val ambulanceData = AmbulanceData()
                ambulanceData.userId = userId.toString()
                ambulanceData.type = "childbirth"
                ambulanceData.questions = data
                ambulanceData.status = "in_progress"

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