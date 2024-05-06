import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.media.MediaPlayer
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.saveearth.GameOver
import com.example.saveearth.R
import java.util.Random

class GameView : View {
    private var dWidth: Int = 0
    private var dHeight: Int = 0
    private lateinit var trash: Bitmap
    private lateinit var hand: Bitmap
    private lateinit var bottle: Bitmap
    private var handler: Handler = Handler()
    private lateinit var runnable: Runnable
    private val UPDATE_MILLIS: Long = 30
    private var handX: Int = 0
    private var handY: Int = 0
    private var plasticX: Int = 0
    private var plasticY: Int = 0
    private var random: Random = Random()
    private var plasticAnimation = false
    private var points = 0
    private val TEXT_SIZE = 120f
    private var textPaint: Paint = Paint().apply {
        color = Color.rgb(255, 0, 0)
        textSize = TEXT_SIZE
        textAlign = Paint.Align.LEFT
    }
    private var healthPaint: Paint = Paint().apply {
        color = Color.GREEN
    }
    private var life = 3
    private var handSpeed: Int = 0
    private var trashX: Int = 0
    private var trashY: Int = 0
    private var mpPoint: MediaPlayer? = null
    private var mpWhoosh: MediaPlayer? = null
    private var mpPop: MediaPlayer? = null

    constructor(context: Context) : super(context) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context)
    }

    private fun initialize(context: Context) {
        val display = (context as Activity).windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        dWidth = size.x
        dHeight = size.y
        trash = BitmapFactory.decodeResource(resources, R.drawable.trash)
        hand = BitmapFactory.decodeResource(resources, R.drawable.hand)
        bottle = BitmapFactory.decodeResource(resources, R.drawable.bottle)
        runnable = Runnable { invalidate() }
        handX = dWidth + random.nextInt(300)
        handY = random.nextInt(600)
        plasticX = handX
        plasticY = handY + hand.height - 30
        handSpeed = 21 + random.nextInt(30)
        trashX = dWidth / 2 - trash.width / 2
        trashY = dHeight - trash.height
        mpPoint = MediaPlayer.create(context, R.raw.point)
        mpWhoosh = MediaPlayer.create(context, R.raw.whoosh)
        mpPop = MediaPlayer.create(context, R.raw.pop)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.parseColor("#c1a88a"))
        if (!plasticAnimation) {
            handX -= handSpeed
            plasticX -= handSpeed
        }
        if (handX <= -hand.width) {
            mpWhoosh?.start()
            handX = dWidth + random.nextInt(300)
            plasticX = handX
            handY = random.nextInt(600)
            plasticY = handY + hand.height - 30
            handSpeed = 21 + random.nextInt(30)
            trashX = hand.width + random.nextInt(dWidth - 2 * hand.width)
            life--
            if (life == 0) {
                val intent = Intent(context, GameOver::class.java)
                intent.putExtra("points", points)
                context.startActivity(intent)
                (context as Activity).finish()
            }
        }
        if (plasticAnimation) {
            plasticY += 40
        }
        if (plasticAnimation && plasticX + bottle.width >= trashX && plasticX <= trashX + trash.width && plasticY + bottle.height >= dHeight - trash.height && plasticY <= dHeight) {
            mpPoint?.start()
            handX = dWidth + random.nextInt(300)
            plasticX = handX
            handY = random.nextInt(600)
            plasticY = handY + hand.height - 30
            handSpeed = 21 + random.nextInt(30)
            points++
            trashX = hand.width + random.nextInt(dWidth - 2 * hand.width)
            plasticAnimation = false
        }
        if (plasticAnimation && plasticY + bottle.height >= dHeight) {
            mpPop?.start()
            life--
            if (life == 0) {
                val intent = Intent(context, GameOver::class.java)
                intent.putExtra("points", points)
                context.startActivity(intent)
                (context as Activity).finish()
            }
            handX = dWidth + random.nextInt(300)
            plasticX = handX
            handY = random.nextInt(600)
            plasticY = handY + hand.height - 30
            trashX = hand.width + random.nextInt(dWidth - 2 * hand.width)
            plasticAnimation = false
        }
        canvas.drawBitmap(trash, trashX.toFloat(), trashY.toFloat(), null)
        canvas.drawBitmap(hand, handX.toFloat(), handY.toFloat(), null)
        canvas.drawBitmap(bottle, plasticX.toFloat(), plasticY.toFloat(), null)
        canvas.drawText("$points", 20f, TEXT_SIZE, textPaint)
        if (life == 2) healthPaint.color = Color.YELLOW else if (life == 1) healthPaint.color = Color.RED
        canvas.drawRect((dWidth - 200).toFloat(), 30f, (dWidth - 200 + 60 * life).toFloat(), 80f, healthPaint)
        if (life != 0) handler.postDelayed(runnable, UPDATE_MILLIS)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            plasticAnimation = true
        }
        return true
    }
}
