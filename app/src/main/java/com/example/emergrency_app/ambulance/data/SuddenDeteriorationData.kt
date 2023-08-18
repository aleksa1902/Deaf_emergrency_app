package com.example.emergrency_app.ambulance.data

import com.google.firebase.firestore.GeoPoint

data class SuddenDeteriorationData(
    val simptomi: String = "",
    val vremeSimptoma: String = "",
    val faktori: String = "",
    val istorijaSimptoma: String = "",
    val promeneStanja: String = "",
    var geografskaLokacija: GeoPoint? = null
)
