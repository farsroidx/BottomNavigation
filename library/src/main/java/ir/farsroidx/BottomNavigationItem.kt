package ir.farsroidx

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.SubMenu
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.get
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
    private lateinit var subMenus: LinearLayout
    private lateinit var textView: AppCompatTextView

    private var colorFilter: Int = Color.DKGRAY

    private var groupIsOpened = false
    private var groupChangeClicked = false

    private fun initial(attrs: AttributeSet?){

        style(attrs)

        val view = View.inflate(context , R.layout.widget_bottom_navigation_itrm , this)

        shape = view.findViewById(R.id.shape)
        imageView = view.findViewById(R.id.imageView)
        subMenus = view.findViewById(R.id.subMenus)
        textView  = view.findViewById(R.id.textView)
    }

    fun setImageDrawable(drawable: Drawable) {
        imageView.setImageDrawable(drawable)
        invalidate()
    }

    fun setColorFilter(color: Int, mode: PorterDuff.Mode) {
        imageView.setColorFilter(color, mode)
        colorFilter = color
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
            BottomNavigationHelper.dpToPx(0)
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
            BottomNavigationHelper.dpToPx(6),
            BottomNavigationHelper.dpToPx(0),
            BottomNavigationHelper.dpToPx(0)
        )

        ilp.width = BottomNavigationHelper.dpToPx(45)
        ilp.height = BottomNavigationHelper.dpToPx(45)
        imageView.layoutParams = ilp

        val tlp = textView.layoutParams as LinearLayout.LayoutParams
        tlp.width = BottomNavigationHelper.dpToPx(65)
        textView.layoutParams = tlp

        shape.visibility = View.VISIBLE
        imageView.visibility = View.VISIBLE
        textView.visibility = View.VISIBLE
        textView.isSelected = true

        invalidate()
    }

    fun setText(text: String){
        textView.text = text
        invalidate()
    }

    fun openGroupMenu(subMenu: SubMenu) {

        if (groupChangeClicked) {
            return
        } else {
            groupChangeClicked = true
        }

        val ilp = imageView.layoutParams as LinearLayout.LayoutParams

        Thread(Runnable {

            if (groupIsOpened){
                closeGroup()
            } else {
                openGroup(subMenu ,ilp)
            }

            groupChangeClicked = false

        }).start()
    }

    private fun openGroup(subMenu: SubMenu, ilp: LinearLayout.LayoutParams){

        mainThread {
            subMenus.visibility = View.VISIBLE
        }

        val size = subMenu.size()

        for (position in 0 until size){

            val menu = subMenu[position]
            val imageView = AppCompatImageView(context)
            imageView.layoutParams = ilp
            imageView.apply {
                setPadding(
                    BottomNavigationHelper.dpToPx(4),
                    BottomNavigationHelper.dpToPx(4),
                    BottomNavigationHelper.dpToPx(4),
                    BottomNavigationHelper.dpToPx(4)
                )
            }

            val drawable = menu.icon
            imageView.setImageDrawable(drawable!!)
            imageView.setColorFilter(colorFilter)

            mainThread {
                showSubMenu(imageView)
            }

            if (position == size - 1) {
                groupIsOpened = true
            }

            Thread.sleep(300)
        }
    }

    private fun closeGroup(){

        val count = subMenus.childCount - 1

        for (position in count downTo 0){

            mainThread {
                hideSubMenu(position, subMenus.getChildAt(position))
            }

            Thread.sleep(300)

            if (position == 1){
                groupIsOpened = false
            }
        }

        mainThread {
            subMenus.visibility = View.GONE
        }
    }

    private fun hideSubMenu(position: Int, view: View){
        val animation = AnimationUtils.loadAnimation(context, R.anim.animation_hide_sub_menu)
        animation.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                subMenus.removeViewAt(position)
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
        view.startAnimation(animation)
    }

    private fun showSubMenu(view: View){
        subMenus.addView(view, 0)
        val animation = AnimationUtils.loadAnimation(context, R.anim.animation_show_sub_menu)
        view.startAnimation(animation)
    }

    private fun mainThread(block: () -> Unit){
        handler.post {
            block()
        }
    }
}