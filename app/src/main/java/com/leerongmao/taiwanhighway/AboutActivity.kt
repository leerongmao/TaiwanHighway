package com.leerongmao.taiwanhighway

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.leerongmao.taiwanhighway.databinding.ActivityAboutBinding
import android.os.SystemClock
import android.widget.Toast

class AboutActivity : AppCompatActivity() {

    private var versionTapCount = 0
    private var lastVersionTapTime = 0L

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.aboutToolbar.setNavigationOnClickListener {
            finish()
        }

        // 從 PackageManager 取得目前安裝版本
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val versionName = packageInfo.versionName ?: "Unknown"

        binding.textVersion.text = "版本 $versionName"

        binding.textVersion.setOnClickListener {
            val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)

            if (prefs.getBoolean("classic_unlocked", false)) {
                return@setOnClickListener
            }

            val now = SystemClock.elapsedRealtime()

            // 超過 2 秒沒有點擊，重新計數
            if (now - lastVersionTapTime > 2000L) {
                versionTapCount = 0
            }

            lastVersionTapTime = now
            versionTapCount++

            if (versionTapCount >= 7) {
                prefs.edit()
                    .putBoolean("classic_unlocked", true)
                    .apply()

                versionTapCount = 0

                Toast.makeText(
                    this,
                    "🎉 已解鎖 2023 經典路況！",
                    Toast.LENGTH_LONG
                ).show()
            } else if (versionTapCount >= 4) {
                Toast.makeText(
                    this,
                    "再點 ${7 - versionTapCount} 次即可解鎖",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}