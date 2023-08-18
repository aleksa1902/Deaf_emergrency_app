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
import com.example.emergrency_app.ambulance.data.SuddenDeteriorationData
import com.example.emergrency_app.helper.FirebaseHelper
import com.example.emergrency_app.helper.SmsHelper
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class SuddenDeteriorationActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 123

    private lateinit var symptomsEditText: EditText
    private lateinit var factorsEditText: EditText
    private lateinit var situationEditText: EditText
    private lateinit var changesEditText: EditText
    private lateinit var additionalInfoEditText: EditText
    private lateinit var sendInfoButton: Button

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambulance_sudden_deterioration)

        symptomsEditText = findViewById(R.id.symptomsEditText)
        factorsEditText = findViewById(R.id.factorsEditText)
        situationEditText = findViewById(R.id.situationEditText)
        changesEditText = findViewById(R.id.changesEditText)
        additionalInfoEditText = findViewById(R.id.additionalInfoEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        db = FirebaseFirestore.getInstance()

        sendInfoButton.setOnClickListener {
            val data = SuddenDeteriorationData(
                factorsEditText.text.toString(),
                situationEditText.text.toString(),
                changesEditText.text.toString(),
                additionalInfoEditText.text.toString(),
                symptomsEditText.text.toString(),
                null
            )

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (FirebaseHelper.isInternetConnected(this)) {
                    getCurrentLocationAndSendData(data, true)

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // No internet, send live location as SMS
                    getCurrentLocationAndSendData(data, false)
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

    private fun getCurrentLocationAndSendData(data: SuddenDeteriorationData, net: Boolean) {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {
            locationManager.requestSingleUpdate(
                LocationManager.GPS_PROVIDER,
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        if(net){
                            data.geografskaLokacija = GeoPoint(location.latitude, location.longitude)
                            FirebaseHelper.saveData(data, "sudden_deterioration", this@SuddenDeteriorationActivity)
                        }else{
                            SmsHelper.sendSMS(data, GeoPoint(location.latitude, location.longitude), this@SuddenDeteriorationActivity)
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
                val data = SuddenDeteriorationData(
                    factorsEditText.text.toString(),
                    situationEditText.text.toString(),
                    changesEditText.text.toString(),
                    additionalInfoEditText.text.toString(),
                    symptomsEditText.text.toString(),
                    null
                )
                if(FirebaseHelper.isInternetConnected(this)){
                    getCurrentLocationAndSendData(data, true)
                }else{
                    getCurrentLocationAndSendData(data, false)
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