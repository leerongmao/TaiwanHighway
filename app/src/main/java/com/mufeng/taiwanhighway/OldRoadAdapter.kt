package com.example.highway_finger

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.mufeng.taiwanhighway.data.TrafficInfoItem
import com.mufeng.taiwanhighway.databinding.ItemViewBinding


class OldRoadAdapter (private val itemClickListener:IItemClickListener):RecyclerView.Adapter<OldRoadAdapter.MyViewHolder>(){

    var trafficList :List<TrafficInfoItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class MyViewHolder(val itemViewBinding: ItemViewBinding):RecyclerView.ViewHolder(itemViewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemViewBinding = ItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return MyViewHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (trafficList[position].areaNm==""){
            holder.itemViewBinding.tvName.text = trafficList[position].road
        }else if(trafficList[position].road==""){
            holder.itemViewBinding.tvName.text = trafficList[position].areaNm
        }else if (trafficList[position].areaNm=="" && trafficList[position].road==""){
            holder.itemViewBinding.tvName.text = trafficList[position].comment
        }

        if (trafficList[position].region=="A"){
            holder.itemViewBinding.tvRegionInfo.text = "全台"
        }else if (trafficList[position].region=="N"){
            holder.itemViewBinding.tvRegionInfo.text = "北部"
        }else if (trafficList[position].region=="M"){
            holder.itemViewBinding.tvRegionInfo.text = "中部"
        }else if (trafficList[position].region=="S"){
            holder.itemViewBinding.tvRegionInfo.text = "南部"
        }else if (trafficList[position].region=="E"){
            holder.itemViewBinding.tvRegionInfo.text = "東部"
        }


        holder.itemViewBinding.layoutItem.setOnClickListener {
            itemClickListener.onItemClickListener(trafficList[position])
        }
    }

    override fun getItemCount(): Int {
        return  trafficList.size
    }

    interface IItemClickListener{
        fun onItemClickListener(data : TrafficInfoItem)
    }

}
