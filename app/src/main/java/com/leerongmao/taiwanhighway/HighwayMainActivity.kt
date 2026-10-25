package com.leerongmao.taiwanhighway

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.leerongmao.taiwanhighway.databinding.ActivityHighwayMainBinding
import android.view.View
import android.view.Gravity
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.util.TypedValue
import com.google.android.material.card.MaterialCardView
import com.google.android.material.color.MaterialColors
import com.leerongmao.taiwanhighway.data.RoadCatalog
import com.leerongmao.taiwanhighway.data.RoadInfo

private lateinit var binding: ActivityHighwayMainBinding

class HighwayMainActivity : AppCompatActivity() {

    private val expresswayCards = mutableListOf<View>()

    private val nationalBranchCards = mutableListOf<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityHighwayMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
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

        RoadCatalog.nationalBranches.forEach { road ->
            val insertIndex = when (road.name) {
                "國道2甲" ->
                    binding.roadGrid.indexOfChild(binding.btnFreewayNo2) + 1

                "國道3甲" ->
                    binding.roadGrid.indexOfChild(binding.btnFreewayNo3) + 1

                else -> binding.roadGrid.childCount
            }

            addNationalBranchCard(road, insertIndex)
        }

        binding.roadTypeGroup.addOnButtonCheckedListener {
                _, checkedId, isChecked ->

            if (isChecked) {
                when (checkedId) {
                    R.id.tabNational -> showNationalRoads()
                    R.id.tabExpress -> showExpressways()
                    R.id.tabProvincial -> {
                        binding.roadTypeGroup.check(R.id.tabNational)

                        val intent = Intent(this, HighwayActivity::class.java)
                        intent.putExtra("roadCategory", "PROVINCIAL")
                        intent.putExtra("road2", "一般省道")
                        startActivity(intent)
                    }
                }
            }
        }





    }

    private fun showNationalRoads() {

        val nationalCards = listOf(
            binding.btnFreewayNo1,
            binding.btnFreewayNo2,
            binding.btnFreewayNo3,
            binding.btnFreewayNo4,
            binding.btnFreewayNo5,
            binding.btnFreewayNo6,
            binding.btnFreewayNo8,
            binding.btnFreewayNo10
        )

        nationalCards.forEach {
            it.visibility = View.VISIBLE
        }

        expresswayCards.forEach {
            it.visibility = View.GONE
        }

        nationalBranchCards.forEach {
            it.visibility = View.VISIBLE
        }
    }

    private fun showExpressways() {

        val nationalCards = listOf(
            binding.btnFreewayNo1,
            binding.btnFreewayNo2,
            binding.btnFreewayNo3,
            binding.btnFreewayNo4,
            binding.btnFreewayNo5,
            binding.btnFreewayNo6,
            binding.btnFreewayNo8,
            binding.btnFreewayNo10
        )

        nationalCards.forEach {
            it.visibility = View.GONE
        }

        nationalBranchCards.forEach {
            it.visibility = View.GONE
        }

        if (expresswayCards.isEmpty()) {
            RoadCatalog.expressways
                .distinctBy { it.name }
                .forEach { road ->
                    addExpresswayCard(road)
                }
        }

        expresswayCards.forEach {
            it.visibility = View.VISIBLE
        }
    }

    private fun addExpresswayCard(road: RoadInfo) {

        fun dp(value: Int): Int {
            return (value * resources.displayMetrics.density).toInt()
        }

        val card = MaterialCardView(this).apply {
            radius = dp(14).toFloat()
            cardElevation = dp(2).toFloat()
            isClickable = true
            isFocusable = true
        }

        val cardParams = GridLayout.LayoutParams().apply {
            width = 0
            height = dp(96)
            columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            setMargins(dp(6), dp(6), dp(6), dp(6))
        }

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }

        val image = ImageView(this).apply {
            setImageResource(road.iconRes ?: R.drawable.ic_expressway_61)
            scaleType = ImageView.ScaleType.FIT_CENTER
            contentDescription = "${road.name}標誌"

            layoutParams = LinearLayout.LayoutParams(
                dp(42),
                dp(42)
            )
        }

        val title = TextView(this).apply {
            text = road.name
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
            setTypeface(null, android.graphics.Typeface.BOLD)
            gravity = Gravity.CENTER

            setTextColor(
                MaterialColors.getColor(
                    this,
                    com.google.android.material.R.attr.colorOnSurface
                )
            )

            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = dp(6)
            }
        }

        content.addView(image)
        content.addView(title)
        card.addView(content)

        card.setOnClickListener {
            val intent = Intent(this, HighwayActivity::class.java)

            intent.putExtra(
                "road",
                road.apiNames.firstOrNull() ?: road.name
            )

            intent.putExtra("road2", road.name)

            startActivity(intent)
        }

        binding.roadGrid.addView(card, cardParams)
        expresswayCards.add(card)
    }

    private fun addNationalBranchCard(
        road: RoadInfo,
        insertIndex: Int
    ) {
        fun dp(value: Int): Int {
            return (value * resources.displayMetrics.density).toInt()
        }

        val card = MaterialCardView(this).apply {
            radius = dp(14).toFloat()
            cardElevation = dp(2).toFloat()
            isClickable = false
            isFocusable = false
            alpha = 0.75f
        }

        val params = GridLayout.LayoutParams().apply {
            width = 0
            height = dp(96)
            columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            setMargins(dp(6), dp(6), dp(6), dp(6))
        }

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER

            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }

        val image = ImageView(this).apply {
            setImageResource(
                if (road.name == "國道3甲") {
                    R.drawable.ic_highway_3a
                } else {
                    R.drawable.ic_highway_2
                }
            )

            layoutParams = LinearLayout.LayoutParams(
                dp(42),
                dp(42)
            )

            contentDescription = "${road.name}標誌（暫用）"
        }

        val title = TextView(this).apply {
            text = "${road.name}（待確認）"
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            gravity = Gravity.CENTER

            setTextColor(
                MaterialColors.getColor(
                    this,
                    com.google.android.material.R.attr.colorOnSurface
                )
            )
        }

        content.addView(image)
        content.addView(title)
        card.addView(content)

        val safeIndex = insertIndex.coerceIn(
            0,
            binding.roadGrid.childCount
        )

        binding.roadGrid.addView(card, safeIndex, params)
        nationalBranchCards.add(card)
    }


}