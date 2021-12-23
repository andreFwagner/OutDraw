package com.example.android.outdraw.paint

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.findFragment
import com.example.android.outdraw.R
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.random.Random

/**
 * View with Drawing Logic and Save Function
 */

private const val STROKE_WIDTH = 12f

class MyCanvasView(context: Context) : View(context) {

    lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap

    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop

    private val drawColor = ResourcesCompat.getColor(resources, R.color.primaryDarkColor, null)

    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.generalBackground, null)

    // Set up the paint with which to draw.
    private val paint = Paint().apply {
        color = drawColor
        // Smooths out edges of what is drawn without affecting shape.
        isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        isDither = true
        style = Paint.Style.STROKE // default: FILL
        strokeJoin = Paint.Join.ROUND // default: MITER
        strokeCap = Paint.Cap.ROUND // default: BUTT
        strokeWidth = STROKE_WIDTH // default: Hairline-width (really thin)
    }

    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f
    private var currentX = 0f
    private var currentY = 0f

    private val path = Path()

    private fun touchStart() {
        this.findFragment<PaintFragment>().fadeUIOut()
        randomColor()
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    private fun touchMove() {
        val dx = abs(motionTouchEventX - currentX)
        val dy = abs(motionTouchEventY - currentY)
        if (dx >= touchTolerance || dy >= touchTolerance) {
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).
            path.quadTo(currentX, currentY, (motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2)
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            // Draw the path in the extra bitmap to cache it.
            extraCanvas.drawPath(path, paint)
        }
        invalidate()
    }

    private fun touchUp() {
        this.findFragment<PaintFragment>().fadeUIIn()
        path.reset()
    }

    private fun randomColor() {
        val random1 = Random
        val random2 = Random
        val width = random1.nextInt(500).toFloat() / 10
        paint.strokeWidth = width
        paint.setARGB(255, random2.nextInt(256), random2.nextInt(256), random2.nextInt(256))
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        motionTouchEventX = event.x
        motionTouchEventY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(extraBitmap, 0f, 0f, null)
    }

    fun saveBitmap(): String {
        val format = SimpleDateFormat("dd-M-yyy-hh-mm-ss")
        val date = format.format(Date())
        val file = File(context.getExternalFilesDir(null).toString() + "/painting-$date.png")

        try {
            extraBitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(file))
        } catch (e: Exception) {
            Log.e(null, "Error Saving Bitmap: $e")
        }

        return file.path.toString()
    }
}
