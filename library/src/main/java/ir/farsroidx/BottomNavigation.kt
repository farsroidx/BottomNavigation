package ir.farsroidx

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
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

        private var defPos = 0
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

    private var onMenuItemClickListener: OnMenuItemClickListener? = null

    private var backLayout = FrameLayout(context)
        .apply {
            layoutParams = LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                BottomNavigationHelper.dpToPx(56),
                Gravity.BOTTOM
            )

            val vid = View.generateViewId()
            id = vid

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

    @Attr(R2.styleable.BottomNavigation_defaultPosition)
    fun setDefaultPosition(defaultPosition: Int) {
        defPos = defaultPosition
    }

    private fun inflateMenu(menuId: Int) {
        val menu = MenuBuilder(context)
        SupportMenuInflater(context).inflate(menuId , menu)
        createMenu(menu)
    }

    private fun createMenu(menu: Menu){

        menuLayout.removeAllViews()

        for (position in 0 until menu.size()){

            val itemView = BottomNavigationItem(context)

            if (defPos == position){
                setupCurrentItem(itemView, menu[position])
            } else {
                setupItem(itemView, menu[position])
            }

            itemView.setOnClickListener {

                if (defPos != position){
                    defPos = position
                    createMenu(menu)
                } else {
                    if (menu[position].hasSubMenu()){
                        itemView.openGroupMenu(menu[position].subMenu)
                    }
                }

                onMenuItemClickListener?.onClicked(menu[position], position)
            }

            itemView.setOnLongClickListener {
                onMenuItemClickListener?.onLongClicked(menu[position], position)
                return@setOnLongClickListener false
            }
        }
    }

    private fun setupCurrentItem(itemView: BottomNavigationItem, menuItem: MenuItem){

        val layout = LinearLayout(context)
        layout.apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
                weight = 0f
            }
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
        layout.apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                BottomNavigationHelper.dpToPx(56)
            ).apply {
                gravity = Gravity.BOTTOM
                weight = 1f
            }
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

    fun setOnMenuItemClickListener(listener: OnMenuItemClickListener){
        onMenuItemClickListener = listener
    }

    interface OnMenuItemClickListener {
        fun onClicked(menuItem: MenuItem, position: Int)
        fun onLongClicked(menuItem: MenuItem, position: Int)
    }
}