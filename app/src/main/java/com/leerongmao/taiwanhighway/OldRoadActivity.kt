package com.leerongmao.taiwanhighway

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.highway_finger.OldRoadAdapter
import com.leerongmao.taiwanhighway.Util.RegionUtil
import com.google.gson.Gson
import com.leerongmao.taiwanhighway.Util.OkHttpUtil
import com.leerongmao.taiwanhighway.data.TrafficInfo
import com.leerongmao.taiwanhighway.data.TrafficInfoItem
import com.leerongmao.taiwanhighway.databinding.ActivityOldRoadBinding
import okhttp3.Response

private lateinit var binding: ActivityOldRoadBinding
private lateinit var viewManager: RecyclerView.LayoutManager
private lateinit var viewAdapter: OldRoadAdapter

private var currentRegion: String = "北部"

private var trafficInfo: TrafficInfo? = null

class OldRoadActivity : AppCompatActivity() , OldRoadAdapter.IItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_old_road)

        binding = ActivityOldRoadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        getTrafficInfo()

    }

    private fun initView() {

        val adapterRegion = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, RegionUtil.getAllRegionName())
        binding.spinnerRegion.adapter=adapterRegion
        binding.spinnerRegion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                currentRegion = binding.spinnerRegion.selectedItem.toString()
                if(currentRegion =="北部"){
                    currentRegion ="N"
                }else if(currentRegion =="中部"){
                    currentRegion ="M"
                }else if(currentRegion =="南部"){
                    currentRegion ="S"
                }else if(currentRegion =="東部"){
                    currentRegion ="E"
                }else {
                    currentRegion ="A"
                }
                if(trafficInfo !=null) {
                    updateRecycleView()
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        setDefaultRegion()

        viewManager = LinearLayoutManager(this)
        viewAdapter = OldRoadAdapter(this)

        binding.recycleView.apply {
            layoutManager = viewManager
            adapter = viewAdapter

//            addItemDecoration(DividerItemDecoration(this@MainActivity,DividerItemDecoration.VERTICAL))
        }
    }

    private fun setDefaultRegion() {
        binding.spinnerRegion.setSelection(RegionUtil.getRegionIndexByName(currentRegion))

    }

//    private fun setSpinnerRegion() {
//
//    }

    private fun getTrafficInfo() {

        OkHttpUtil.mOkHttpUtil.getAsync(TRAFFIC_DATA_URL, object : OkHttpUtil.ICallback {
            override fun onResponse(response: Response) {
                val trafficData = response.body?.string()

                trafficInfo = Gson().fromJson(trafficData, TrafficInfo::class.java)






                runOnUiThread{
                    //將 Okhttp 獲取到的回應值，指定到畫面的 TextView 元件中

//                    binding.tv.text= trafficData
//                    viewAdapter.trafficList =trafficInfo
                    updateRecycleView()

                    binding.progressBar.visibility = View.GONE

                }
            }

            override fun onFailure(e: okio.IOException) {
                Log.d("HKT", "onFailure: $e")
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun updateRecycleView() {
        var filterData = trafficInfo?.filter { it.region == currentRegion }

        if (filterData!=null){
            viewAdapter.trafficList = filterData
        }
    }

    override fun onItemClickListener(data: TrafficInfoItem) {
        val intent = Intent(this, TrafficDetailActivity::class.java)
        intent.putExtra("data",data)
        startActivity(intent)
    }
}