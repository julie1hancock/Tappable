package julie.hancock.tappable

import android.view.MotionEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    val gestures = mutableListOf<Gesture>()
    var curEvents = mutableListOf<Motion>()
    private var _visiblePoints = MutableLiveData<List<Gesture>>()
    var visiblePoint: LiveData<List<Gesture>> = _visiblePoints

    fun onTouch(motionEvent: MotionEvent?) {
        if (motionEvent == null) return
        var msg = ""
        curEvents.add(motionEvent.toMotion())
        when (motionEvent.action) {
            MotionEvent.ACTION_UP -> {
                msg += "onUp - "
                val newGesture = curEvents.toGesture()
                newGesture?.let {
                    gestures.add(it)
                    _visiblePoints.postValue(gestures)
                }
                curEvents.clear()
            }
            MotionEvent.ACTION_DOWN -> {
                msg += "onDown - "
            }
            MotionEvent.ACTION_MOVE -> {
                msg += "onMove - "
            }
            else -> {
                msg += "else - "
            }
        }
        msg += Point(motionEvent.x, motionEvent.y)
        println(msg)
    }
}

private fun MotionEvent.toMotion() = Motion(
        motionType = this.action.toMotionType(),
        point = Point(this.x, this.y),
        downTime = this.downTime,
        eventTime = this.eventTime,
        pointerCount = this.pointerCount,
        pressure = this.pressure
    )

private fun Int.toMotionType() = when (this) {
        MotionEvent.ACTION_DOWN -> MotionType.DOWN
        MotionEvent.ACTION_UP -> MotionType.UP
        MotionEvent.ACTION_MOVE -> MotionType.MOVE
        else -> MotionType.OTHER
    }

private fun MutableList<Motion>.toGesture(): Gesture? {
    if (this.size < 2) return null
    return if (this.size == 2) {
        Tap(point = this.first().point)
    } else {
        Move(points = this.map { it.point })
    }
}