package com.leerongmao.taiwanhighway.data

data class CameraInfo(
    val id: String,
    val roadName: String,
    val direction: String,
    val locationMile: String,
    val videoStreamUrl: String,
    val latitude: Double?,
    val longitude: Double?
)