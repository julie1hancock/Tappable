package julie.hancock.tappable

data class Point(val x: Float, val y: Float) {
    override fun toString(): String {
        return "($x, $y)"
    }
}

sealed class Gesture

class Tap (val point: Point): Gesture()
class Move(val points: List<Point>): Gesture()

//class Tap(point: Point, time: Long, length: Long): Gesture(point, time, length)
//class Tap(point: Point, time: Long, length: Long): Gesture(point, time, length)



enum class MotionType { UP, DOWN, MOVE, OTHER }
class Motion(
    val motionType: MotionType,
    val point: Point,
    val downTime: Long,
    val eventTime: Long,
    val pointerCount: Int,
    val pressure: Float
)