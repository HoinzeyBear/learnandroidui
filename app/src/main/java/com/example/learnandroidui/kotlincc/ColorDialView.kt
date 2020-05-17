package com.example.learnandroidui.kotlincc

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.example.learnandroidui.R
import kotlin.math.roundToInt

class ColorDialView @JvmOverloads constructor(context: Context,
                                              attrs: AttributeSet? = null,
                                              defStyleAttr: Int = 0,
                                              defStyleRes: Int = 0)
    : View(context,attrs,defStyleAttr,defStyleRes) {

    private var colors: ArrayList<Int> = arrayListOf(Color.RED, Color.GREEN, Color.GRAY,
                                                    Color.DKGRAY, Color.MAGENTA,Color.YELLOW, Color.CYAN)

    private var scale = 1f
    private var scaleToFit = false
    private var dialDrawable: Drawable? = null
//    private var noColorDrawable: Drawable? = null
    private val paint = Paint().also {
        it.color = Color.BLUE
        it.isAntiAlias = true
    }

    private var dialDiameter = toDP(100)
    private var extraPadding = toDP(30)
    private var tickSize = toDP(10).toFloat()
    private var tickSizeScaled = tickSize * scale
    private var angle = 0f
    //padding values
    private var totalPaddingLeft = 0f
    private var totalPaddingTop = 0f
    private var totalPaddingRight = 0f
    private var totalPaddingBottom = 0f
    //Pre-compute horizontal and vertical sizes to save processing in onDraw
    private var tickPositionVertical = 0f
    private var horizontalSize = 0f
    private var verticalSize = 0f
    //Helper values
    private var centerHorizontal = 0f
    private var centerVertical = 0f
    //view interaction values
    private var dragStartX = 0f
    private var dragStartY = 0f
    private var dragging = false
    private var snapAngle = 0f
    private var selectedPosition = 0

    var selectedColorValue: Int = android.R.color.transparent
    set(value) {
        val index = colors.indexOf(value)
        selectedPosition = if(index == -1) 0 else index
        snapAngle = (selectedPosition * angle)
        invalidate()
    }
    private var listeners: ArrayList<(Int) -> Unit> = arrayListOf()

    fun addListener(function: (Int) -> Unit) {
        listeners.add(function)
    }

    private fun broadcastColorChange() {
        listeners.forEach {
            if(selectedPosition > colors.size -1) {
                it(colors[0])
            } else {
                it(colors[selectedPosition])
            }
        }
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ColorDialView)
        try {
            dialDiameter = typedArray.getDimension(R.styleable.ColorDialView_dialDiameter,
                    toDP(100).toFloat()).toInt()

            extraPadding = typedArray.getDimension(R.styleable.ColorDialView_tickPadding,
                    toDP(30).toFloat()).toInt()

            tickSize = typedArray.getDimension(R.styleable.ColorDialView_tickRadius,
                    toDP(10).toFloat())

            scaleToFit = typedArray.getBoolean(R.styleable.ColorDialView_scaletofit, false)
        } finally {
            typedArray.recycle()
        }

        dialDrawable = context.getDrawable(R.drawable.ic_dial).also {
            it?.bounds = getCenteredBounds(dialDiameter)
            it?.setTint(Color.DKGRAY)
        }

        angle = 360f / colors.size
        refreshValues(false)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if(scaleToFit) {
            refreshValues(false)
            val specWidth = MeasureSpec.getSize(widthMeasureSpec)
            val specHeight = MeasureSpec.getSize(heightMeasureSpec)

            val workingWidth = specWidth - paddingLeft - paddingRight
            val workingHeight = specHeight - paddingTop - paddingBottom
            scale = if(workingWidth < workingHeight) {
                (workingWidth) / (horizontalSize - paddingLeft - paddingRight)
            } else {
                (workingHeight) / (verticalSize - paddingTop - paddingBottom)
            }
            dialDrawable?.let { it.bounds = getCenteredBounds((dialDiameter * scale).toInt()) }
            val width = resolveSizeAndState((horizontalSize * scale).toInt(),
                    widthMeasureSpec,
                    0)

            val height = resolveSizeAndState((verticalSize * scale).toInt(),
                    heightMeasureSpec,
                    0)
            refreshValues(true)
            setMeasuredDimension(width,height)
        } else {
            val width = resolveSizeAndState(horizontalSize.toInt(),
                    widthMeasureSpec,
                    0)

            val height = resolveSizeAndState(verticalSize.toInt(),
                    heightMeasureSpec,
                    0)

            setMeasuredDimension(width,height)
        }
    }

    override fun onDraw(canvas: Canvas) {
        val saveCount = canvas.save()
        colors.forEachIndexed { i, color ->
                paint.color = colors[i]
                canvas.drawCircle(centerHorizontal,
                        tickPositionVertical,
                        tickSizeScaled,
                        paint)

                canvas.rotate(angle,
                        centerHorizontal,
                        centerVertical)
//            }
        }
        canvas.restoreToCount(saveCount)
        canvas.rotate(snapAngle,centerHorizontal,centerVertical)
        canvas.translate(centerHorizontal, centerVertical)
        dialDrawable?.draw(canvas)
    }

    private fun refreshValues(withScale: Boolean) {
        val localScale = if(withScale) scale else 1f
        //Compute padding values
        this.totalPaddingLeft = (paddingLeft + extraPadding * localScale)
        this.totalPaddingTop = (paddingTop + extraPadding * localScale)
        this.totalPaddingRight = (paddingRight + extraPadding * localScale)
        this.totalPaddingBottom = (paddingBottom + extraPadding * localScale)

        //Compute helper values
        this.horizontalSize = paddingLeft + paddingRight + (extraPadding * localScale * 2) + dialDiameter.toFloat() * localScale
        this.verticalSize = paddingTop + paddingBottom + (extraPadding * localScale * 2) + dialDiameter.toFloat() * localScale

        //Compute position values
        this.tickPositionVertical = paddingTop + (extraPadding * localScale) /2f
        centerHorizontal = totalPaddingLeft + (horizontalSize - totalPaddingLeft - totalPaddingRight) /2f
        centerVertical = totalPaddingTop + (verticalSize - totalPaddingTop - totalPaddingBottom) /2f
        tickSizeScaled = tickSize * localScale
    }

    private fun getCenteredBounds(size: Int, scalar: Float = 1f): Rect {
        val half = (( if(size > 0) size/2 else 1) * scalar).toInt()
        return Rect(-half, -half, half, half)
    }

    private fun toDP(value: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                value.toFloat(),
                context.resources.displayMetrics).toInt()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragStartX = event.x
        dragStartY = event.y
        if(event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
            dragging = true

            if(getSnapAngle(dragStartX,dragStartY)){
                broadcastColorChange()
                invalidate()
            }
        }
        if(event.action == MotionEvent.ACTION_UP) {
            dragging = false
        }
        return true
    }

    private fun getSnapAngle(x: Float, y: Float):Boolean  {
        var dragAngle = cartesianToPolar(x - horizontalSize /2, (verticalSize -y) -verticalSize /2)
        val nearest: Int = (getNearestAngle(dragAngle) / angle).roundToInt()
        val newAngle:Float = nearest * angle
        var shouldUpdate = false
        if (newAngle != snapAngle){
            shouldUpdate = true
            selectedPosition = nearest
        }
        snapAngle = newAngle
        return shouldUpdate
    }

    private fun getNearestAngle(dragAngle: Float): Float {
        var adjustAngle = (360 - dragAngle) + 90
        while (adjustAngle > 360) adjustAngle -= 360
        return adjustAngle
    }

    private fun cartesianToPolar(x: Float, y: Float): Float {
        val angle: Float = Math.toDegrees((Math.atan2(y.toDouble(), x.toDouble()))).toFloat()
        return when(angle) {
            in 0f .. 180f -> angle
            in -180f .. 0f -> angle + 360f
            else -> angle
        }
    }
}
