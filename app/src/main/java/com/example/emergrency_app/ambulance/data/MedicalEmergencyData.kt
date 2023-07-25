package com.example.emergrency_app.ambulance.data

import com.google.firebase.firestore.GeoPoint

data class MedicalEmergencyData(
    val simptomi: String = "",
    val svest: String = "",
    val disanje: String = "",
    val bol: String = "",
    val istorijaBolesti: String = "",
    val geografskaLokacija: GeoPoint? = null
)
