package com.example.emergrency_app.helper

import android.content.Context
import android.telephony.SmsManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint

object SmsHelper {

    fun <T> sendSMS(data: T, location: GeoPoint?, context: Context) {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        val uid = currentUser?.uid // Dohvatanje UID trenutno prijavljenog korisnika
        val smsManager = SmsManager.getDefault()

        val message = "Podaci:\n" +
                "Korisnik UID: ${uid.toString()}" + // UID korisnika, da bi znali koji je korisnik
                "Lokacija: ${location?.latitude}, ${location?.longitude}\n" + // Trenutna lokacija
                "Dodatne informacije: $data" // Prikaz dodatnih informacija, tj. odgovora

        val phoneNumber = "+012345678"

        smsManager.sendTextMessage(phoneNumber, null, message, null, null)

        Toast.makeText(
            context,
            "Live lokacija je poslata putem SMS-a.",
            Toast.LENGTH_SHORT
        ).show()
    }
}