package com.example.emergrency_app.helper

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.emergrency_app.ambulance.data.AmbulanceData
import com.example.emergrency_app.firefighters.data.FirefighterData
import com.example.emergrency_app.police.data.PoliceData
import org.json.JSONObject

object FirebaseHelper {

    fun saveDataPolice(data: PoliceData, token: String, context: Context) {
        val url = "https://deaf-emergency-api-7y7tv3tc.ew.gateway.dev/police"

        val geoLocationObject = JSONObject()
        geoLocationObject.put("latitude", data.geoLocation?.latitude)
        geoLocationObject.put("longitude", data.geoLocation?.longitude)

        val stringQuestions = data.questions.mapValues { it.value.toString() }

        val params = JSONObject()
        params.put("geoLocation", geoLocationObject)
        params.put("questions", JSONObject(stringQuestions))
        params.put("status", data.status)
        params.put("type", data.type)
        params.put("userId", data.userId)

        val request = object : JsonObjectRequest(
            Method.POST, url, params,
            Response.Listener {
                Toast.makeText(context, "Uspesno pozvana policija.", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener {
                Toast.makeText(
                    context,
                    "Nesto nije proslo kako treba, pokusajte ponovo.",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            // Override getHeaders() to set the Bearer token
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.putAll(super.getHeaders())
                headers["Authorization"] = "Bearer $token"

                return headers
            }
        }
        // Add the request to the RequestQueue
        Volley.newRequestQueue(context).add(request)
    }

    fun saveDataAmbulance(data: AmbulanceData, token: String, context: Context) {
        val url = "https://deaf-emergency-api-7y7tv3tc.ew.gateway.dev/ambulance"

        val geoLocationObject = JSONObject()
        geoLocationObject.put("latitude", data.geoLocation?.latitude)
        geoLocationObject.put("longitude", data.geoLocation?.longitude)

        val stringQuestions = data.questions.mapValues { it.value.toString() }

        val params = JSONObject()
        params.put("geoLocation", geoLocationObject)
        params.put("questions", JSONObject(stringQuestions))
        params.put("status", data.status)
        params.put("type", data.type)
        params.put("userId", data.userId)

        val request = object : JsonObjectRequest(
            Method.POST, url, params,
            Response.Listener {
                Toast.makeText(context, "Uspesno pozvana hitna pomoc.", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener {
                Toast.makeText(
                    context,
                    "Nesto nije proslo kako treba, pokusajte ponovo.",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            // Override getHeaders() to set the Bearer token
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.putAll(super.getHeaders())
                headers["Authorization"] = "Bearer $token"

                return headers
            }
        }
        // Add the request to the RequestQueue
        Volley.newRequestQueue(context).add(request)
    }

    fun saveDataFirefighters(data: FirefighterData, token: String, context: Context) {
        val url = "https://deaf-emergency-api-7y7tv3tc.ew.gateway.dev/firefighters"

        val geoLocationObject = JSONObject()
        geoLocationObject.put("latitude", data.geoLocation?.latitude)
        geoLocationObject.put("longitude", data.geoLocation?.longitude)

        val stringQuestions = data.questions.mapValues { it.value.toString() }

        val params = JSONObject()
        params.put("geoLocation", geoLocationObject)
        params.put("questions", JSONObject(stringQuestions))
        params.put("status", data.status)
        params.put("type", data.type)
        params.put("userId", data.userId)

        val request = object : JsonObjectRequest(
            Method.POST, url, params,
            Response.Listener {
                Toast.makeText(context, "Uspesno pozvani vatrogasci.", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener {
                Toast.makeText(
                    context,
                    "Nesto nije proslo kako treba, pokusajte ponovo.",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            // Override getHeaders() to set the Bearer token
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.putAll(super.getHeaders())
                headers["Authorization"] = "Bearer $token"

                return headers
            }
        }
        // Add the request to the RequestQueue
        Volley.newRequestQueue(context).add(request)
    }

    fun isInternetConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}