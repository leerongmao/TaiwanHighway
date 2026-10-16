package com.mufeng.taiwanhighway

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mufeng.taiwanhighway.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFreeway.setOnClickListener {
            val intent = Intent(this, HighwayMainActivity::class.java)
            startActivity(intent)
        }

        binding.btnOldroad.setOnClickListener {
            val intent = Intent(this, OldRoadActivity::class.java)
            startActivity(intent)
        }

        binding.btnEtag.setOnClickListener {
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ETAG_URL))
//            startActivity(intent)
            val intent = Intent(this, EtagActivity::class.java)
            startActivity(intent)
        }

        binding.btnOil.setOnClickListener {
            val intent = Intent(this, OilActivity::class.java)
            startActivity(intent)
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(OIL_URL))
//            startActivity(intent)
        }

        binding.btnCamera.setOnClickListener {
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(CAMERA_URL))
//            startActivity(intent)
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
    }
}