package com.leerongmao.taiwanhighway

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.leerongmao.taiwanhighway.databinding.ActivityMainBinding
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import android.view.View
import android.net.Uri
import android.util.Log
import org.json.JSONObject

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private data class RoadFact(
        val id: String,
        val category: String,
        val text: String,
        val sourceUrl: String
    )

    private var roadFacts: List<RoadFact> = emptyList()
    private var currentRoadFact: RoadFact? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        val savedThemeMode = getSharedPreferences("app_settings", MODE_PRIVATE)
            .getInt(
                "theme_mode",
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )

        if (AppCompatDelegate.getDefaultNightMode() != savedThemeMode) {
            AppCompatDelegate.setDefaultNightMode(savedThemeMode)
        }

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
                    startActivity(Intent(this, CameraListActivity::class.java))
                }

                R.id.nav_etag -> {
                    startActivity(Intent(this, EtagActivity::class.java))
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
                R.id.nav_classic -> {
                    startActivity(
                        Intent(this, OldRoadActivity::class.java)
                    )
                    true
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
            val intent = Intent(this, CameraListActivity::class.java)
            startActivity(intent)
        }

        // 初始化公路小知識

        roadFacts = loadRoadFacts()

        showRandomRoadFact()

        binding.btnRoadFactNext.setOnClickListener {
            showRandomRoadFact()
        }

        binding.btnRoadFactSource.setOnClickListener {
            currentRoadFact?.sourceUrl
                ?.takeIf { it.isNotBlank() }
                ?.let { url ->
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    } catch (e: Exception) {
                        Log.e("RoadFacts", "無法開啟資料來源", e)
                    }
                }
        }
    }

    private fun loadRoadFacts(): List<RoadFact> {
        return try {
            val json = assets.open("road_facts.json")
                .bufferedReader()
                .use { it.readText() }

            val array = JSONObject(json).getJSONArray("facts")

            (0 until array.length()).map { index ->
                val item = array.getJSONObject(index)

                RoadFact(
                    id = item.getString("id"),
                    category = item.getString("category"),
                    text = item.getString("text"),
                    sourceUrl = item.optString("sourceUrl")
                )
            }
        } catch (e: Exception) {
            Log.e("RoadFacts", "讀取公路小知識失敗", e)
            emptyList()
        }
    }

    private fun showRandomRoadFact() {
        if (roadFacts.isEmpty()) {
            binding.cardRoadFact.visibility = View.GONE
            return
        }

        val candidates = roadFacts.filter {
            it.id != currentRoadFact?.id
        }

        val selected = (candidates.ifEmpty { roadFacts }).random()
        currentRoadFact = selected

        binding.tvRoadFactCategory.text = selected.category
        binding.tvRoadFactText.text = selected.text

        binding.btnRoadFactSource.visibility =
            if (selected.sourceUrl.isBlank()) View.GONE else View.VISIBLE

        binding.btnRoadFactNext.isEnabled = roadFacts.size > 1
    }



    override fun onResume() {
        super.onResume()

        val unlocked = getSharedPreferences("app_settings", MODE_PRIVATE)
            .getBoolean("classic_unlocked", false)

        binding.navigationView.menu
            .findItem(R.id.nav_classic)
            ?.isVisible = unlocked

        binding.classicSection.visibility =
            if (unlocked) View.VISIBLE else View.GONE
    }
}