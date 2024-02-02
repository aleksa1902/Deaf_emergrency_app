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

class FloodsActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 123

    private lateinit var floodLocationEditText: EditText
    private lateinit var floodNumEditText: EditText
    private lateinit var stuckEditText: EditText
    private lateinit var dangerEditText: EditText
    private lateinit var additionalInfoEditText: EditText
    private lateinit var sendInfoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firefighters_floods)

        floodLocationEditText = findViewById(R.id.floodLocationEditText)
        floodNumEditText = findViewById(R.id.floodNumEditText)
        stuckEditText = findViewById(R.id.stuckEditText)
        dangerEditText = findViewById(R.id.dangerEditText)
        additionalInfoEditText = findViewById(R.id.additionalInfoEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        sendInfoButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val userId: String? = sharedPreferences.getString("userId", null).toString()

            val data = mapOf(
                "Gde se događa poplava?" to floodLocationEditText.text.toString(),
                "Koliko je osoba ugroženo ili zarobljeno?" to floodNumEditText.text.toString(),
                "Postoje li posebne potrebe za spašavanjem, poput djece, starijih osoba ili osoba s invaliditetom?" to stuckEditText.text.toString(),
                "Je li voda u porastu ili se povlači?" to dangerEditText.text.toString(),
                "Je li područje strujanja vode ili ima potencijalne opasnosti?" to additionalInfoEditText.text.toString()
            )

            val firefighterData = FirefighterData()
            firefighterData.userId = userId.toString()
            firefighterData.type = "floods"
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
                            FirebaseHelper.saveDataFirefighters(data, sharedPreferences.getString("token", null).toString(), this@FloodsActivity)
                        }else{
                            SmsHelper.sendSMS(data, GeoPoint(location.latitude, location.longitude), this@FloodsActivity)
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
                    "Gde se događa poplava?" to floodLocationEditText.text.toString(),
                    "Koliko je osoba ugroženo ili zarobljeno?" to floodNumEditText.text.toString(),
                    "Postoje li posebne potrebe za spašavanjem, poput djece, starijih osoba ili osoba s invaliditetom?" to stuckEditText.text.toString(),
                    "Je li voda u porastu ili se povlači?" to dangerEditText.text.toString(),
                    "Je li područje strujanja vode ili ima potencijalne opasnosti?" to additionalInfoEditText.text.toString()
                )

                val firefighterData = FirefighterData()
                firefighterData.userId = userId.toString()
                firefighterData.type = "floods"
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