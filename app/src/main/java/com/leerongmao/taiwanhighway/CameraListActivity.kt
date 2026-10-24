package com.leerongmao.taiwanhighway

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.leerongmao.taiwanhighway.data.CameraXmlParser
import com.leerongmao.taiwanhighway.databinding.ActivityCameraListBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import android.content.Intent
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.leerongmao.taiwanhighway.data.CameraInfo
import android.text.Editable
import android.text.TextWatcher

class CameraListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraListBinding
    private lateinit var cameraAdapter: CameraAdapter

    private var allCameras: List<CameraInfo> = emptyList()

    private val client = OkHttpClient()

    companion object {
        private const val CCTV_XML_URL =
            "https://tisvcloud.freeway.gov.tw/history/motc20/CCTV.xml"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSearch()
        loadCameras()

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }


    private fun setupSearch() {
        binding.etSearchMile.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    filterCameras()
                }

                override fun afterTextChanged(s: Editable?) {}
            }
        )
    }


    private fun setupRecyclerView() {
        cameraAdapter = CameraAdapter { camera ->
            val intent = Intent(this, CameraActivity::class.java).apply {
                putExtra("camera_url", camera.videoStreamUrl)
                putExtra("camera_road", camera.roadName)
                putExtra("camera_direction", camera.direction)
                putExtra("camera_mile", camera.locationMile)
            }

            startActivity(intent)
        }

        binding.recyclerViewCamera.apply {
            layoutManager = LinearLayoutManager(this@CameraListActivity)
            adapter = cameraAdapter
            setHasFixedSize(true)
        }
    }

    private fun loadCameras() {
        showLoading()

        val request = Request.Builder()
            .url(CCTV_XML_URL)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                if (isFinishing || isDestroyed) return

                runOnUiThread {
                    if (isFinishing || isDestroyed) return@runOnUiThread
                    showMessage("攝影機資料載入失敗，請檢查網路連線")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val xml = response.use {
                        if (!it.isSuccessful) {
                            throw IOException("HTTP ${it.code}")
                        }
                        it.body?.string()
                            ?: throw IOException("回應內容為空")
                    }

                    val cameras = CameraXmlParser.parse(xml)

                    runOnUiThread {
                        if (isFinishing || isDestroyed) return@runOnUiThread

                        binding.progressBar.visibility = View.GONE

                        if (cameras.isEmpty()) {
                            showMessage("目前沒有攝影機資料")
                        } else {
                            allCameras = cameras
                            binding.tvMessage.visibility = View.GONE
                            setupRoadSpinner()
                            setupDirectionSpinner()
                            filterCameras()
                        }
                    }

                } catch (e: Exception) {
                    android.util.Log.e(
                        "CameraList",
                        "攝影機清單載入失敗",
                        e
                    )

                    runOnUiThread {
                        if (isFinishing || isDestroyed) return@runOnUiThread
                        showMessage("攝影機資料讀取失敗，請稍後再試")
                    }
                }
            }
        })
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvMessage.visibility = View.GONE
        binding.tvCount.text = "正在載入攝影機資料..."
    }

    private fun showMessage(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.tvMessage.visibility = View.VISIBLE
        binding.tvMessage.text = message
        binding.tvCount.text = "無法顯示攝影機清單"
    }

    override fun onDestroy() {
        client.dispatcher.cancelAll()
        super.onDestroy()
    }


    private fun setupRoadSpinner() {

        val roadNames = allCameras
            .map { it.roadName }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()

        val options = listOf("全部道路") + roadNames

        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            options
        )

        spinnerAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spinnerRoad.adapter = spinnerAdapter

        binding.spinnerRoad.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    setupDirectionSpinner()
                    filterCameras()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // 不需要額外處理
                }
            }
    }


    private fun setupDirectionSpinner() {

        val selectedRoad =
            binding.spinnerRoad.selectedItem?.toString() ?: "全部道路"

        val availableCameras = if (selectedRoad == "全部道路") {
            allCameras
        } else {
            allCameras.filter { it.roadName == selectedRoad }
        }

        val directionNames = mapOf(
            "N" to "北向",
            "S" to "南向",
            "E" to "東向",
            "W" to "西向"
        )

        val directions = availableCameras
            .map { it.direction }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()

        val options = listOf("全部方向") +
                directions.map { directionNames[it] ?: it }

        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            options
        )

        spinnerAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spinnerDirection.adapter = spinnerAdapter

        binding.spinnerDirection.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    filterCameras()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }



    private fun filterCameras() {

        val selectedRoad =
            binding.spinnerRoad.selectedItem?.toString() ?: "全部道路"

        val selectedDirection =
            binding.spinnerDirection.selectedItem?.toString() ?: "全部方向"

        val directionCodes = mapOf(
            "北向" to "N",
            "南向" to "S",
            "東向" to "E",
            "西向" to "W"
        )

        val directionCode =
            directionCodes[selectedDirection] ?: selectedDirection

        val keyword = binding.etSearchMile.text
            .toString()
            .trim()
            .replace(" ", "")
            .uppercase()

        val filtered = allCameras.filter { camera ->

            val roadMatches =
                selectedRoad == "全部道路" ||
                        camera.roadName == selectedRoad

            val directionMatches =
                selectedDirection == "全部方向" ||
                        camera.direction == directionCode

            val mileMatches = keyword.isEmpty() ||
                    camera.locationMile
                        .replace(" ", "")
                        .uppercase()
                        .contains(keyword)

            roadMatches && directionMatches && mileMatches
        }

        cameraAdapter.updateData(filtered)

        binding.tvCount.text = "共 ${filtered.size} 支攝影機"

        binding.tvMessage.visibility =
            if (filtered.isEmpty()) View.VISIBLE else View.GONE

        if (filtered.isEmpty()) {
            binding.tvMessage.text = "目前沒有符合條件的攝影機"
        }

        if (filtered.isNotEmpty()) {
            binding.recyclerViewCamera.scrollToPosition(0)
        }
    }



}
