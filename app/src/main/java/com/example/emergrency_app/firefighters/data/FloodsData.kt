package com.example.emergrency_app.firefighters.data

import com.google.firebase.firestore.GeoPoint

data class FloodsData(
    val lokacijaPoplave: String = "",
    val brojOsobaZivotinja: String = "",
    val decaStariInvalidi: String = "",
    val informacijaPoplave: String = "",
    val situacijaPodrucje: String = "",
    val geografskaLokacija: GeoPoint? = null
)
