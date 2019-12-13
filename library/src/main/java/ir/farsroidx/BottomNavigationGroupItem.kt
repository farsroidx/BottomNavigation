package ir.farsroidx

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.paris.annotations.Styleable
import com.airbnb.paris.extensions.style

@Styleable("BottomNavigationGroupItem")
class BottomNavigationGroupItem : RelativeLayout {

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

    private lateinit var imageView: AppCompatImageView
    private lateinit var textView: AppCompatTextView

    private var colorFilter: Int = Color.DKGRAY

    private fun initial(attrs: AttributeSet?){

        style(attrs)

        val view = View.inflate(context , R.layout.widget_bottom_navigation_group_itrm , this)

        imageView = view.findViewById(R.id.imageView)
        textView  = view.findViewById(R.id.textView)
    }

    fun setImageDrawable(drawable: Drawable) {
        imageView.setImageDrawable(drawable)
        invalidate()
    }

    fun setColorFilter(color: Int, mode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN) {
        imageView.setColorFilter(color, mode)
        colorFilter = color
        invalidate()
    }

    fun setText(text: String){
        textView.text = text
        //startAnimationText(textView)
        invalidate()
    }

    private fun hideSubMenu(position: Int, view: View){
        val animation = AnimationUtils.loadAnimation(context, R.anim.animation_hide_sub_menu)
        animation.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {

            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
        view.startAnimation(animation)
    }

    private fun startAnimationText(view: View){
        val animation = AnimationUtils.loadAnimation(context, R.anim.animation_show_sub_menu)
        view.startAnimation(animation)
    }

    private fun mainThread(block: () -> Unit){
        handler.post {
            block()
        }
    }
}