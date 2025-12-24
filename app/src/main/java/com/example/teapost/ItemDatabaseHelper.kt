package com.example.teapost

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getIntOrNull

class ItemDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "mymy.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_ITEMS = "DATA"
        private const val COLUMN_ITEM_ID = "id"
        private const val COLUMN_ITEM_IMG = "img"
        private const val COLUMN_ITEM_NAME = "name"
        private const val COLUMN_ITEM_PRICE = "price"

        private const val TABLE_RESERVATIONS = "reservations"
        private const val COLUMN_RESERVATION_ID = "id"
        private const val COLUMN_RESERVATION_NAME = "name"
        private const val COLUMN_RESERVATION_DATE = "date"
        private const val COLUMN_RESERVATION_TIME = "time"
        private const val COLUMN_RESERVATION_PEOPLE = "people"
        private const val COLUMN_RESERVATION_SPECIAL_EVENT = "special_event" // New column
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createItemsTable = ("CREATE TABLE $TABLE_ITEMS (" +
                "$COLUMN_ITEM_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_ITEM_IMG BLOB, " +
                "$COLUMN_ITEM_NAME TEXT, " +
                "$COLUMN_ITEM_PRICE REAL)"
                )
        db.execSQL(createItemsTable)
        val createReservationsTable = ("CREATE TABLE $TABLE_RESERVATIONS (" +
                "$COLUMN_RESERVATION_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_RESERVATION_NAME TEXT, " +
                "$COLUMN_RESERVATION_DATE TEXT, " +
                "$COLUMN_RESERVATION_TIME TEXT, " +
                "$COLUMN_RESERVATION_PEOPLE INTEGER, " +
                "$COLUMN_RESERVATION_SPECIAL_EVENT TEXT)") // Add new column
        db.execSQL(createReservationsTable)
        db.execSQL("CREATE INDEX idx_item_id ON $TABLE_ITEMS($COLUMN_ITEM_ID)")
        db.execSQL("CREATE INDEX idx_reservation_id ON $TABLE_RESERVATIONS($COLUMN_RESERVATION_ID)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ITEMS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RESERVATIONS")
        onCreate(db)
    }
    fun insertItem(item: Item) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            val values = ContentValues().apply {
                put(COLUMN_ITEM_IMG, item.img)
                put(COLUMN_ITEM_NAME, item.name)
                put(COLUMN_ITEM_PRICE, item.price)

            }
            db.insert(TABLE_ITEMS, null, values)
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun updateItem(item: Item) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            val values = ContentValues().apply {
                put(COLUMN_ITEM_IMG, item.img)
                put(COLUMN_ITEM_NAME, item.name)
                put(COLUMN_ITEM_PRICE, item.price)
            }
            db.update(TABLE_ITEMS, values, "$COLUMN_ITEM_ID = ?", arrayOf(item.id.toString()))
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun deleteItem(itemId: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_ITEMS, "$COLUMN_ITEM_ID = ?", arrayOf(itemId.toString()))
        db.close()
        return result > 0
    }

    fun getAllItems(): List<Item> {
        val itemList = mutableListOf<Item>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_ITEMS,
            arrayOf(COLUMN_ITEM_ID, COLUMN_ITEM_IMG, COLUMN_ITEM_NAME, COLUMN_ITEM_PRICE),
            null, null, null, null, null
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_ITEM_ID))
                val img = getBlob(getColumnIndexOrThrow(COLUMN_ITEM_IMG))
                val name = getString(getColumnIndexOrThrow(COLUMN_ITEM_NAME))
                val price = getIntOrNull(getColumnIndexOrThrow(COLUMN_ITEM_PRICE))
                itemList.add(Item(id, name, price, img))
            }
        }
        cursor.close()
        db.close()
        return itemList
    }

    // Reservation operations
    fun insertReservation(reservation: Reservation) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            val values = ContentValues().apply {
                put(COLUMN_RESERVATION_NAME, reservation.name)
                put(COLUMN_RESERVATION_DATE, reservation.date)
                put(COLUMN_RESERVATION_TIME, reservation.time)
                put(COLUMN_RESERVATION_PEOPLE, reservation.people)
                put(COLUMN_RESERVATION_SPECIAL_EVENT, reservation.specialEvent) // Add special event
            }
            db.insert(TABLE_RESERVATIONS, null, values)
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun updateReservation(reservation: Reservation) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            val values = ContentValues().apply {
                put(COLUMN_RESERVATION_NAME, reservation.name)
                put(COLUMN_RESERVATION_DATE, reservation.date)
                put(COLUMN_RESERVATION_TIME, reservation.time)
                put(COLUMN_RESERVATION_PEOPLE, reservation.people)
                put(COLUMN_RESERVATION_SPECIAL_EVENT, reservation.specialEvent) // Update special event
            }
            db.update(TABLE_RESERVATIONS, values, "$COLUMN_RESERVATION_ID = ?", arrayOf(reservation.id.toString()))
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun deleteReservation(reservationId: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_RESERVATIONS, "$COLUMN_RESERVATION_ID = ?", arrayOf(reservationId.toString()))
        db.close()
        return result > 0
    }

    fun getAllReservations(): List<Reservation> {
        val reservationList = mutableListOf<Reservation>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_RESERVATIONS,
            arrayOf(COLUMN_RESERVATION_ID, COLUMN_RESERVATION_NAME, COLUMN_RESERVATION_DATE, COLUMN_RESERVATION_TIME, COLUMN_RESERVATION_PEOPLE, COLUMN_RESERVATION_SPECIAL_EVENT),
            null, null, null, null, null
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_RESERVATION_ID))
                val name = getString(getColumnIndexOrThrow(COLUMN_RESERVATION_NAME))
                val date = getString(getColumnIndexOrThrow(COLUMN_RESERVATION_DATE))
                val time = getString(getColumnIndexOrThrow(COLUMN_RESERVATION_TIME))
                val people = getInt(getColumnIndexOrThrow(COLUMN_RESERVATION_PEOPLE))
                val specialEvent = getString(getColumnIndexOrThrow(COLUMN_RESERVATION_SPECIAL_EVENT)) // Read special event
                reservationList.add(Reservation(id, name, date, time, people, specialEvent))
            }
        }
        cursor.close()
        db.close()
        return reservationList
    }

    fun getReservationById(id: Int): Reservation? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_RESERVATIONS, null, "$COLUMN_RESERVATION_ID = ?", arrayOf(id.toString()), null, null, null
        )
        val reservation = if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_NAME))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_DATE))
            val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_TIME))
            val people = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_PEOPLE))
            val specialEvent = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESERVATION_SPECIAL_EVENT)) // Read special event
            Reservation(id, name, date, time, people, specialEvent)
        } else null
        cursor.close()
        db.close()
        return reservation
    }
}
