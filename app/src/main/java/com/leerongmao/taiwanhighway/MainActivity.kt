package com.leerongmao.taiwanhighway

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.leerongmao.taiwanhighway.databinding.ActivityMainBinding
import androidx.appcompat.app.ActionBarDrawerToggle

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.homeToolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.nav_home -> {
                    // 已經在首頁，不需要重新開啟 MainActivity
                }

                R.id.nav_traffic -> {
                    startActivity(Intent(this, HighwayMainActivity::class.java))
                }

                R.id.nav_oil -> {
                    startActivity(Intent(this, OilActivity::class.java))
                }

                R.id.nav_camera -> {
                    startActivity(Intent(this, CameraActivity::class.java))
                }

                R.id.nav_etag -> {
                    startActivity(Intent(this, EtagActivity::class.java))
                }
            }

            binding.drawerLayout.closeDrawers()
            true
        }

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