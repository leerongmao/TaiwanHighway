package com.leerongmao.taiwanhighway

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.leerongmao.taiwanhighway.databinding.ActivityCameraBinding

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



        }


    }



}