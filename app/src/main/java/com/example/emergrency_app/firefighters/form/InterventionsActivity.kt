package com.example.emergrency_app.firefighters.form

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
import com.example.emergrency_app.firefighters.data.FirefighterData
import com.example.emergrency_app.helper.FirebaseHelper
import com.example.emergrency_app.helper.SmsHelper
import com.google.firebase.firestore.GeoPoint

class InterventionsActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 123

    private lateinit var dangerNatureEditText: EditText
    private lateinit var dangerLocationEditText: EditText
    private lateinit var situationEditText: EditText
    private lateinit var dangerEditText: EditText
    private lateinit var additionalInfoEditText: EditText
    private lateinit var sendInfoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firefighters_interventions)

        dangerNatureEditText = findViewById(R.id.dangerNatureEditText)
        dangerLocationEditText = findViewById(R.id.dangerLocationEditText)
        situationEditText = findViewById(R.id.situationEditText)
        dangerEditText = findViewById(R.id.dangerEditText)
        additionalInfoEditText = findViewById(R.id.additionalInfoEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        sendInfoButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val userId: String? = sharedPreferences.getString("userId", null).toString()

            val data = mapOf(
                "Koji je specifični tehnički problem?" to dangerNatureEditText.text.toString(),
                "Gde se dogodio problem (lift, tunel itd.)?" to dangerLocationEditText.text.toString(),
                "Jesu li osobe zarobljene ili imaju poteškoća u kretanju?" to situationEditText.text.toString(),
                "Je li područje sigurno za intervenciju?" to dangerEditText.text.toString(),
                "Trebaju li osobe medicinsku pomoć?" to additionalInfoEditText.text.toString()
            )

            val firefighterData = FirefighterData()
            firefighterData.userId = userId.toString()
            firefighterData.type = "intervention"
            firefighterData.questions = data
            firefighterData.status = "in_progress"

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (FirebaseHelper.isInternetConnected(this)) {
                    getCurrentLocationAndSendData(firefighterData, true)

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // No internet, send live location as SMS
                    getCurrentLocationAndSendData(firefighterData, false)
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

    private fun getCurrentLocationAndSendData(data: FirefighterData, net: Boolean) {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {
            locationManager.requestSingleUpdate(
                LocationManager.GPS_PROVIDER,
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        if(net){
                            data.geoLocation = GeoPoint(location.latitude, location.longitude)
                            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                            FirebaseHelper.saveDataFirefighters(data, sharedPreferences.getString("token", null).toString(), this@InterventionsActivity)
                        }else{
                            SmsHelper.sendSMS(data, GeoPoint(location.latitude, location.longitude), this@InterventionsActivity)
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
                    "Koji je specifični tehnički problem?" to dangerNatureEditText.text.toString(),
                    "Gde se dogodio problem (lift, tunel itd.)?" to dangerLocationEditText.text.toString(),
                    "Jesu li osobe zarobljene ili imaju poteškoća u kretanju?" to situationEditText.text.toString(),
                    "Je li područje sigurno za intervenciju?" to dangerEditText.text.toString(),
                    "Trebaju li osobe medicinsku pomoć?" to additionalInfoEditText.text.toString()
                )

                val firefighterData = FirefighterData()
                firefighterData.userId = userId.toString()
                firefighterData.type = "intervention"
                firefighterData.questions = data
                firefighterData.status = "in_progress"

                if(FirebaseHelper.isInternetConnected(this)){
                    getCurrentLocationAndSendData(firefighterData, true)
                }else{
                    getCurrentLocationAndSendData(firefighterData, false)
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