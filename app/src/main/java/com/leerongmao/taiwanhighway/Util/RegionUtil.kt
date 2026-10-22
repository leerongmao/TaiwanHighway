package com.leerongmao.taiwanhighway.Util

object RegionUtil {

    private val regions = listOf(
        "全台", "北部", "中部", "南部", "東部"
    )

    fun getAllRegionName(): List<String> {
        return regions
    }

    fun getRegionIndexByName(regionName: String): Int {
        return regions.indexOf(regionName)
    }

    // 將畫面上的中文區域轉換成警廣 API 的代碼
    // null 代表全台，不限制 region
    fun getRegionCodeByName(regionName: String): String? {
        return when (regionName) {
            "北部" -> "N"
            "中部" -> "M"
            "南部" -> "S"
            "東部" -> "E"
            else -> null
        }
    }
}