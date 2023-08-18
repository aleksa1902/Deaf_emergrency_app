package com.example.emergrency_app.ambulance.data

import com.google.firebase.firestore.GeoPoint

data class MentalCrisesData(
    val simptomi: String = "",
    val prisutnostSimptoma: String = "",
    val istorijaDijagnostika: String = "",
    val terapijaLekovi: String = "",
    val sigurnost: String = "",
    var geografskaLokacija: GeoPoint? = null
)
