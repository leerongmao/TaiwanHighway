package com.leerongmao.taiwanhighway

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.leerongmao.taiwanhighway.databinding.ActivityOilBinding
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.leerongmao.taiwanhighway.Util.OkHttpUtil
import com.leerongmao.taiwanhighway.data.OilPriceInfo
import okhttp3.Response





class OilActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.btnRefresh.setOnClickListener {
            getOilPrices()
        }

        getOilPrices()

    }

    private fun getOilPrices() {

        showLoading()

        OkHttpUtil.mOkHttpUtil.getAsync(
            CPC_OIL_URL,
            object : OkHttpUtil.ICallback {

                override fun onResponse(response: Response) {
                    try {
                        if (!response.isSuccessful) {
                            Log.e("OilPrice", "HTTP Error: ${response.code}")

                            runOnUiThread {
                                showError("取得油價失敗（HTTP ${response.code}）")
                            }
                            return
                        }

                        val json = response.body?.string()
                            ?: throw IllegalStateException("API 回傳空資料")

                        // 將 JSON Array 轉換成 Kotlin List
                        val listType =
                            object : TypeToken<List<OilPriceInfo>>() {}.type

                        val oilPrices: List<OilPriceInfo> =
                            Gson().fromJson(json, listType)

                        // 只保留四種常用汽柴油
                        val targetProducts = setOf(
                            "92無鉛汽油",
                            "95無鉛汽油",
                            "98無鉛汽油",
                            "超級柴油"
                        )

                        val filteredPrices = oilPrices.filter {
                            it.type == "汽柴油零售" &&
                                    it.productName in targetProducts &&
                                    it.unit.replace(" ", "") == "元/公升"
                        }
                        if (filteredPrices.size != 4 ||
                            filteredPrices.map { it.productName }.toSet().size != 4) {
                            throw IllegalStateException("四種油價資料不完整")
                        }

                        // 先印在 Logcat，確認資料正確
                        filteredPrices.forEach {
                            Log.d(
                                "OilPrice",
                                "${it.productName}: ${it.price} 元/公升，生效日期：${it.effectiveDate}"
                            )
                        }

                        Log.d("OilPrice", "成功取得 ${filteredPrices.size} 筆油價")

                        runOnUiThread {
                            updateOilPriceUI(filteredPrices)
                        }

                    } catch (e: Exception) {
                        Log.e("OilPrice", "解析油價失敗", e)
                        runOnUiThread {
                            showError("油價資料解析失敗，請稍後再試")
                        }
                    }
                }

                override fun onFailure(e: okio.IOException) {
                    Log.e("OilPrice", "網路請求失敗", e)
                    runOnUiThread {
                        showError("網路連線失敗，請檢查網路")
                    }
                }
            }
        )
    }

    private fun updateOilPriceUI(prices: List<OilPriceInfo>) {

        // 將四種油品整理成「產品名稱 → 油價」的對應表
        val priceMap = prices.associateBy { it.productName }

        binding.textPrice92.text =
            priceMap["92無鉛汽油"]?.price?.toString() ?: "--.-"

        binding.textPrice95.text =
            priceMap["95無鉛汽油"]?.price?.toString() ?: "--.-"

        binding.textPrice98.text =
            priceMap["98無鉛汽油"]?.price?.toString() ?: "--.-"

        binding.textPriceDiesel.text =
            priceMap["超級柴油"]?.price?.toString() ?: "--.-"

        // 中油日期格式：1151019 → 2026/10/19
        val date = prices.firstOrNull()?.effectiveDate

        val formattedDate = if (date != null &&
            date.length == 7 && date.all { it.isDigit() }) {

            val year = date.substring(0, 3).toInt() + 1911
            val month = date.substring(3, 5)
            val day = date.substring(5, 7)

            "$year/$month/$day"
        } else {
            "未知"
        }

        binding.textEffectiveDate.text =
            "牌價生效日期：$formattedDate"

        binding.textStatus.text =
            "已成功更新 ${prices.size} 筆油價"

        binding.btnRefresh.isEnabled = true
    }


    private fun showLoading() {
        binding.textStatus.text = "正在取得最新油價..."
        binding.btnRefresh.isEnabled = false
    }

    private fun showError(message: String) {
        binding.textStatus.text = message
        binding.btnRefresh.isEnabled = true
    }





}