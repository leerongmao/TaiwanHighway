package com.leerongmao.taiwanhighway.Util

object DirectionNSUtil {
    private val direction = listOf(
         "北上", "南下","雙向"
    )

    fun getAllRegionName():List<Any>{
        return direction
    }

    fun getRegionIndexByName(regionName:String): Int{
        return direction.indexOf(regionName)
    }
}