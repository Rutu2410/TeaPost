package com.example.teapost

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException

class Addmenu : AppCompatActivity() {
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_PHOTO = 2

    private lateinit var imageView: ImageView
    private lateinit var itemNameEditText: EditText
    private lateinit var itemPriceEditText: EditText

    private val db by lazy { ItemDatabaseHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_menu)

        imageView = findViewById(R.id.i1)
        itemNameEditText = findViewById(R.id.t1)
        itemPriceEditText = findViewById(R.id.t2)
        val saveButton: ImageButton = findViewById(R.id.save)

        imageView.setOnClickListener {
            // Show options to capture or select image
            val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("Add Photo")
            builder.setItems(options) { _, which ->
                when (which) {
                    0 -> dispatchTakePictureIntent()
                    1 -> dispatchPickPhotoIntent()
                    2 -> {} // Cancel
                }
            }
            builder.show()
        }

        saveButton.setOnClickListener {
            if (saveItemToDatabase()) {
                Toast.makeText(this, "Item saved successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to save item.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun dispatchPickPhotoIntent() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, REQUEST_PICK_PHOTO)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    imageBitmap?.let {
                        imageView.setImageBitmap(it)
                    }
                }
                REQUEST_PICK_PHOTO -> {
                    val imageUri: Uri? = data?.data
                    imageUri?.let {
                        loadBitmapFromUri(it)
                    }
                }
            }
        }
    }

    private fun loadBitmapFromUri(uri: Uri) {
        lifecycleScope.launch {
            try {
                val bitmap = withContext(Dispatchers.IO) {
                    contentResolver.openInputStream(uri)?.use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)
                    }
                }
                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this@Addmenu, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveItemToDatabase(): Boolean {
        val name = itemNameEditText.text.toString()
        val price = itemPriceEditText.text.toString().toIntOrNull() ?: 0
        val bitmap = (imageView.drawable as? BitmapDrawable)?.bitmap
        val imgByteArray = bitmap?.let { getBitmapAsByteArray(it) }

        val item = Item(id = 0, img = imgByteArray, name = name, price = price.toInt())
        return try {
            lifecycleScope.launch(Dispatchers.IO) {
                db.insertItem(item)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun getBitmapAsByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }
}
