package com.leerongmao.taiwanhighway

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.leerongmao.taiwanhighway.data.TrafficInfoItem
import com.leerongmao.taiwanhighway.databinding.ActivityTrafficDetailBinding


class TrafficDetailActivity : AppCompatActivity() {
    private val data by lazy { intent.getSerializableExtra("data") as TrafficInfoItem }

    private val areaNm by lazy { data?.areaNm }
    private val road by lazy { data?.road }
    private val comment by lazy { data?.comment }
    private val region by lazy { data?.region }
    private val roadtype by lazy { data?.roadtype }
    private val direction by lazy { data?.direction }
    private val happenedtime by lazy { data?.happentime }
    private val happeneddate by lazy { data?.happendate }

    private lateinit var binding: ActivityTrafficDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_traffic_detail)



        binding= ActivityTrafficDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        getImage()

        binding.btnBack.setOnClickListener {
            finish()
        }


    }

    private fun initView() {
        val road2 =intent.getStringExtra("road2")
        if(areaNm==""){
            binding.tvName.text=road
        }else if(road==""){
            binding.tvName.text=areaNm
        }else if(areaNm==""&&road==""){
            binding.tvName.text=comment
        }
        binding.tvName.text=areaNm
        if (region=="A"){
            binding.tvRegionInfo.text= "全台"
        }else if (region=="N"){
            binding.tvRegionInfo.text= "北"
        }else if (region=="M"){
            binding.tvRegionInfo.text= "中"
        }else if (region=="S"){
            binding.tvRegionInfo.text= "南"
        }else if (region=="E"){
            binding.tvRegionInfo.text= "東"
        }

        binding.tvDirection.text=direction
        binding.tvName.text=road2
        binding.tvCommentInfo.text=comment
        binding.tvRoadtypeInfo.text="["+roadtype+"]"
        binding.tvHappenedDate.text=happeneddate
        binding.tvHappenedTime.text=happenedtime.replaceAfter("00.","")
//        binding.tvHappenedTime.text=happenedtime
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

        if(roadtype=="道路施工"){
            binding.image2.setImageResource(R.drawable.construction)
        }else if(roadtype=="事故"){
            binding.image2.setImageResource(R.drawable.accident)
        }else if(roadtype=="交通障礙"){
            binding.image2.setImageResource(R.drawable.obstacle)
        }else{
            binding.image2.setImageResource(R.drawable.others)
        }

    }
}