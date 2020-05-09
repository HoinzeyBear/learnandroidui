package com.example.learnandroidui.kotlincc

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.learnandroidui.R
import kotlinx.android.synthetic.main.color_selector.view.*

class ColorSelector @JvmOverloads
    constructor(context: Context,
                attrs: AttributeSet? = null,
                defStyleAttr: Int = 0,
                defStyleRes: Int = 0)
    : LinearLayout(context,attrs,defStyleAttr,defStyleRes) {

    private var listOfColors = listOf(Color.BLACK, Color.BLUE, Color.RED, Color.GREEN)
    private var selectedColorIndex = 0
    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorSelector)
        listOfColors = typedArray.getTextArray(R.styleable.ColorSelector_colors).map {
            Color.parseColor(it.toString())
        }
        typedArray.recycle()

        orientation = LinearLayout.HORIZONTAL
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater
        inflater.inflate(R.layout.color_selector, this)
        selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])

        arrow_right.setOnClickListener {
            selectNextColor()
        }

        arrow_left.setOnClickListener {
            selectPreviousColor()
        }
    }

    private var colorSelectListener: ((Int) -> Unit)? = null

    fun setListener(color: (Int) -> Unit) {
           this.colorSelectListener = color
    }

    private fun selectNextColor() {
        if (selectedColorIndex == listOfColors.lastIndex) {
            selectedColorIndex = 0
        } else {
            selectedColorIndex ++
        }
        selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])
        broadcastColor(listOfColors[selectedColorIndex])
    }

    private fun selectPreviousColor() {
        if (selectedColorIndex == 0) {
            selectedColorIndex = listOfColors.lastIndex
        } else {
            selectedColorIndex --
        }
        selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])
        broadcastColor(listOfColors[selectedColorIndex])
    }

    fun broadcastColor(color: Int) {
        this.colorSelectListener?.let {
            broadcastColor -> broadcastColor(color)
        }
    }
}