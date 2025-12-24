package com.example.teapost

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UpdateReservationActivity : AppCompatActivity() {

    private lateinit var dbHelper: ItemDatabaseHelper
    private var reservationId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.updatereservation) // Ensure this layout exists

        dbHelper = ItemDatabaseHelper(this)

        val nameEditText = findViewById<EditText>(R.id.edit_name)
        val dateEditText = findViewById<EditText>(R.id.edit_date)
        val timeEditText = findViewById<EditText>(R.id.edit_time)
        val peopleEditText = findViewById<EditText>(R.id.edit_people)
        val specialEventEditText = findViewById<EditText>(R.id.edit_special_event)
        val saveButton = findViewById<Button>(R.id.button_save)

        reservationId = intent.getIntExtra("RESERVATION_ID", 0)

        // Load the reservation from the database
        val reservation = dbHelper.getReservationById(reservationId)

        if (reservation != null) {
            // Populate fields with current reservation data
            nameEditText.setText(reservation.name)
            dateEditText.setText(reservation.date)
            timeEditText.setText(reservation.time)
            peopleEditText.setText(reservation.people.toString())
            specialEventEditText.setText(reservation.specialEvent)
        } else {
            Toast.makeText(this, "Reservation not found", Toast.LENGTH_SHORT).show()
            finish() // Close activity if reservation is not found
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val date = dateEditText.text.toString()
            val time = timeEditText.text.toString()
            val people = peopleEditText.text.toString().toIntOrNull() ?: 0
            val specialEvent = specialEventEditText.text.toString()

            val updatedReservation = Reservation(
                id = reservationId,
                name = name,
                date = date,
                time = time,
                people = people,
                specialEvent = specialEvent
            )

            dbHelper.updateReservation(updatedReservation)
            Toast.makeText(this, "Reservation updated", Toast.LENGTH_SHORT).show()

            finish() // Close activity and return to previous screen
        }
    }
}
