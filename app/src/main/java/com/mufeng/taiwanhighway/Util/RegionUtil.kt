package com.mufeng.taiwanhighway.Util

object RegionUtil {
    private val regions = listOf(
        "全台","北部","中部","南部","東部",
//        "北部","A","N","M","S","E"
    )

    fun getAllRegionName():List<Any>{
        return regions
    }

    fun getRegionIndexByName(regionName:String): Int{
        return regions.indexOf(regionName)
    }
}