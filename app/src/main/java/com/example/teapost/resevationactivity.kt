package com.example.teapost

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class resevationactivity : AppCompatActivity() {

    private lateinit var dbHelper: ItemDatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var reservationAdapter: ReservationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reservation) // Make sure this matches your XML layout file

        dbHelper = ItemDatabaseHelper(this)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadReservations()

        val addButton = findViewById<Button>(R.id.button_add)
        addButton.setOnClickListener {
            startActivity(Intent(this, AddReservationActivity::class.java))
        }
    }

    private fun loadReservations() {
        val reservations = dbHelper.getAllReservations()
        reservationAdapter = ReservationAdapter(reservations,
            onDelete = { reservationId ->
                // Handle delete action
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Are you sure you want to delete this reservation?")
                    .setPositiveButton("Yes") { _, _ ->
                        dbHelper.deleteReservation(reservationId)
                        loadReservations() // Refresh the list
                    }
                    .setNegativeButton("No", null)
                builder.create().show()
            },
            onUpdate = { reservation ->
                val intent = Intent(this, UpdateReservationActivity::class.java).apply {
                    putExtra("RESERVATION_ID", reservation.id)
                }
                startActivity(intent)
            }
        )
        recyclerView.adapter = reservationAdapter
    }
}
