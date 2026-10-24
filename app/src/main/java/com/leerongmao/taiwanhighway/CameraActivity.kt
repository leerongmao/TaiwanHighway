package com.leerongmao.taiwanhighway

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.leerongmao.taiwanhighway.databinding.ActivityCameraBinding
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import com.leerongmao.taiwanhighway.data.CameraXmlParser

class CameraActivity : AppCompatActivity() {

    private lateinit var binding:ActivityCameraBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        binding= ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        runOnUiThread {
            val roadName = intent.getStringExtra("camera_road") ?: "道路攝影機"
            val direction = when (intent.getStringExtra("camera_direction")) {
                "N" -> "北向"
                "S" -> "南向"
                "E" -> "東向"
                "W" -> "西向"
                else -> ""
            }
            val mile = intent.getStringExtra("camera_mile") ?: ""

            binding.tv.text = "$roadName $direction $mile"

// 暫時隱藏固定的國道1號圖示，避免選擇其他道路時顯示錯誤
            binding.image.visibility = android.view.View.GONE
            binding.webView.webViewClient = WebViewClient()
            binding.webView.settings.setSupportZoom(true)
            binding.webView.settings.builtInZoomControls = true
            binding.webView.settings.javaScriptEnabled = true
            val cameraUrl = intent.getStringExtra("camera_url")
            android.util.Log.d(
                "CameraURL",
                "攝影機網址：$cameraUrl"
            )
            if (!cameraUrl.isNullOrBlank() &&
                (
                        cameraUrl.startsWith("https://cctvn.freeway.gov.tw/") ||
                                cameraUrl.startsWith("https://cctvs.freeway.gov.tw/") ||
                                cameraUrl.startsWith("https://cctvc.freeway.gov.tw/") ||
                                cameraUrl.startsWith("https://cctvn5.freeway.gov.tw/")
                        )
            ) {
                binding.webView.loadUrl(cameraUrl)
            } else {
                android.widget.Toast.makeText(
                    this,
                    "攝影機網址無效",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }

            testCameraXmlDownload()

        }


    }

    private fun testCameraXmlDownload() {
        val url = "https://tisvcloud.freeway.gov.tw/history/motc20/CCTV.xml"

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                Log.e("CameraXML", "下載失敗", e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    Log.d("CameraXML", "HTTP 狀態碼：${it.code}")

                    if (!it.isSuccessful) {
                        Log.e("CameraXML", "伺服器回傳錯誤")
                        return
                    }

                    val xml = it.body?.string()
                    if (!xml.isNullOrBlank()) {

                        try {
                            val cameras = CameraXmlParser.parse(xml)

                            Log.d("CameraXML", "成功解析：${cameras.size} 支攝影機")

                            val roads = cameras
                                .map { camera -> camera.roadName }
                                .distinct()
                                .sorted()

                            Log.d("CameraXML", "道路清單：$roads")

                            val directions = cameras
                                .map { camera -> camera.direction }
                                .distinct()
                                .sorted()

                            Log.d("CameraXML", "方向代碼：$directions")

                            cameras.take(5).forEach { camera ->
                                Log.d(
                                    "CameraXML",
                                    "${camera.roadName} / " +
                                            "${camera.direction} / " +
                                            "${camera.locationMile} / " +
                                            camera.videoStreamUrl
                                )
                            }

                        } catch (e: Exception) {
                            Log.e("CameraXML", "XML 解析失敗", e)
                        }
                    }

                    Log.d("CameraXML", "XML 長度：${xml?.length ?: 0}")
                    Log.d("CameraXML", "XML 開頭：${xml?.take(300)}")
                }
            }
        })
    }

}