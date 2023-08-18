package com.example.emergrency_app.police.data

import com.google.firebase.firestore.GeoPoint

data class FamilyViolenceData(
    val ukljuceneOsobe: String = "",
    val povrede: String = "",
    val svedok: String = "",
    val prethodniSlucajevi: String = "",
    val dodatnaInformacija: String = "",
    val lokacija: String = "",
    var geografskaLokacija: GeoPoint? = null
)
