package com.google.firebase.codelab.friendlychat.model

import android.util.Log
import kotlin.math.PI
import kotlin.math.cos

/*
data class Room(val room:String,val address:String,val lat:Double,val lon:Double,val area:Rect, val floor:String){
    constructor(room: String, address: String, lat: Double, lon: Double, floor: String) : this(
        room,
        address,
        lat,
        lon,
        Rect.calculateRect(lat, lon),
        floor
    )

}

 */
class Room(public val room:String,public val address:String,public val lat:Double,public val lon:Double,public val floor:String,size:Double = 25.0){
    private val rect: Rect = Rect.calculateRect(lat,lon,size)

    fun isInsideRoom(lat:Double,lon:Double):Boolean{
        Log.d("Room","Input={$lat, $lon} Rect=> point0={${rect.lat_0}, ${rect.lon_0}} point1={${rect.lat_1}, ${rect.lon_1}}")
        return isPointInsideRectangle(lat,lon,rect.lat_0,rect.lon_0,rect.lat_1,rect.lon_1)
    }

    private fun isPointInsideRectangle(x: Double, y: Double, rectX1: Double, rectY1: Double, rectX2: Double, rectY2: Double): Boolean {
        return x >= minOf(rectX1, rectX2) && x <= maxOf(rectX1, rectX2) &&
                y >= minOf(rectY1, rectY2) && y <= maxOf(rectY1, rectY2)
    }

    override fun toString(): String {
        return "Room(room='$room', address='$address', lat=$lat, lon=$lon, floor='$floor')"
    }
}
data class FakeRoom(
    val roomName:String? = null,
    val address:String? = null,
    val lat:String? = null,
    val lon:String? = null,
    val floor:String? = null,
    val size:String? = null
)

data class Rect(public val lat_0:Double,public val lon_0:Double,public val lat_1:Double,public val lon_1:Double){
    companion object {

        fun calculateRect(lat: Double, lon: Double,size:Double): Rect {
            Log.d("Rect","Calculating... $lat, $lon")
            val R = 6378137.0

            val dn = size
            val de = size

            val latRad = lat * PI / 180

            val dLat = dn / R
            val dLon = de / (R * cos(latRad))

            val lat0 = lat + dLat * 180 / PI
            val lon0 = lon + dLon * 180 / PI
            val lat1 = lat - dLat * 180 / PI
            val lon1 = lon - dLon * 180 / PI

            Log.d("Rect","Point 0 = $lat0, $lon0")
            Log.d("Rect","Point 1 = $lat1, $lon1")

            return Rect(lat0, lon0, lat1, lon1)
        }
    }
}