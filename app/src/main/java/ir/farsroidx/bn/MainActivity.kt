package ir.farsroidx.bn

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ir.farsroidx.BottomNavigation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomMenu.setOnMenuItemClickListener(object: BottomNavigation.OnMenuItemClickListener{
            override fun onItemClicked(menuItem: MenuItem) {
                val text = menuItem.title
                Toast.makeText(this@MainActivity , text , Toast.LENGTH_SHORT).show()
            }
        })
    }
}
