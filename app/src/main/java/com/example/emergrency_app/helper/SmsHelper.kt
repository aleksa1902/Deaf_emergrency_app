package com.example.emergrency_app.helper

import android.content.Context
import android.telephony.SmsManager
import android.widget.Toast
import com.google.firebase.firestore.GeoPoint

object SmsHelper {

    fun <T> sendSMS(data: T, location: GeoPoint?, context: Context) {
        val smsManager = SmsManager.getDefault()

        val message = "Data Info:\n" +
                "Location: ${location?.latitude}, ${location?.longitude}\n" +
                "Additional Info: $data" // Ovde koristimo generički tip za prikaz dodatnih informacija

        val phoneNumber = "+012345678"

        smsManager.sendTextMessage(phoneNumber, null, message, null, null)

        Toast.makeText(
            context,
            "Live lokacija je poslata putem SMS-a.",
            Toast.LENGTH_SHORT
        ).show()
    }
}