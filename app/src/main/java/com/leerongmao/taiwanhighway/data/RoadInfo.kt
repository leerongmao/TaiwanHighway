package com.leerongmao.taiwanhighway.data

enum class RoadCategory {
    NATIONAL,
    EXPRESSWAY,
    PROVINCIAL
}

enum class RoadDirectionType {
    NORTH_SOUTH,
    EAST_WEST
}

data class RoadInfo(
    val name: String,
    val category: RoadCategory,
    val directionType: RoadDirectionType,
    val apiNames: List<String>,
    val iconRes: Int? = null
) {
    fun matches(areaNm: String): Boolean {
        return apiNames.contains(areaNm)
    }
}