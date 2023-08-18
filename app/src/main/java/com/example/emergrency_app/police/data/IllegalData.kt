package com.example.emergrency_app.police.data

import com.google.firebase.firestore.GeoPoint

data class IllegalData(
    val vrstaSumnjiveAktivnosti: String = "",
    val opisOsobaVozila: String = "",
    val tacnaLokacija: String = "",
    val svedok: String = "",
    val trenutnaPretnja: String = "",
    var geografskaLokacija: GeoPoint? = null
)
