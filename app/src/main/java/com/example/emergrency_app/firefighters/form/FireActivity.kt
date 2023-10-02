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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class FireActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 123

    private lateinit var fireLocationEditText: EditText
    private lateinit var fireWhatEditText: EditText
    private lateinit var stuckEditText: EditText
    private lateinit var dangerEditText: EditText
    private lateinit var additionalInfoEditText: EditText
    private lateinit var sendInfoButton: Button

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firefighters_fire)

        fireLocationEditText = findViewById(R.id.fireLocationEditText)
        fireWhatEditText = findViewById(R.id.fireWhatEditText)
        stuckEditText = findViewById(R.id.stuckEditText)
        dangerEditText = findViewById(R.id.dangerEditText)
        additionalInfoEditText = findViewById(R.id.additionalInfoEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        db = FirebaseFirestore.getInstance()

        sendInfoButton.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            val currentUser: FirebaseUser? = auth.currentUser
            val userId: String? = currentUser?.uid

            val data = mapOf(
                "Gde se nalazi požar?" to fireLocationEditText.text.toString(),
                "Kakva je priroda požara (kuća, zgrada, vozilo, šuma itd.)?" to fireWhatEditText.text.toString(),
                "Je li netko zadržan unutar požarnog područja?" to stuckEditText.text.toString(),
                "Postoji li opasnost od širenja požara na druge objekte?" to dangerEditText.text.toString(),
                "Imate li informacije o prisutnosti opasnih materijala?" to additionalInfoEditText.text.toString()
            )

            val firefighterData = FirefighterData()
            firefighterData.userId = userId.toString()
            firefighterData.type = "fire"
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
                            FirebaseHelper.saveData(data, "firefighters", this@FireActivity)
                        }else{
                            SmsHelper.sendSMS(data, GeoPoint(location.latitude, location.longitude), this@FireActivity)
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
                    "Gde se nalazi požar?" to fireLocationEditText.text.toString(),
                    "Kakva je priroda požara (kuća, zgrada, vozilo, šuma itd.)?" to fireWhatEditText.text.toString(),
                    "Je li netko zadržan unutar požarnog područja?" to stuckEditText.text.toString(),
                    "Postoji li opasnost od širenja požara na druge objekte?" to dangerEditText.text.toString(),
                    "Imate li informacije o prisutnosti opasnih materijala?" to additionalInfoEditText.text.toString()
                )

                val firefighterData = FirefighterData()
                firefighterData.userId = userId.toString()
                firefighterData.type = "fire"
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