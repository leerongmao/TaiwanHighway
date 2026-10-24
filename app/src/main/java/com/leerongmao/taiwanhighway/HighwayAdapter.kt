package com.leerongmao.taiwanhighway

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leerongmao.taiwanhighway.data.TrafficInfoItem
import com.leerongmao.taiwanhighway.databinding.HighwayViewBinding
import android.view.View
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoField

class HighwayAdapter(
    private val itemClickListener: IItemClickListener,
    private val isProvincial: Boolean = false
) : RecyclerView.Adapter<HighwayAdapter.MyViewHolder>() {
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

    private val apiTimeFormatter = DateTimeFormatterBuilder()
        .appendPattern("uuuu-MM-dd HH:mm:ss")
        .optionalStart()
        .appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, true)
        .optionalEnd()
        .toFormatter()

    private val displayTimeFormatter =
        DateTimeFormatter.ofPattern("MM/dd HH:mm")

    private fun formatReportTime(rawTime: String?): String? {
        if (rawTime.isNullOrBlank()) return null

        return try {
            val dateTime = LocalDateTime.parse(
                rawTime.trim(),
                apiTimeFormatter
            )

            dateTime.format(displayTimeFormatter)

        } catch (e: DateTimeParseException) {
            null
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = trafficList[position]

        with(holder.highwayViewBinding) {
            if (isProvincial) {
                tvRoadName.text = item.areaNm
                tvRoadName.visibility = View.VISIBLE
            } else {
                tvRoadName.text = ""
                tvRoadName.visibility = View.GONE
            }
            tvType.text = "[${item.roadtype}]"
            tvComment.text = item.comment

            // Beta 3.2.3：顯示路況最後修改時間
            val updatedTime = formatReportTime(item.modDttm)

            if (updatedTime != null) {
                tvReportUpdated.text = "通報更新：$updatedTime"
                tvReportUpdated.visibility = View.VISIBLE
            } else {
                tvReportUpdated.text = ""
                tvReportUpdated.visibility = View.GONE
            }

            layoutItem.setOnClickListener {
                itemClickListener.onItemClickListener(item)
            }
        }
    }



    override fun getItemCount(): Int {
        return trafficList.size
    }

    interface IItemClickListener{
        fun onItemClickListener(data : TrafficInfoItem)
    }

}