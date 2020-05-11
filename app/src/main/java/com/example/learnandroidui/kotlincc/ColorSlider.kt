package com.example.learnandroidui.kotlincc

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.example.learnandroidui.R

class ColorSlider @JvmOverloads constructor(context: Context,
                                            attrs: AttributeSet? = null,
                                            defStyleAttr: Int = R.attr.seekBarStyle)
    : androidx.appcompat.widget.AppCompatSeekBar(context, attrs, defStyleAttr) {

    private var listOfColors = arrayListOf(Color.BLACK, Color.BLUE, Color.RED, Color.GREEN, Color.DKGRAY)

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorSlider)
        listOfColors = typedArray.getTextArray(R.styleable.ColorSelector_colors).map {
            Color.parseColor(it.toString())
        } as ArrayList<Int>
        typedArray.recycle()

        listOfColors.add(0, Color.TRANSPARENT)

        splitTrack = false
        max = listOfColors.size-1
        thumb = context.getDrawable(R.drawable.ic_arrow_drop_down_black_24dp)
        setPadding(paddingLeft,paddingTop, paddingRight,paddingBottom + getPixelValueFromDP(16f).toInt())
        progressTintList = ContextCompat.getColorStateList(context, android.R.color.transparent)
        progressBackgroundTintList = ContextCompat.getColorStateList(context, android.R.color.transparent)

        setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                listeners.forEach {
                    it(listOfColors[progress])
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    var selectedColorValue: Int = android.R.color.transparent
    set(value) {
        var index = listOfColors.indexOf(value)
        if(index == -1) {
            progress = 0
        } else {
            progress = index
        }
    }

    private var listeners: ArrayList<(Int) -> Unit> = arrayListOf()

    fun addListener(function: (Int) -> Unit) {
        listeners.add(function)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawTickMarks(canvas)
    }

    private fun drawTickMarks(canvas: Canvas?) {
        canvas?.let {
            val colorCount = listOfColors.size
            val saveCount = canvas.save()
            canvas.translate(paddingLeft.toFloat(), (height/2).toFloat()+getPixelValueFromDP(16f).toInt())
            if(colorCount > 1) {
                for(i in 0 until colorCount) {
                    val w = getPixelValueFromDP(16f)
                    val h = getPixelValueFromDP(16f)
                    val halfW = w/2
                    val halfH = h/2

                    val spacing = (width - paddingLeft - paddingRight) / (colorCount-1).toFloat()
                    if(i == 0) {
                        val drawable = context.getDrawable(R.drawable.ic_clear_black_24dp)
                        val clearWidth = drawable?.intrinsicWidth?: 0
                        val clearHeight = drawable?.intrinsicHeight?: 0
                        val halfCW = clearWidth/2
                        val halfCH = clearHeight/2
                        drawable?.setBounds(
                                -halfCW,
                                -halfCH,
                                halfCW,
                                halfCH)
                        drawable?.draw(canvas)
                    } else {
                        val paint = Paint()
                        paint.color = listOfColors[i]
                        canvas.drawRect(
                                -halfW,
                                -halfW,
                                halfW,
                                halfH,
                                paint)
                    }
                    canvas.translate(spacing, 0f)
                }
                canvas.restoreToCount(saveCount)
            }
        }
    }

    private fun getPixelValueFromDP(value: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                value,
                context.resources.displayMetrics)
    }
}