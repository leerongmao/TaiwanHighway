package com.mufeng.taiwanhighway.Util

object DirectionEWUtil {
    private val direction = listOf(
         "東向", "西行","雙向"
    )

    fun getAllRegionName():List<Any>{
        return direction
    }

    fun getRegionIndexByName(regionName:String): Int{
        return direction.indexOf(regionName)
    }
}