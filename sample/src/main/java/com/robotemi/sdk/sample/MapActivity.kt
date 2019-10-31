package com.robotemi.sdk.sample

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.robotemi.sdk.Robot
import kotlinx.android.synthetic.main.map.*
import java.nio.ByteBuffer

class MapActivity : AppCompatActivity(), Robot.MapListener {

    var count = 0
    var width = 0
    var height = 0

    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.0f
    override fun onMap(mapBytes: ByteArray?, width: Int, height: Int) {
        if (count == 0) {
            this.width = width
            this.height = height
            val buffer = ByteBuffer.wrap(mapBytes)
            val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            var k = 0
            for (i in 0 until height) {
                for (j in 0 until width) {
                    if (k < mapBytes!!.size - 4) {
                        if (buffer.getInt(k) == -1) {
                            b.setPixel(j, i, Color.RED)
                        } else if (buffer.getInt(k) < 35) {
                            b.setPixel(j, i, Color.LTGRAY)
                        } else if (buffer.getInt(k) < 75) {
                            b.setPixel(j, i, Color.GRAY)
                        } else {
                            b.setPixel(j, i, Color.DKGRAY)
                        }
                    }
                    k++
                }
            }
            mapBitmap.setImageBitmap(b)
            count++
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map)
        mScaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
    }

    override fun onStart() {
        super.onStart()
        val robot = Robot.getInstance()
        robot.speak2(this)
        mapBitmap.setOnTouchListener(View.OnTouchListener { v, event ->
            mScaleGestureDetector!!.onTouchEvent(event)
            if (event.action == MotionEvent.ACTION_DOWN) {
                Log.d("TouchEvent", "Width - " + width.toString())
                Log.d("TouchEvent", "Height - " + height.toString())
                Log.d("TouchEvent", "WidthPhone - " + mapBitmap.width.toString())
                Log.d("TouchEvent", "HeightPhone - " + mapBitmap.height.toString())
                Log.d("TouchEvent", "X - " + event.x.toString())
                Log.d("TouchEvent", "Y - " + event.y.toString())
                Log.d("TouchEvent", "NewX - " + (event.x * width / mapBitmap.width).toString())
                Log.d("TouchEvent", "NewY - " + (event.y * height / mapBitmap.height).toString())
                val toast = Toast.makeText(applicationContext, "(" + (event.x * width / mapBitmap.width).toInt().toString() + "," + (event.y * height / mapBitmap.height).toInt().toString() + ")", Toast.LENGTH_LONG)
                toast.show()
            }

            true
        })
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            mScaleFactor *= scaleGestureDetector.scaleFactor
            mScaleFactor = Math.max(
                0.1f,
                Math.min(mScaleFactor, 10.0f)
            )
            mapBitmap.setScaleX(mScaleFactor)
            mapBitmap.setScaleY(mScaleFactor)
            return true
        }
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MapActivity::class.java))
        }

        fun startActivity(context: MainActivity, mapBytes: ByteArray, width: Int, height: Int) {
            val intent = Intent(context, MapActivity::class.java)
            intent.putExtra("mapBytes", mapBytes)
            intent.putExtra("width", width)
            intent.putExtra("height", height)
            context.startActivity(intent)
        }
    }
}
