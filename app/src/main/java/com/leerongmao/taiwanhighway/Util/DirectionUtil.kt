package com.leerongmao.taiwanhighway.Util

object DirectionUtil {
    private val direction = listOf(
        "雙向", "北上", "南下"
    )

    fun getAllRegionName():List<Any>{
        return direction
    }

    fun getRegionIndexByName(regionName:String): Int{
        return direction.indexOf(regionName)
    }
}