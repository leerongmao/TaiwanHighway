
package com.leerongmao.taiwanhighway.data

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
        direction: RoadDirectionType
    ) = RoadInfo(
        name = name,
        category = RoadCategory.EXPRESSWAY,
        directionType = direction,
        apiNames = listOf(apiName)
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

        expressway(
            "台61線", "(西濱快速)-台61線", NS
        ),
        expressway(
            "台62線", "(萬里瑞濱)-台62線", EW
        ),
        expressway(
            "台63線", "(中投公路)-台63線", NS
        ),
        expressway(
            "台64線", "(八里新店)-台64線", EW
        ),
        expressway(
            "台65線", "(特2號道路)-台65線", NS
        ),
        expressway(
            "台66線", "(觀音大溪)-台66線", EW
        ),
        expressway(
            "台68線", "(南寮竹東)-台68線", EW
        ),
        expressway(
            "台72線", "(後龍汶水)-台72線", EW
        ),
        expressway(
            "台74線", "快官霧峰-台74線", EW
        ),
        expressway(
            "台74甲線", "快官花壇-台74甲", NS
        ),
        expressway(
            "台76線", "(漢寶草屯)-台76線", EW
        ),
        expressway(
            "台78線", "(台西古坑)-台78線", EW
        ),
        expressway(
            "台82線", "(東石嘉義)-台82線", EW
        ),
        expressway(
            "台84線", "(北門玉井)-台84線", EW
        ),
        expressway(
            "台86線", "(台南關廟)-台86線", EW
        ),
        expressway(
            "台88線", "(高雄潮州)-台88線", EW
        )
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
