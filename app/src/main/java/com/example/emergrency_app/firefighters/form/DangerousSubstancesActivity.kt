package com.example.emergrency_app.firefighters.form

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.emergrency_app.MainActivity
import com.example.emergrency_app.R
import com.example.emergrency_app.firefighters.data.FirefighterData
import com.example.emergrency_app.helper.FirebaseHelper
import com.example.emergrency_app.helper.SmsHelper
import com.google.firebase.firestore.GeoPoint

class DangerousSubstancesActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 123

    private lateinit var radioGroupDangerousSubstancesType: RadioGroup
    private lateinit var layoutSpilling: LinearLayout
    private lateinit var layoutRemoving: LinearLayout
    private lateinit var buttonSendInformation: Button

    private lateinit var thingPersonEditText: EditText
    private lateinit var locationThingEditText: EditText
    private lateinit var dangerEditText: EditText
    private lateinit var numEditText: EditText
    private lateinit var additionalInfoEditText: EditText

    private lateinit var hazardousEditText: EditText
    private lateinit var hazardousLocationEditText: EditText
    private lateinit var dangerSpreadEditText: EditText
    private lateinit var safeEditText: EditText
    private lateinit var numPersonEditText: EditText

    private var spilingThing: Boolean = false
    private var check: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firefighters_dangerous_substances)

        radioGroupDangerousSubstancesType = findViewById(R.id.radioGroupDangerousSubstancesType)
        layoutSpilling = findViewById(R.id.layoutSpilling)
        layoutRemoving = findViewById(R.id.layoutRemoving)
        buttonSendInformation = findViewById(R.id.buttonSendInformation)

        radioGroupDangerousSubstancesType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonSpilling -> {
                    layoutSpilling.visibility = View.VISIBLE
                    layoutRemoving.visibility = View.GONE
                    buttonSendInformation.visibility = View.VISIBLE

                    thingPersonEditText = findViewById(R.id.thingPersonEditText)
                    locationThingEditText = findViewById(R.id.locationThingEditText)
                    dangerEditText = findViewById(R.id.dangerEditText)
                    numEditText = findViewById(R.id.numEditText)
                    additionalInfoEditText = findViewById(R.id.additionalInfoEditText)

                    spilingThing = true
                    check = true
                }
                R.id.radioButtonRemoving -> {
                    layoutSpilling.visibility = View.GONE
                    layoutRemoving.visibility = View.VISIBLE
                    buttonSendInformation.visibility = View.VISIBLE

                    hazardousEditText = findViewById(R.id.hazardousEditText)
                    hazardousLocationEditText = findViewById(R.id.hazardousLocationEditText)
                    dangerSpreadEditText = findViewById(R.id.dangerSpreadEditText)
                    safeEditText = findViewById(R.id.safeEditText)
                    numPersonEditText = findViewById(R.id.numPersonEditText)

                    spilingThing = false
                    check = true
                }
            }
        }

        // Ovdje dodajte kod za obradu informacija kad korisnik pritisne "Posalji informacije"
        buttonSendInformation.setOnClickListener {
            if(check && spilingThing){
                val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                val userId: String? = sharedPreferences.getString("userId", null).toString()

                val data = mapOf(
                    "Koja stvar izliva i koliko je opasna?" to thingPersonEditText.text.toString(),
                    "Gde se dogodilo izlivanje?" to locationThingEditText.text.toString(),
                    "Postoji li opasnost od eksplozije ili širenja stvari?" to dangerEditText.text.toString(),
                    "Jeste li primetili povređene osobe ili prisutnost opasnih para?" to numEditText.text.toString(),
                    "Imate li informacije o količini i vrsti stvari?" to additionalInfoEditText.text.toString()
                )

                val firefighterData = FirefighterData()
                firefighterData.userId = userId.toString()
                firefighterData.type = "spiling_thing"
                firefighterData.questions = data
                firefighterData.status = "in_progress"

                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    if (FirebaseHelper.isInternetConnected(this)) {
                        getCurrentLocationAndSendDataSpilling(firefighterData, true)

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // No internet, send live location as SMS
                        getCurrentLocationAndSendDataSpilling(firefighterData, false)
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
            }else{
                val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                val userId: String? = sharedPreferences.getString("userId", null).toString()

                val data = mapOf(
                    "Koja je vrsta opasnog materijala ili hemikalije?" to hazardousEditText.text.toString(),
                    "Gde se nalazi stvar koja zahteva uklanjanje?" to hazardousLocationEditText.text.toString(),
                    "Postoji li opasnost od širenja stvari?" to dangerSpreadEditText.text.toString(),
                    "Je li područje sigurno za intervenciju ili su potrebne posebne mere zaštite?" to safeEditText.text.toString(),
                    "Jeste li primijetili prisutnost povređenih osoba?" to numPersonEditText.text.toString()
                )

                val firefighterData = FirefighterData()
                firefighterData.userId = userId.toString()
                firefighterData.type = "hazardous"
                firefighterData.questions = data
                firefighterData.status = "in_progress"

                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    if (FirebaseHelper.isInternetConnected(this)) {
                        getCurrentLocationAndSendDataRemoving(firefighterData, true)

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // No internet, send live location as SMS
                        getCurrentLocationAndSendDataRemoving(firefighterData, false)
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
    }

    private fun getCurrentLocationAndSendDataSpilling(data: FirefighterData, net: Boolean) {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {
            locationManager.requestSingleUpdate(
                LocationManager.GPS_PROVIDER,
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        if(net){
                            data.geoLocation = GeoPoint(location.latitude, location.longitude)
                            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                            FirebaseHelper.saveDataFirefighters(data, sharedPreferences.getString("token", null).toString(), this@DangerousSubstancesActivity)
                        }else{
                            SmsHelper.sendSMS(data, GeoPoint(location.latitude, location.longitude), this@DangerousSubstancesActivity)
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

    private fun getCurrentLocationAndSendDataRemoving(data: FirefighterData, net: Boolean) {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {
            locationManager.requestSingleUpdate(
                LocationManager.GPS_PROVIDER,
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        if(net){
                            data.geoLocation = GeoPoint(location.latitude, location.longitude)
                            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                            FirebaseHelper.saveDataFirefighters(data, sharedPreferences.getString("token", null).toString(), this@DangerousSubstancesActivity)
                        }else{
                            SmsHelper.sendSMS(data, GeoPoint(location.latitude, location.longitude), this@DangerousSubstancesActivity)
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
                if(spilingThing){
                    val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    val userId: String? = sharedPreferences.getString("userId", null).toString()

                    val data = mapOf(
                        "Koja stvar izliva i koliko je opasna?" to thingPersonEditText.text.toString(),
                        "Gde se dogodilo izlivanje?" to locationThingEditText.text.toString(),
                        "Postoji li opasnost od eksplozije ili širenja stvari?" to dangerEditText.text.toString(),
                        "Jeste li primetili povređene osobe ili prisutnost opasnih para?" to numEditText.text.toString(),
                        "Imate li informacije o količini i vrsti stvari?" to additionalInfoEditText.text.toString()
                    )

                    val firefighterData = FirefighterData()
                    firefighterData.userId = userId.toString()
                    firefighterData.type = "spiling_thing"
                    firefighterData.questions = data
                    firefighterData.status = "in_progress"

                    if(FirebaseHelper.isInternetConnected(this)){
                        getCurrentLocationAndSendDataSpilling(firefighterData, true)
                    }else{
                        getCurrentLocationAndSendDataSpilling(firefighterData, false)
                    }
                }else{
                    val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    val userId: String? = sharedPreferences.getString("userId", null).toString()

                    val data = mapOf(
                        "Koja je vrsta opasnog materijala ili hemikalije?" to hazardousEditText.text.toString(),
                        "Gde se nalazi stvar koja zahteva uklanjanje?" to hazardousLocationEditText.text.toString(),
                        "Postoji li opasnost od širenja stvari?" to dangerSpreadEditText.text.toString(),
                        "Je li područje sigurno za intervenciju ili su potrebne posebne mere zaštite?" to safeEditText.text.toString(),
                        "Jeste li primijetili prisutnost povređenih osoba?" to numPersonEditText.text.toString()
                    )

                    val firefighterData = FirefighterData()
                    firefighterData.userId = userId.toString()
                    firefighterData.type = "hazardous"
                    firefighterData.questions = data
                    firefighterData.status = "in_progress"

                    if(FirebaseHelper.isInternetConnected(this)){
                        getCurrentLocationAndSendDataRemoving(firefighterData, true)
                    }else{
                        getCurrentLocationAndSendDataRemoving(firefighterData, false)
                    }
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