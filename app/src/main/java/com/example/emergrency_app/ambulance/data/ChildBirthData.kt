package com.example.emergrency_app.ambulance.data

import com.google.firebase.firestore.GeoPoint

data class ChildBirthData(
    val cestiTrudovi: String = "",
    val kolikoTrajuTrudovi: String = "",
    val komplikacija: String = "",
    val vodenjak: String = "",
    val istorijaProblema: String = "",
    var geografskaLokacija: GeoPoint? = null
)
