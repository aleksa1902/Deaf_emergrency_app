package com.example.emergrency_app.police.data

import com.google.firebase.firestore.GeoPoint

data class TheftData(
    val vremeKradje: String = "",
    val tacnaLokacija: String = "",
    val prirodaImovine: String = "",
    val opisPocinitelja: String = "",
    val sumnjivaAktivnost: String = "",
    val geografskaLokacija: GeoPoint? = null
)
