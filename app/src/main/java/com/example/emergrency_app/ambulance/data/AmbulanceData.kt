package com.example.emergrency_app.ambulance.data

import com.google.firebase.firestore.GeoPoint

data class AmbulanceData(
    var userId: String = "",
    var type: String = "",
    var status: String = "",
    var questions: Map<String, Any> = emptyMap(),
    var geoLocation: GeoPoint? = null
)
