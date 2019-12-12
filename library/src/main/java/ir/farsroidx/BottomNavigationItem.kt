package ir.farsroidx

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.paris.annotations.Styleable
import com.airbnb.paris.extensions.style

@Styleable("BottomNavigationItem")
class BottomNavigationItem : RelativeLayout {

    constructor(context: Context?):
            super(context) {
        initial(null)
    }

    constructor(context: Context?, attrs: AttributeSet?):
            super(context, attrs) {
        initial(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int):
            super(context, attrs, defStyleAttr) {
        initial(attrs)
    }

    private lateinit var shape: FrameLayout
    private lateinit var imageView: AppCompatImageView
    private lateinit var textView: AppCompatTextView

    private fun initial(attrs: AttributeSet?){

        style(attrs)

        val view = View.inflate(context , R.layout.widget_bottom_navigation_itrm , this)

        shape = view.findViewById(R.id.shape)
        imageView = view.findViewById(R.id.imageView)
        textView  = view.findViewById(R.id.textView)
    }

    fun setImageDrawable(drawable: Drawable) {
        imageView.setImageDrawable(drawable)
        invalidate()
    }

    fun setColorFilter(color: Int, mode: PorterDuff.Mode) {
        imageView.setColorFilter(color, mode)
        invalidate()
    }

    fun disableItem(){

        val lp = this.layoutParams as LinearLayout.LayoutParams
        lp.setMargins(
            BottomNavigationHelper.dpToPx(0),
            BottomNavigationHelper.dpToPx(0),
            BottomNavigationHelper.dpToPx(0),
            BottomNavigationHelper.dpToPx(0)
        )
        this.layoutParams = lp

        val ilp = imageView.layoutParams as LinearLayout.LayoutParams
        ilp.setMargins(
            BottomNavigationHelper.dpToPx(2),
            BottomNavigationHelper.dpToPx(2),
            BottomNavigationHelper.dpToPx(2),
            BottomNavigationHelper.dpToPx(2)
        )
        ilp.width = BottomNavigationHelper.dpToPx(38)
        ilp.weight = 1f
        ilp.height = BottomNavigationHelper.dpToPx(38)
        imageView.layoutParams = ilp

        textView.visibility = View.GONE
        shape.visibility = View.GONE

        invalidate()
    }

    fun enableItem(){

        val lp = this.layoutParams as LinearLayout.LayoutParams
        lp.setMargins(
            BottomNavigationHelper.dpToPx(4),
            BottomNavigationHelper.dpToPx(0),
            BottomNavigationHelper.dpToPx(4),
            BottomNavigationHelper.dpToPx(0)
        )
        this.layoutParams = lp

        val ilp = imageView.layoutParams as LinearLayout.LayoutParams
        ilp.setMargins(
            BottomNavigationHelper.dpToPx(0),
            BottomNavigationHelper.dpToPx(4),
            BottomNavigationHelper.dpToPx(0),
            BottomNavigationHelper.dpToPx(4)
        )

        ilp.width = BottomNavigationHelper.dpToPx(55)
        ilp.height = BottomNavigationHelper.dpToPx(55)
        imageView.layoutParams = ilp

        val tlp = textView.layoutParams as LinearLayout.LayoutParams
        tlp.width = BottomNavigationHelper.dpToPx(80)
        textView.layoutParams = tlp

        shape.visibility = View.VISIBLE
        imageView.visibility = View.VISIBLE
        textView.visibility = View.VISIBLE
        textView.setTextColor(Color.WHITE)
        textView.isSelected = true

        invalidate()
    }

    fun setText(text: String){
        textView.text = text
        invalidate()
    }

}