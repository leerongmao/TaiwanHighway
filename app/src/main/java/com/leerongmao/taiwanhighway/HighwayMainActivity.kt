package com.leerongmao.taiwanhighway

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.leerongmao.taiwanhighway.databinding.ActivityHighwayMainBinding

private lateinit var binding: ActivityHighwayMainBinding

class HighwayMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freeway)

        binding = ActivityHighwayMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnFreewayNo1.setOnClickListener {
            val intent = Intent(this, HighwayActivity::class.java)
            intent.putExtra("road","中山高速公路-國道１號")
            intent.putExtra("road2","國道１號")
            startActivity(intent)
        }

        binding.btnFreewayNo2.setOnClickListener {
            val intent = Intent(this, HighwayActivity::class.java)
            intent.putExtra("road","機場支線-國道２號")
            intent.putExtra("road2","國道２號")
            startActivity(intent)
        }

        binding.btnFreewayNo3.setOnClickListener {
            val intent = Intent(this, HighwayActivity::class.java)
            intent.putExtra("road","福爾摩沙高速公路-國道３號")
            intent.putExtra("road2","國道３號")
            startActivity(intent)
        }

        binding.btnFreewayNo4.setOnClickListener {
            val intent = Intent(this, HighwayActivity::class.java)
            intent.putExtra("road","台中環線-國道４號")
            intent.putExtra("road2","國道４號")
            startActivity(intent)
        }

        binding.btnFreewayNo5.setOnClickListener {
            val intent = Intent(this, HighwayActivity::class.java)
            intent.putExtra("road","蔣渭水高速公路-國道５號")
            intent.putExtra("road2","國道５號")
            startActivity(intent)
        }

        binding.btnFreewayNo6.setOnClickListener {
            val intent = Intent(this, HighwayActivity::class.java)
            intent.putExtra("road","國道６號-國道６號")
            intent.putExtra("road2","國道６號")
            startActivity(intent)
        }

        binding.btnFreewayNo8.setOnClickListener {
            val intent = Intent(this, HighwayActivity::class.java)
            intent.putExtra("road","台南支線-國道８號")
            intent.putExtra("road2","國道８號")
            startActivity(intent)
        }

        binding.btnFreewayNo10.setOnClickListener {
            val intent = Intent(this, HighwayActivity::class.java)
            intent.putExtra("road","高雄支線-國道10號")
            intent.putExtra("road2","國道10號")
            startActivity(intent)
        }




    }
}