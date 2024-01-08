package com.google.firebase.codelab.friendlychat.data.networking

import android.util.Log
import com.google.firebase.codelab.friendlychat.model.GeocodeResponse

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import com.google.gson.Gson

object DataLocationSource{
    private fun getBaseURL(lat:Double,lon:Double):String{
        return "https://maps.googleapis.com/maps/api/geocode/json?latlng=${lat},${lon}&key=AIzaSyAS4XVRTd0IKpGohVOt8gozfl2iIcBXH0k"
    }

    suspend fun getLocation(lat:Double, lon:Double): GeocodeResponse?{
        val url_string = getBaseURL(lat,lon)
        val url = URL(url_string)
        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                val inputStream = connection.inputStream
                val json = inputStream.bufferedReader().use { it.readText() }
                Log.d("LocationDataSource","Input stream: $json")

                val gson = Gson()
                val geocodeResponse: GeocodeResponse = gson.fromJson(json, GeocodeResponse::class.java)
                Log.d("LocationDataSource","GeoCodeResponse object=${geocodeResponse}")
                geocodeResponse
            } catch (e: Exception) {
                Log.d("LocationDataSource", e.toString())
                null
            }
        }
    }
}