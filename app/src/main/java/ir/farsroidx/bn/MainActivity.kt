package ir.farsroidx.bn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import ir.farsroidx.BottomNavigation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomMenu.setOnMenuItemClickListener(object: BottomNavigation.OnMenuItemClickListener{
            override fun onClicked(menuItem: MenuItem, position: Int) {
                val text = menuItem.title
                Toast.makeText(this@MainActivity , text , Toast.LENGTH_SHORT).show()
            }

            override fun onLongClicked(menuItem: MenuItem, position: Int) {

            }
        })
    }
}
