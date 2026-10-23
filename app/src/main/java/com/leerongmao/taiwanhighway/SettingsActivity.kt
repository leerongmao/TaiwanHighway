package com.leerongmao.taiwanhighway

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.leerongmao.taiwanhighway.databinding.ActivitySettingsBinding
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 隱藏系統提供的 ActionBar，保留 XML 自訂 Toolbar
        supportActionBar?.hide()

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingsToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.cardAbout.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
        binding.cardAppearance.setOnClickListener {
            showThemeDialog()
        }
    }

    private fun showThemeDialog() {
        val options = arrayOf(
            "跟隨系統",
            "淺色模式",
            "深色模式"
        )

        val modes = intArrayOf(
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
            AppCompatDelegate.MODE_NIGHT_NO,
            AppCompatDelegate.MODE_NIGHT_YES
        )

        val currentMode = AppCompatDelegate.getDefaultNightMode()

        val selectedIndex = modes.indexOf(currentMode)
            .takeIf { it >= 0 } ?: 0

        AlertDialog.Builder(this)
            .setTitle("外觀設定")
            .setSingleChoiceItems(options, selectedIndex) { dialog, which ->
                val selectedMode = modes[which]

                // 保存使用者選擇
                getSharedPreferences("app_settings", MODE_PRIVATE)
                    .edit()
                    .putInt("theme_mode", selectedMode)
                    .apply()

                dialog.dismiss()

                // 套用主題，可能會重新建立 Activity
                AppCompatDelegate.setDefaultNightMode(selectedMode)
            }
            .setNegativeButton("取消", null)
            .show()
    }
}