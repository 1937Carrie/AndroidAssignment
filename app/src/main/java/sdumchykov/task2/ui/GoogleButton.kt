package sdumchykov.task2.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import sdumchykov.task2.R

class GoogleButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        textSize = 50f
        textAlign = Paint.Align.CENTER
    }

    var set = intArrayOf(android.R.attr.text)
    val text = "GOOGLE"

    val fontFamily = ResourcesCompat.getFont(context, R.font.roboto_regular)

    //        context.obtainStyledAttributes(attrs, set).getText(0).toString()
    /*attrs?.getAttributeValue(
    "http://schemas.android.com/apk/res/android",
    "text"
)*/
    val width = paint.measureText(text)
    private val rect = Rect()
    private var radius = getDPValue(4F)  // Radius of the rounded corner.
    private val icon = ResourcesCompat.getDrawable(resources, R.drawable.google_logo, null)!!
    private val iconSide = getDPValue(18F).toInt()


    init {
//        init(attrs)

        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.setColor(Color.WHITE)
        shape.cornerRadius = radius
        background = shape

        paint.getTextBounds(text, 0, text.lastIndex, rect)

    }

//  TODO handle with inflate and why this has wrong type
//    private fun init(attrs: AttributeSet?) {
//        inflate(context, R.layout.custom_button_layout, this)
//    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas != null) {
            val startX = measuredWidth / 2 - (iconSide / 2 + getDPValue(24F) + width / 2)
            icon.setBounds(
                startX.toInt(),
                (measuredHeight / 2F).toInt() - iconSide / 2,
                (startX + iconSide).toInt(),
                (measuredHeight / 2F + iconSide / 2).toInt()
            )
            icon.draw(canvas)
        }

        paint.typeface = fontFamily
        val yPos = (height / 2 - (paint.descent() + paint.ascent()) / 2)
        canvas?.drawText(text, measuredWidth / 2F, yPos, paint)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentViewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentViewHeight = MeasureSpec.getSize(heightMeasureSpec)

        val newWidth = getDPValue(200F).toInt()
        val newHeight = getDPValue(36F).toInt()

        super.onMeasure(
            widthMeasureSpec, MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.EXACTLY)
        )

    }

    private fun getDPValue(dpValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dpValue, context.resources.displayMetrics
        )
    }

}