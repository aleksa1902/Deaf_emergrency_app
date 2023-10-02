    package com.example.emergrency_app.police.data

    import com.google.firebase.firestore.GeoPoint

    data class PoliceData(
        var userId: String = "",
        var type: String = "",
        var status: String = "",
        var questions: Map<String, Any> = emptyMap(),
        var geoLocation: GeoPoint? = null
    )
