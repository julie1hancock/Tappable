package julie.hancock.tappable

import android.view.SurfaceHolder
import android.view.SurfaceView
import android.os.Bundle
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = DrawingView(this)
        setContentView(view)
        view.setOnTouchListener { view, motionEvent ->
            viewModel.onTouch(motionEvent)
            true
        }

        viewModel.visiblePoint.observe(this, Observer<List<Gesture>> { gestures ->
            view.drawGestures(gestures)
        })
    }

    inner class DrawingView(context: Context?) : SurfaceView(context) {

        private val surfaceHolder: SurfaceHolder = holder
        private val tapPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.RED
            style = Paint.Style.FILL
        }

        private val textPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            style = Paint.Style.FILL
            textSize = 50f
        }
        private val movePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.GREEN
            style = Paint.Style.FILL
        }

        fun drawGesture(gesture: Gesture, index: Int, canvas: Canvas) {
            if (!surfaceHolder.surface.isValid) return
            fun drawIndex(point: Point) {
                canvas.drawText(index.toString(), point.x-20, point.y+20, textPaint)
            }
            fun drawCircle(point: Point, size: Float = 50f, paint: Paint) {
                canvas.drawCircle(point.x, point.y, size, paint)
            }
            fun drawTap(point: Point, paint: Paint = tapPaint) {
                drawCircle(point = point, paint = paint)
                drawIndex(point)
            }
            fun drawMove(points: List<Point>) {
                for (i in 1..points.size-2) {
                    canvas.drawLine(points[i-1].x, points[i-1].y, points[i].x, points[i].y, movePaint)
                    drawCircle(point = points[i], size = 5f, paint = movePaint)
                }
                drawTap(points.first(), paint = movePaint)
                drawTap(points.last(), paint = movePaint)
            }


            if (gesture is Tap) {
                drawTap(gesture.point)
            } else if (gesture is Move) {
                drawMove(gesture.points)
            }
        }

        fun drawGestures(gestures: List<Gesture>?) {
            val canvas: Canvas = surfaceHolder.lockCanvas()
            canvas.drawColor(Color.BLACK)
            gestures?.forEachIndexed { i, gesture ->
                drawGesture(gesture, i, canvas)
            }
            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }
}