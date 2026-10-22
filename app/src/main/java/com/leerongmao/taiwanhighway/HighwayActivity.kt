package com.leerongmao.taiwanhighway

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leerongmao.taiwanhighway.Util.DirectionEWUtil
import com.leerongmao.taiwanhighway.Util.DirectionNSUtil
import com.leerongmao.taiwanhighway.Util.OkHttpUtil
import com.google.gson.Gson
import com.leerongmao.taiwanhighway.data.TrafficInfo
import com.leerongmao.taiwanhighway.data.TrafficInfoItem
import com.leerongmao.taiwanhighway.databinding.ActivityHighwayBinding
import okhttp3.Response

import com.leerongmao.taiwanhighway.data.TrafficResponse
import android.widget.Toast
import com.leerongmao.taiwanhighway.Util.RegionUtil

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private lateinit var binding: ActivityHighwayBinding
private lateinit var viewAdapter: HighwayAdapter
private lateinit var viewManager: RecyclerView.LayoutManager



private var trafficInfo: TrafficInfo? = null
private var currentDirection: String = "雙向"

class HighwayActivity : AppCompatActivity() , HighwayAdapter.IItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_highway)

        binding = ActivityHighwayBinding.inflate(layoutInflater)
        setContentView(binding.root)



        initView()

// 避免列表還沒滑到最頂端就觸發重新整理
        binding.swipeRefresh.setOnChildScrollUpCallback { _, _ ->
            binding.recycleView.canScrollVertically(-1)
        }

// 使用者向下拉動時，重新取得 API 資料
        binding.swipeRefresh.setOnRefreshListener {
            getTrafficInfo()
        }
        // 點擊重新載入按鈕
        binding.btnRetry.setOnClickListener {
            getTrafficInfo()
        }

        // 即使 API 失敗，也要正常顯示國道名稱
        binding.tv.text = intent.getStringExtra("road2") ?: "國道路況"

        getTrafficInfo()
        getImage()

        binding.btnBack.setOnClickListener {
            finish()
        }

    }

    private var isLoadingTraffic = false
    private var currentRegion = "全台"

    private enum class TrafficUiState {
        LOADING,
        SUCCESS,
        EMPTY,
        ERROR
    }

    private fun showTrafficState(state: TrafficUiState) {
        binding.progressBar.visibility =
            if (state == TrafficUiState.LOADING) View.VISIBLE else View.GONE

        binding.recycleView.visibility =
            if (state == TrafficUiState.SUCCESS) View.VISIBLE else View.GONE

        binding.tvEmpty.visibility =
            if (state == TrafficUiState.EMPTY) View.VISIBLE else View.GONE

        binding.errorState.visibility =
            if (state == TrafficUiState.ERROR) View.VISIBLE else View.GONE
    }

    private fun initView() {

        val road2 =intent.getStringExtra("road2")
        if(road2=="國道１號"||road2=="國道３號"||road2=="國道５號") {
            currentDirection ="北上"
            val adapterDirection = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, DirectionNSUtil.getAllRegionName())
            binding.spinner.adapter=adapterDirection
        }else if (road2=="國道２號"||road2=="國道４號"||road2=="國道６號"||road2=="國道８號"||road2=="國道10號"){
            currentDirection ="東向"
            val adapterDirection = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, DirectionEWUtil.getAllRegionName())
            binding.spinner.adapter=adapterDirection
        }

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                currentDirection = binding.spinner.selectedItem.toString()

                if(trafficInfo !=null) {
                    updateRecycleView()
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        setDefaultRegion()

        // 定義 LayoutManager 為 LinearLayoutManager
        viewManager = LinearLayoutManager(this)

        // 自定義 Adapte 為 MainAdapter，稍後再定義 MainAdapter 這個類別
        viewAdapter = HighwayAdapter(this)

        // 定義從佈局當中，拿到 recycler_view 元件，
        binding.recycleView.apply {
            // 透過 kotlin 的 apply 語法糖，設定 LayoutManager 和 Adapter
            layoutManager = viewManager
            adapter = viewAdapter
        }

        // Beta 3.1.2：初始化路況區域選單
        val regionAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            RegionUtil.getAllRegionName()
        )

        regionAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spinnerRegion.adapter = regionAdapter
        binding.spinnerRegion.setSelection(0)

        // 切換路況區域時，重新篩選現有資料
        binding.spinnerRegion.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    currentRegion = binding.spinnerRegion.selectedItem.toString()

                    if (trafficInfo != null) {
                        updateRecycleView()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

    }

    private fun setDefaultRegion() {
        binding.spinner.setSelection(0)
    }

    private fun getTrafficInfo() {
        val road2 = intent.getStringExtra("road2")

        if (isLoadingTraffic) return

        isLoadingTraffic = true

        if (trafficInfo == null) {
            showTrafficState(TrafficUiState.LOADING)
        }

        OkHttpUtil.mOkHttpUtil.getAsync(
            TRAFFIC_DATA_URL,
            object : OkHttpUtil.ICallback {

                override fun onResponse(response: Response) {
                    try {
                        // 1. 檢查 HTTP 狀態，並讀取資料
                        val json = response.use {
                            if (!it.isSuccessful) {
                                error("HTTP ${it.code}")
                            }

                            it.body?.string()
                                ?: error("API 回傳空內容")
                        }

                        // 2. 解析新版 JSON
                        val newInfo = Gson().fromJson(
                            json,
                            TrafficResponse::class.java
                        )?.result ?: error("JSON 缺少 result")

                        // 3. 回到主執行緒更新畫面
                        runOnUiThread {
                            if (isFinishing || isDestroyed) {
                                return@runOnUiThread
                            }

                            trafficInfo = newInfo
                            updateLastSuccessTime()
                            updateRecycleView()
                            finishTrafficLoading()
                        }

                    } catch (e: Exception) {
                        Log.e("Highway", "路況解析失敗", e)

                        runOnUiThread {
                            showTrafficError()
                        }
                    }
                }

                override fun onFailure(e: okio.IOException) {
                    Log.e("Highway", "網路連線失敗", e)

                    runOnUiThread {
                        showTrafficError()
                    }
                }
            }
        )
    }

    private fun finishTrafficLoading() {
        isLoadingTraffic = false

        if (isFinishing || isDestroyed) return

        binding.swipeRefresh.isRefreshing = false
    }

    private fun updateLastSuccessTime() {
        val formatter = SimpleDateFormat(
            "MM/dd HH:mm:ss",
            Locale.getDefault()
        )

        val time = formatter.format(Date())

        binding.tvLastUpdated.text = "最後成功取得：$time"
        binding.tvLastUpdated.visibility = View.VISIBLE
    }

    private fun showTrafficError() {
        finishTrafficLoading()

        if (isFinishing || isDestroyed) return

        if (trafficInfo == null) {
            showTrafficState(TrafficUiState.ERROR)
        } else {
            Toast.makeText(
                this,
                "路況更新失敗，顯示上次取得的資料",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun getImage(){
        val road2 =intent.getStringExtra("road2")
        if (road2=="國道１號"){
            binding.image.setImageResource(R.drawable.twhw1)
        }else if(road2=="國道２號"){
            binding.image.setImageResource(R.drawable.twhw2)
        }else if(road2=="國道３號"){
            binding.image.setImageResource(R.drawable.twhw3)
        }else if(road2=="國道3號甲線"){
            binding.image.setImageResource(R.drawable.twhw3a)
        }else if(road2=="國道４號"){
            binding.image.setImageResource(R.drawable.twhw4)
        }else if(road2=="國道５號"){
            binding.image.setImageResource(R.drawable.twhw5)
        }else if(road2=="國道６號"){
            binding.image.setImageResource(R.drawable.twhw6)
        }else if(road2=="國道８號"){
            binding.image.setImageResource(R.drawable.twhw8)
        }else if(road2=="國道10號"){
            binding.image.setImageResource(R.drawable.twhw10)
        }

    }

    private fun updateRecycleView() {
        val road = intent.getStringExtra("road")

        // 將中文地區轉成 API 代碼
        // 全台會回傳 null，代表不限制區域
        val regionCode = RegionUtil.getRegionCodeByName(currentRegion)

        // 三條件篩選：國道 + 方向 + 區域
        val filterData = trafficInfo.orEmpty().filter {
            it.areaNm == road &&
                    it.direction == currentDirection &&
                    (regionCode == null || it.region == regionCode)
        }

        viewAdapter.trafficList = filterData

        if (filterData.isEmpty()) {
            showTrafficState(TrafficUiState.EMPTY)
        } else {
            showTrafficState(TrafficUiState.SUCCESS)
        }
    }


    override fun onItemClickListener(data: TrafficInfoItem) {
        val road =intent.getStringExtra("road2")
        val intent = Intent(this, TrafficDetailActivity::class.java)
        intent.putExtra("data",data)
        intent.putExtra("road2",road)
        startActivity(intent)
    }

}