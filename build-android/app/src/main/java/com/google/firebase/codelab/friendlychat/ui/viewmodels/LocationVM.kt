package com.google.firebase.codelab.friendlychat.ui.viewmodels

import com.google.firebase.codelab.friendlychat.model.Room
import android.app.Application
import android.location.Location
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.codelab.friendlychat.data.networking.DataLocationSource
import com.google.firebase.codelab.friendlychat.data.sensors.GpsManager
import com.google.firebase.codelab.friendlychat.model.FakeRoom
import com.google.firebase.codelab.friendlychat.model.GeocodeResponse
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.codelab.friendlychat.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LocationVM(
    application: Application,
    private val activity: ComponentActivity,
    val db: FirebaseDatabase,
    val messagesRef: DatabaseReference,
    val auth: FirebaseAuth,
    val usersRef: DatabaseReference,
    val roomsRef: DatabaseReference
):AndroidViewModel(application) {
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
        listenForRooms()
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
        val currentRoom = getCurrentRoom("Room")
        if(currentRoom != null)
            return currentRoom.room.toString()
        return "No room"
    }

    private fun getCurrentRoom(roomName:String,size:Double):Room?{
        updateLocation()
        val l = _geoLocation.value
        if(l != null){
            val address = l?.results?.first()?.formatted_address
            val lat = l?.results?.first()?.geometry?.location?.lat
            val lng = l?.results?.first()?.geometry?.location?.lng

            val room = Room(roomName,address?:"null",lat?:0.0,lng?:0.0,"First",size)
            _rooms.value += room
            Log.d("DataVM","Added room ${room.toString()}")
            return room
        }else
            return null
    }
    private fun getCurrentRoom(roomName:String):Room?{
        return getCurrentRoom(roomName, size = 25.0)
    }
    //Den ska ge rummet jag är i och även updtera min position
    fun getMyCurrentRoomName(): String{
        val myRoom = getCurrentRoom("Room")
        if(myRoom==null)
            return "Hemma"
        val latitude = myRoom.lat
        val longitude = myRoom.lon

        //TODO hämta istället rum från databas
        val makerSpace = Room("Makerspace","Blickagången",59.22126919999999,17.9377919,"7")
        val willys = Room("Willys","Röntgenvägen 7, 141 52 Huddinge", 59.222811,17.938936,"0")
        val h = Room("H","Röntgenvägen 7, 141 52 Huddinge", 59.22159,17.93675,"0")
        //todo matsalen t2 osv redovisnings rummet t65 Huddinge

        //TODO kontrollera foreach loop för varenda rum i lista
        Log.d("LocationVM","number of rooms=${_rooms.value.size}")
        Log.d("LocationVM","rooms=${_rooms.value}")
        if( makerSpace.isInsideRoom(latitude,longitude) ){
            Log.d("LocationVM", makerSpace.room)
            return makerSpace.room
        }
        else if(willys.isInsideRoom(latitude,longitude)){
            Log.d("LocationVM", willys.room)
            return willys.room
        }
        else if(h.isInsideRoom(latitude,longitude)){
            Log.d("LocationVM", h.room)
            return h.room
        }
        for(room in _rooms.value){
            Log.d("LocationVM","Rooms from db = $room")
            if(room.isInsideRoom(latitude,longitude)){
                Log.d("LocationVM","Is inside room from db = ${room}")
                return room.room
            }
        }
        Log.d("LocationVM","No room found")
        return "Hemma"
    }

    private fun listenForRooms() {
        roomsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val roomsList = snapshot.children.mapNotNull { it.getValue(Room::class.java) }
                _rooms.value = roomsList
                Log.d("MarcusTAG rooms",roomsList.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ChatVM", "Error listening for rooms", error.toException())
            }
        })
    }

    fun createRoom(roomName:String,size:Double){
        val room = getCurrentRoom(roomName,size)
        if(room==null)
            return
        roomsRef.push().setValue(FakeRoom(roomName,room.address,room.lat.toString(),room.lon.toString(),room.floor,size.toString()))
    }

    fun getRooms():List<Room>{
        roomsRef.get()

        return emptyList()
    }
    fun updateUser() {
        fun updateUser() {
            val user = auth.currentUser
            if (user != null) {
                val userRef = db.getReference("users/${user.uid}")
                userRef.child("room").setValue(getMyCurrentRoomName())
                if (auth.currentUser?.displayName == "null" || auth.currentUser == null) {
                    userRef.child("photoUrl").setValue(auth.currentUser?.displayName)
                }
            }
        }
    }
    fun updateUserOnInterval(delay: Long) {
        viewModelScope.launch {
            while (isActive) {
                updateUser()
                delay(delay)
            }
        }
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