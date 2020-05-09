package com.example.learnandroidui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.learnandroidui.kotlincc.ColorSelector
import kotlinx.android.synthetic.main.activity_kotlin_c_c.*

class KotlinCCActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_c_c)

        colorSelect.setListener { color ->
            swatch_button.setBackgroundColor(color)
        }

        colorslider.addListener { color ->
            swatch_button.setBackgroundColor(color)
        }
    }
}
