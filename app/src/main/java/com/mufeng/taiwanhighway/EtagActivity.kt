package com.mufeng.taiwanhighway

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.mufeng.taiwanhighway.databinding.ActivityEtagBinding

class EtagActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEtagBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_etag)
        binding= ActivityEtagBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        runOnUiThread {
            binding.webView.webViewClient = WebViewClient()
            binding.webView.settings.setSupportZoom(true)
            binding.webView.settings.builtInZoomControls = true
            binding.webView.settings.javaScriptEnabled = true
            binding.webView.loadUrl(ETAG_URL)
        }
    }
}