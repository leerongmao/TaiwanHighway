package com.leerongmao.taiwanhighway

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.leerongmao.taiwanhighway.databinding.ActivityFreewayBinding

private lateinit var binding: ActivityFreewayBinding

class FreewayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freeway)

        binding = ActivityFreewayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }



    }
}