package com.leerongmao.taiwanhighway

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leerongmao.taiwanhighway.data.TrafficInfoItem
import com.leerongmao.taiwanhighway.databinding.HighwayViewBinding

class HighwayAdapter(private val itemClickListener: IItemClickListener): RecyclerView.Adapter<HighwayAdapter.MyViewHolder>() {
    class MyViewHolder(val highwayViewBinding: HighwayViewBinding) :
        RecyclerView.ViewHolder(highwayViewBinding.root)


    var trafficList: List<TrafficInfoItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val highwayViewBinding =
            HighwayViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(highwayViewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.highwayViewBinding.tvComment.text=trafficList[position].comment
        holder.highwayViewBinding.tvType.text="["+trafficList[position].roadtype+"]"
        holder.highwayViewBinding.tvDirection.text=trafficList[position].direction

        holder.highwayViewBinding.layoutItem.setOnClickListener {
            itemClickListener.onItemClickListener(trafficList[position])
        }
    }



    override fun getItemCount(): Int {
        return trafficList.size
    }

    interface IItemClickListener{
        fun onItemClickListener(data : TrafficInfoItem)
    }

}