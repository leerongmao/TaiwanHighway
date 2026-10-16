package com.mufeng.taiwanhighway

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebViewClient
import com.mufeng.taiwanhighway.Util.OkHttpUtil
import com.mufeng.taiwanhighway.databinding.ActivityOilBinding
import okhttp3.Response

private lateinit var binding : ActivityOilBinding

class OilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_oil)

        binding = ActivityOilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        runOnUiThread {
            binding.webView.webViewClient = WebViewClient()
            binding.webView.settings.setSupportZoom(true)
            binding.webView.settings.builtInZoomControls = true
            binding.webView.settings.javaScriptEnabled = true
            binding.webView.loadUrl(OIL_URL)
        }

//        getOil()

    }

//    private fun getOil(){
//        OkHttpUtil.mOkHttpUtil.getAsync(OIL_URL, object : OkHttpUtil.ICallback {
//            override fun onResponse(response: Response) {
//
//                val oilData = response.body?.string()
//                Log.d("QQQ", "response: $oilData")
//                binding.tv.text=oilData
//
//                runOnUiThread{
//
//
//                }
//            }
//
//            override fun onFailure(e: okio.IOException) {
//                Log.d("HKT", "onFailure: $e")
//
//            }
//        })
//    }
}