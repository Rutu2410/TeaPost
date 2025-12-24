package com.example.teapost

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class UpdateDialogFragment (
    private val item: Item,
    private val onUpdate: (Item) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.update_dialog, null)

        val imageView: ImageView = view.findViewById(R.id.dialog_image)
        val nameEditText: EditText = view.findViewById(R.id.dialog_name)
        val priceEditText: EditText = view.findViewById(R.id.dialog_price)
        val saveButton: Button = view.findViewById(R.id.dialog_save)

        // Load item data into the dialog
        if (item.img != null) {
            val bmp = BitmapFactory.decodeStream(ByteArrayInputStream(item.img))
            imageView.setImageBitmap(bmp)
        }
        nameEditText.setText(item.name)
        priceEditText.setText(item.price.toString())

        saveButton.setOnClickListener {
            val updatedName = nameEditText.text.toString()
            val updatedPrice = priceEditText.text.toString().toInt()
            val updatedImg = imageView.drawable?.let {
                val stream = ByteArrayOutputStream()
                (it as android.graphics.drawable.BitmapDrawable).bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream)
                stream.toByteArray()
            }

            val updatedItem = Item(item.id, updatedName, updatedPrice, updatedImg)
            onUpdate(updatedItem)
            dismiss()
        }

        builder.setView(view)
        return builder.create()
    }
}
