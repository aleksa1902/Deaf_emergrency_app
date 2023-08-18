package com.example.emergrency_app.police.data

import com.google.firebase.firestore.GeoPoint

data class PublicViolenceData(
    val tacnaLokacija: String = "",
    val brojOsoba: String = "",
    val svedok: String = "",
    val opisNapadaca: String = "",
    val brojPovredjenih: String = "",
    var geografskaLokacija: GeoPoint? = null
)
