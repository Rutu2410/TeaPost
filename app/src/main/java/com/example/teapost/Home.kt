package com.example.teapost
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class Home : AppCompatActivity() {
    private lateinit var menuButton: Button
    private lateinit var imageSwitcher: ImageSwitcher
    private val imageIds = intArrayOf(R.drawable.banner2, R.drawable.banner, R.drawable.banner1, R.drawable.banner3) // Replace with your image IDs
    private var currentImageIndex = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        menuButton = findViewById(R.id.b1)
        imageSwitcher = findViewById(R.id.imageSwitcher)

        imageSwitcher.setFactory {
            ImageView(this).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }

        // Start image switching
        startImageSwitching()

        menuButton.setOnClickListener {
            val menu = PopupMenu(this, menuButton)
            menu.menuInflater.inflate(R.menu.popup_menu, menu.menu)
            menu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_item1 -> {
                        // Start Activity 1
                        startActivity(Intent(this,MENU::class.java))
                        true
                    }
                    R.id.menu_item2 -> {
                        // Start Activity 2
                        startActivity(Intent(this,resevationactivity::class.java))
                        true




                    }
                    R.id.menu_item3 -> {
                        // Start Activity 3
                        startActivity(Intent(this, Add_Memories::class.java))
                        true
                    }
                    else -> false
                }
            }
            menu.show() // Show the popup menu
        }
    }

    private fun startImageSwitching() {
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                currentImageIndex = (currentImageIndex + 1) % imageIds.size
                imageSwitcher.setImageResource(imageIds[currentImageIndex])
                handler.postDelayed(this, 2000) // Change image every 5 seconds
            }
        }
        handler.post(runnable) // Start the image switching
    }
}
