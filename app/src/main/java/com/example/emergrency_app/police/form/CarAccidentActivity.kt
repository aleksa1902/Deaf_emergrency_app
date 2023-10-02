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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.emergrency_app.MainActivity
import com.example.emergrency_app.R
import com.example.emergrency_app.helper.FirebaseHelper
import com.example.emergrency_app.helper.SmsHelper
import com.example.emergrency_app.police.data.PoliceData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.json.JSONObject

class CarAccidentActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 123

    private lateinit var locationEditText: EditText
    private lateinit var carNumEditText: EditText
    private lateinit var injuryEditText: EditText
    private lateinit var infoDriversEditText: EditText
    private lateinit var suspectEditText: EditText
    private lateinit var sendInfoButton: Button

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_police_car_accident)

        locationEditText = findViewById(R.id.locationEditText)
        carNumEditText = findViewById(R.id.carNumEditText)
        injuryEditText = findViewById(R.id.injuryEditText)
        infoDriversEditText = findViewById(R.id.infoDriversEditText)
        suspectEditText = findViewById(R.id.suspectEditText)
        sendInfoButton = findViewById(R.id.sendInfoButton)

        db = FirebaseFirestore.getInstance()

        sendInfoButton.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            val currentUser: FirebaseUser? = auth.currentUser
            val userId: String? = currentUser?.uid

            val data = mapOf(
                "Gde se dogodila saobraćajna nesreća nesreća?" to locationEditText.text.toString(),
                "Koliko je vozila bilo uključeno?" to carNumEditText.text.toString(),
                "Da li je neko povređen?" to injuryEditText.text.toString(),
                "Imate li informacije o vozačima uključenim u saobraćajnoj nesreći?" to infoDriversEditText.text.toString(),
                "Jeste li primijetili bilo kakvo kršenje saobraćajnih pravila ili nepravilno ponašanje vozača?" to suspectEditText.text.toString()
            )

            val policeData = PoliceData()
            policeData.userId = userId.toString()
            policeData.type = "car_accident"
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

    private fun getCurrentLocationAndSendData(accidentData: PoliceData, net: Boolean) {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {
            locationManager.requestSingleUpdate(
                LocationManager.GPS_PROVIDER,
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        if(net){
                            accidentData.geoLocation = GeoPoint(location.latitude, location.longitude)
                            FirebaseHelper.saveData(accidentData, "police", this@CarAccidentActivity)
                        }else{
                            SmsHelper.sendSMS(accidentData, GeoPoint(location.latitude, location.longitude), this@CarAccidentActivity)
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
                    "Gde se dogodila saobraćajna nesreća nesreća?" to locationEditText.text.toString(),
                    "Koliko je vozila bilo uključeno?" to carNumEditText.text.toString(),
                    "Da li je neko povređen?" to injuryEditText.text.toString(),
                    "Imate li informacije o vozačima uključenim u saobraćajnoj nesreći?" to infoDriversEditText.text.toString(),
                    "Jeste li primijetili bilo kakvo kršenje saobraćajnih pravila ili nepravilno ponašanje vozača?" to suspectEditText.text.toString()
                )

                val policeData = PoliceData()
                policeData.userId = userId.toString()
                policeData.type = "car_accident"
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