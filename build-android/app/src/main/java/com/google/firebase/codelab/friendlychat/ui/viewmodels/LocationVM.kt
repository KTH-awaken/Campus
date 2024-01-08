package com.google.firebase.codelab.friendlychat.ui.viewmodels

import com.google.firebase.codelab.friendlychat.model.Room
import android.app.Application
import android.location.Location
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.AndroidViewModel

import com.google.firebase.codelab.friendlychat.data.networking.DataLocationSource
import com.google.firebase.codelab.friendlychat.data.sensors.GpsManager
import com.google.firebase.codelab.friendlychat.model.GeocodeResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking

class LocationVM(application: Application, private val activity: ComponentActivity):AndroidViewModel(application) {
    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    private lateinit var gpsManager : GpsManager

    init{
        gpsManager = GpsManager(activity) { location ->
            Log.d("MainActivity","Location = ${location.toString() ?: "Loading"}")
            _gpsLocation.value = location
            runBlocking {
                val geoLocationResult = DataLocationSource.getLocation(location.latitude,location.longitude)
                geoLocationResult.let{
                    val address = it?.results?.first()?.formatted_address
                    Log.d("Address","Geolocation = ${address?:"Address is null"}")
                    _geoLocation.value = it
                }
            }

        }
    }
    fun updateLocation(){
        gpsManager.requestLocationUpdates()
    }

    fun saveRoom(){
        val l = _geoLocation.value
        if(l != null){
            val address = l?.results?.first()?.formatted_address
            val lat = l?.results?.first()?.geometry?.location?.lat
            val lng = l?.results?.first()?.geometry?.location?.lng

            val room = Room("Room",address?:"null",lat?:0.0,lng?:0.0,"First")
            _rooms.value += room
            Log.d("DataVM","Added room ${room.toString()}")
        }
    }

    fun checkRoom(){

    }

    fun getMyCurrentRoom(): String{
        //TODO IMPLEMENT FOR REAL
        val l = _geoLocation.value
        if(l != null){
            val address = l?.results?.first()?.formatted_address
            val lat = l?.results?.first()?.geometry?.location?.lat
            val lng = l?.results?.first()?.geometry?.location?.lng

            val room = Room("Room",address?:"null",lat?:0.0,lng?:0.0,"First")
            _rooms.value += room
            Log.d("DataVM","Added room ${room.toString()}")
             return room.toString()
        }
        return "No room"
    }

    fun getMyCurrentRoomName(): String{
        val l = _geoLocation.value

        return "no room found"
    }

    private val _geoLocation = MutableStateFlow<GeocodeResponse?>(null)
    val geoLocation: StateFlow<GeocodeResponse?>
        get() = _geoLocation

    private val _gpsLocation = MutableStateFlow<Location?>(null)
    val gpsLocation: StateFlow<Location?>
        get() = _gpsLocation

    private val _rooms = MutableStateFlow<List<Room>>(emptyList())
    val rooms: StateFlow<List<Room>>
        get() = _rooms
}