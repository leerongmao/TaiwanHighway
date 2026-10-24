package com.leerongmao.taiwanhighway

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leerongmao.taiwanhighway.data.CameraInfo
import com.leerongmao.taiwanhighway.databinding.ItemCameraBinding

class CameraAdapter(
    private val onCameraClick: (CameraInfo) -> Unit
) : RecyclerView.Adapter<CameraAdapter.CameraViewHolder>() {

    private val cameras = mutableListOf<CameraInfo>()

    inner class CameraViewHolder(
        val binding: ItemCameraBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CameraViewHolder {
        val binding = ItemCameraBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CameraViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CameraViewHolder,
        position: Int
    ) {
        val camera = cameras[position]

        with(holder.binding) {
            tvRoadName.text = camera.roadName
            tvMile.text = camera.locationMile

            tvDirection.text = when (camera.direction) {
                "N" -> "北向"
                "S" -> "南向"
                "E" -> "東向"
                "W" -> "西向"
                else -> camera.direction
            }

            root.setOnClickListener {
                onCameraClick(camera)
            }
        }
    }

    override fun getItemCount(): Int = cameras.size

    fun updateData(newCameras: List<CameraInfo>) {
        cameras.clear()
        cameras.addAll(newCameras)
        notifyDataSetChanged()
    }
}
