package com.leerongmao.taiwanhighway.data

import com.google.gson.annotations.SerializedName

data class OilPriceInfo(
    @SerializedName("型別名稱")
    val type: String,

    @SerializedName("產品名稱")
    val productName: String,

    @SerializedName("參考牌價_金額")
    val price: Double,

    @SerializedName("牌價生效日期")
    val effectiveDate: String,

    @SerializedName("計價單位")
    val unit: String
)