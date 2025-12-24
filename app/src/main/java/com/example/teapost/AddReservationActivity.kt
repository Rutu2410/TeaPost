package com.example.teapost

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class AddReservationActivity : AppCompatActivity() {

    private lateinit var dbHelper: ItemDatabaseHelper
    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reservation)

        dbHelper = ItemDatabaseHelper(this)
        calendar = Calendar.getInstance()

        val nameEditText = findViewById<EditText>(R.id.edit_name)
        val dateEditText = findViewById<EditText>(R.id.edit_date)
        val timeEditText = findViewById<EditText>(R.id.edit_time)
        val peopleEditText = findViewById<EditText>(R.id.edit_people)
        val specialEventEditText = findViewById<EditText>(R.id.edit_special_event)
        val saveButton = findViewById<Button>(R.id.button_save)

        // Set click listeners for date and time EditText fields
        dateEditText.setOnClickListener {
            showDatePickerDialog()
        }

        timeEditText.setOnClickListener {
            showTimePickerDialog()
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val date = dateEditText.text.toString()
            val time = timeEditText.text.toString()
            val people = peopleEditText.text.toString().toIntOrNull() ?: 0
            val specialEvent = specialEventEditText.text.toString()

            if (name.isBlank() || date.isBlank() || time.isBlank()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val reservation = Reservation(
                id = 0, // New reservation, so ID is 0
                name = name,
                date = date,
                time = time,
                people = people,
                specialEvent = specialEvent
            )

            dbHelper.insertReservation(reservation)
            Toast.makeText(this, "Reservation saved", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,resevationactivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish() // Close activity and return to previous screen
        }
    }

    private fun showDatePickerDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            val formattedDate = "${year}-${month + 1}-${dayOfMonth}"
            findViewById<EditText>(R.id.edit_date).setText(formattedDate)
        }, year, month, day).show()
    }

    private fun showTimePickerDialog() {
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val is24HourFormat = false

        TimePickerDialog(this, { _, hourOfDay, minute1 ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute1)
            val amPm = if (calendar.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"
            val formattedTime = String.format("%02d:%02d %s", hourOfDay, minute1, amPm)
            findViewById<EditText>(R.id.edit_time).setText(formattedTime)
        }, hour, minute, is24HourFormat).show()
    }
}
