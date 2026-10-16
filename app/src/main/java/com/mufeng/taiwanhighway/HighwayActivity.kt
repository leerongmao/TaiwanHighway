package com.mufeng.taiwanhighway

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mufeng.taiwanhighway.Util.DirectionEWUtil
import com.mufeng.taiwanhighway.Util.DirectionNSUtil
import com.mufeng.taiwanhighway.Util.OkHttpUtil
import com.mufeng.taiwanhighway.Util.RegionUtil
import com.google.gson.Gson
import com.mufeng.taiwanhighway.data.TrafficInfo
import com.mufeng.taiwanhighway.data.TrafficInfoItem
import com.mufeng.taiwanhighway.databinding.ActivityHighwayBinding
import okhttp3.Response

import com.mufeng.taiwanhighway.data.TrafficResponse
import android.widget.Toast

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

        getTrafficInfo()
        getImage()

        binding.btnBack.setOnClickListener {
            finish()
        }

    }

    private var isLoadingTraffic = false

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
    }

    private fun setDefaultRegion() {
        binding.spinner.setSelection(RegionUtil.getRegionIndexByName(currentDirection))

    }

//    private fun getTrafficInfo() {
//        val road2 =intent.getStringExtra("road2")
//
//        OkHttpUtil.mOkHttpUtil.getAsync(TRAFFIC_DATA_URL, object : OkHttpUtil.ICallback {
//            override fun onResponse(response: Response) {
//                val trafficData = response.body?.string()
//
////                trafficInfo = Gson().fromJson(trafficData, TrafficInfo::class.java)
//                trafficInfo = Gson().fromJson(
//                    trafficData,
//                    TrafficResponse::class.java
//                ).result
//
//                runOnUiThread{
//                    //將 Okhttp 獲取到的回應值，指定到畫面的 TextView 元件中
//
////                    viewAdapter.trafficList =trafficInfo
//                    updateRecycleView()
//                    binding.tv.text=road2
//                    binding.progressBar.visibility = View.GONE
//
//                }
//            }
//
//            override fun onFailure(e: okio.IOException) {
//                Log.d("Jia", "onFailure: $e")
//                binding.progressBar.visibility = View.GONE
//            }
//        })
//    }

    private fun getTrafficInfo() {
        val road2 = intent.getStringExtra("road2")

        if (isLoadingTraffic) return

        isLoadingTraffic = true

        if (!binding.swipeRefresh.isRefreshing) {
            binding.progressBar.visibility = View.VISIBLE
        }

        binding.progressBar.visibility = View.VISIBLE

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
                            binding.tv.text = road2
                            updateRecycleView()
//                            binding.progressBar.visibility = View.GONE
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

        binding.progressBar.visibility = View.GONE
        binding.swipeRefresh.isRefreshing = false
    }

    private fun showTrafficError() {
        finishTrafficLoading()

        if (isFinishing || isDestroyed) return

        Toast.makeText(
            this,
            "路況載入失敗，請檢查網路後重試",
            Toast.LENGTH_LONG
        ).show()
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

//    private fun updateRecycleView() {
//        val road= intent.getStringExtra("road")
//        var filterData = trafficInfo?.filter { it.areaNm == road && it.direction== currentDirection }
//
//        if (filterData!=null){
//            viewAdapter.trafficList = filterData
//        }
//    }

    private fun updateRecycleView() {
        val road = intent.getStringExtra("road")

        val filterData = trafficInfo.orEmpty().filter {
            it.areaNm == road &&
                    it.direction == currentDirection
        }

        viewAdapter.trafficList = filterData

        if (filterData.isEmpty()) {
            // 沒有符合條件的事件
            binding.recycleView.visibility = View.GONE
            binding.tvEmpty.visibility = View.VISIBLE
        } else {
            // 有符合條件的事件
            binding.recycleView.visibility = View.VISIBLE
            binding.tvEmpty.visibility = View.GONE
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