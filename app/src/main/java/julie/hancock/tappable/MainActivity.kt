package julie.hancock.tappable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import julie.hancock.tappable.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var list = mutableListOf<Gesture>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupTapping()
    }
    lateinit var lastDown: MotionEvent
    var listSinceDown = mutableListOf<MotionEvent>()
    var startTime = System.nanoTime()
    var startCurTime: Long = 0

    private fun setupTapping() {
        binding.root.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startCurTime = System.nanoTime()
                    lastDown = event
                }
                MotionEvent.ACTION_UP -> {
                    if (listSinceDown.isEmpty()) {
                        list.add(Tap(Point(event.x, event.y), System.nanoTime()-startTime, System.nanoTime()-startCurTime))
                        println(list)
                    } else {
                        listSinceDown.clear()
                    }
                }
                else -> {
                    listSinceDown.add(event)
                }
//                MotionEvent.ACTION_MOVE -> println("ACTION_MOVE : something changed")
//                MotionEvent.ACTION_POINTER_DOWN -> println("ACTION_POINTER_DOWN : ?")
//                MotionEvent.ACTION_POINTER_UP -> println("ACTION_POINTER_UP : ?")
//
//                MotionEvent.ACTION_CANCEL -> println("ACTION_CANCEL : gesture canceled")
//                MotionEvent.ACTION_OUTSIDE -> println("ACTION_OUTSIDE : gesure outside bounds")
//                MotionEvent.ACTION_SCROLL -> println("ACTION_SCROLL : vert/horiz scroll")
//                else -> println("something else happened!")
            }
            binding.root.performClick()
            true
        }
    }


}

data class Point(val x: Float, val y: Float) {
    override fun toString(): String {
        return "($x, $y)"
    }
}

sealed class Gesture(val point: Point, val time: Long, val length: Long) {
    override fun toString(): String {
        return "$point @ $time for $length"
    }
}
class Tap(point: Point, time: Long, length: Long): Gesture(point, time, length) {
    override fun toString(): String {
        return super.toString()
    }
}