package com.leerongmao.taiwanhighway.data

import com.leerongmao.taiwanhighway.R

object RoadCatalog {

    private val NS = RoadDirectionType.NORTH_SOUTH
    private val EW = RoadDirectionType.EAST_WEST

    private fun national(
        name: String,
        apiNames: List<String>,
        direction: RoadDirectionType,
        iconRes: Int? = null
    ) = RoadInfo(
        name = name,
        category = RoadCategory.NATIONAL,
        directionType = direction,
        apiNames = apiNames,
        iconRes = iconRes
    )

    private fun expressway(
        name: String,
        apiName: String,
        direction: RoadDirectionType,
        iconRes: Int
    ) = RoadInfo(
        name = name,
        category = RoadCategory.EXPRESSWAY,
        directionType = direction,
        apiNames = listOf(apiName),
        iconRes = iconRes
    )

    val nationalBranches = listOf(
        national(
            name = "國道2甲",
            apiNames = emptyList(),
            direction = NS
        ),
        national(
            name = "國道3甲",
            apiNames = emptyList(),
            direction = EW
        )
    )

    val expressways = listOf(
        expressway("台61線", "(西濱快速)-台61線", NS, R.drawable.ic_expressway_61),
        expressway("台62線", "(萬里瑞濱)-台62線", EW, R.drawable.ic_expressway_62),
        expressway("台63線", "(中投公路)-台63線", NS, R.drawable.ic_expressway_63),
        expressway("台64線", "(八里新店)-台64線", EW, R.drawable.ic_expressway_64),
        expressway("台65線", "(特2號道路)-台65線", NS, R.drawable.ic_expressway_65),
        expressway("台66線", "(觀音大溪)-台66線", EW, R.drawable.ic_expressway_66),
        expressway("台68線", "(南寮竹東)-台68線", EW, R.drawable.ic_expressway_68),
        expressway("台72線", "(後龍汶水)-台72線", EW, R.drawable.ic_expressway_72),
        expressway("台74線", "快官霧峰-台74線", EW, R.drawable.ic_expressway_74),
        expressway("台74甲線", "快官花壇-台74甲", NS, R.drawable.ic_expressway_74a),
        expressway("台76線", "(漢寶草屯)-台76線", EW, R.drawable.ic_expressway_76),
        expressway("台78線", "(台西古坑)-台78線", EW, R.drawable.ic_expressway_78),
        expressway("台82線", "(東石嘉義)-台82線", EW, R.drawable.ic_expressway_82),
        expressway("台84線", "(北門玉井)-台84線", EW, R.drawable.ic_expressway_84),
        expressway("台86線", "(台南關廟)-台86線", EW, R.drawable.ic_expressway_86),
        expressway("台88線", "(高雄潮州)-台88線", EW, R.drawable.ic_expressway_88)
    )

    fun findByName(name: String): RoadInfo? {
        return expressways.find { it.name == name }
    }

    private val provincialRoadPattern =
        Regex("""台[０-９0-9]+[甲乙丙丁]?""")

    fun isProvincialRoad(areaNm: String): Boolean {
        if (expressways.any { it.matches(areaNm) }) {
            return false
        }

        if (areaNm.contains("國道")) {
            return false
        }

        return provincialRoadPattern.containsMatchIn(areaNm)
    }
}
