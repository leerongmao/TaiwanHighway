package com.mufeng.taiwanhighway

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.mufeng.taiwanhighway.databinding.ActivityCameraBinding

class CameraActivity : AppCompatActivity() {

    private lateinit var binding:ActivityCameraBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        binding= ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        runOnUiThread {
            binding.tv.text="國道１號"
            binding.image.setImageResource(R.drawable.twhw1)
            binding.webView.webViewClient = WebViewClient()
            binding.webView.settings.setSupportZoom(true)
            binding.webView.settings.builtInZoomControls = true
            binding.webView.settings.javaScriptEnabled = true
            binding.webView.loadUrl("https://cctvn.freeway.gov.tw/abs2mjpg/bmjpg?camera=19930")

        }


    }
}