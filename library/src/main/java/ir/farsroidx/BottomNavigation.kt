package ir.farsroidx

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.MenuRes
import androidx.appcompat.view.SupportMenuInflater
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.get
import com.airbnb.paris.annotations.Attr
import com.airbnb.paris.annotations.Styleable
import com.airbnb.paris.extensions.style

@Styleable("BottomNavigation")
class BottomNavigation : FrameLayout {

    companion object {
        private var COLOR_BACKGROUND  = Color.parseColor("#FFFFFF")
        private var COLOR_ITEM_NORMAL  = Color.parseColor("#000000")
        private var COLOR_ITEM_SELECTED = Color.parseColor("#FFFFFF")

        private var currentItem = 4
    }

    constructor(context: Context):
            super(context) {
        initial(null)
    }

    constructor(context: Context, attrs: AttributeSet?):
            super(context, attrs) {
        initial(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
            super(context, attrs, defStyleAttr) {
        initial(attrs)
    }

    private var backLayout = FrameLayout(context)
        .apply {
            layoutParams = LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                BottomNavigationHelper.dpToPx(56),
                Gravity.BOTTOM
            )
            setBackgroundColor(COLOR_BACKGROUND)
        }

    private var menuLayout = LinearLayout(context)
        .apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            gravity = Gravity.BOTTOM
        }

    private fun initial(attrs: AttributeSet?) {

        style(attrs)

        val lp = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        ).apply {
            minimumHeight = BottomNavigationHelper.dpToPx(56)
        }

        this.layoutParams = lp

        this.addView(backLayout)
        this.addView(menuLayout)
    }

    @Attr(R2.styleable.BottomNavigation_menu)
    fun setMenu(@MenuRes menuId: Int) {

        if (menuId != -1){
            inflateMenu(menuId)
        } else {
            throw BottomNavigationException("In view, (Menu) is invalid or not found.")
        }
    }

    private fun inflateMenu(menuId: Int) {
        val menu = MenuBuilder(context)
        SupportMenuInflater(context).inflate(menuId , menu)
        createMenu(menu)
    }

    private fun createMenu(menu: Menu){

        menuLayout.removeAllViews()

        for (i in 0 until menu.size()){

            val itemView = BottomNavigationItem(context)

            if (currentItem == i){
                setupCurrentItem(itemView, menu[i])
            } else {
                setupItem(itemView, menu[i])
            }

            itemView.setOnClickListener {
                currentItem = i
                Log.d("CentralCore" , "current: $i")

                createMenu(menu)
            }
        }
    }

    private fun setupCurrentItem(itemView: BottomNavigationItem, menuItem: MenuItem){

        val layout = LinearLayout(context)
        layout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER
            weight = 0f
        }

        itemView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT

        ).apply {
            gravity = Gravity.CENTER
        }

        val drawable = menuItem.icon

        itemView.setImageDrawable(drawable!!)
        itemView.setColorFilter(COLOR_ITEM_SELECTED, PorterDuff.Mode.SRC_IN)
        itemView.setText(menuItem.title.toString())
        itemView.enableItem()

        layout.addView(itemView)
        menuLayout.addView(layout)
    }

    private fun setupItem(itemView: BottomNavigationItem, menuItem: MenuItem){

        val layout = LinearLayout(context)
        layout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            BottomNavigationHelper.dpToPx(56)
        ).apply {
            gravity = Gravity.BOTTOM
            weight = 1f
        }

        itemView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT

        ).apply {
            weight = 1f
            gravity = Gravity.CENTER
        }

        val drawable = menuItem.icon

        itemView.setImageDrawable(drawable!!)
        itemView.setColorFilter(COLOR_ITEM_NORMAL, PorterDuff.Mode.SRC_IN)
        itemView.disableItem()

        layout.addView(itemView)
        menuLayout.addView(layout)
    }
}