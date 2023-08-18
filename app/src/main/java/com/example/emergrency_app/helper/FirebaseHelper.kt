package com.example.emergrency_app.helper

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseHelper {

    private val db = FirebaseFirestore.getInstance()

    fun <T : Any> saveData(data: T, dbName: String, context: Context) {
        Log.d("SaveData", "Sadržaj data: $data")
        db.collection(dbName)
            .add(data)
            .addOnSuccessListener {
                Toast.makeText(
                    context,
                    "Podaci su uspešno sačuvani u bazi.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { error ->
                Toast.makeText(
                    context,
                    "Došlo je do greške prilikom upisivanja podataka: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun isInternetConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}