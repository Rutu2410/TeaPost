package com.example.teapost

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.teapost.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create a Handler object
        val handler = Handler()

        // Define the delay (in milliseconds) before moving to the next activity
        val delayMillis: Long = 2000 // 2 seconds

        // Runnable to start the next activity
        val runnable = Runnable {
            // Create an Intent to start the next activity
            val intent = Intent(this, login::class.java)
            startActivity(intent)

            // Finish current activity (optional, depending on your needs)
            finish()
        }
        // Execute the Runnable after the specified delay
        handler.postDelayed(runnable, delayMillis)
    }
}
