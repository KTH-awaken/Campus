package com.google.firebase.codelab.friendlychat.model

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
class Room(public val room:String,public val address:String,public val lat:Double,public val lon:Double,public val floor:String){
    private val rect = Rect
    init{
        rect.calculateRect(lat,lon)
    }

    fun isInsideRoom(lat:Double,lon:Double):Boolean{
        return false
    }

}

data class Rect(val lat_0:Double,val lon_0:Double,val lat_1:Double,val lon_1:Double){
    companion object {

        fun calculateRect(lat: Double, lon: Double): Rect {
            val R=6378137

            //offsets i meter
            val dn = 10
            val de = 10

            val dLat = dn/R
            val dLon = de/(R* cos(Math.PI*lat/180))

            val lat_0 = lat + dLat * 180/Math.PI
            val lon_0 = lon + dLon * 180/Math.PI

            val lat_1 = lat - dLat * 180/Math.PI
            val lon_1 = lon - dLon * 180/Math.PI

            return Rect(lat_0, lon_0, lat_1, lon_1)
        }
    }
}