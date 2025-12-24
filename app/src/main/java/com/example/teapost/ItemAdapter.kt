package com.example.teapost

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import java.io.ByteArrayInputStream

class ItemAdapter(
    private val context: Context,
    private val items: List<Item>,
    private val onItemDelete: (Int) -> Unit,
    private val onItemUpdate: (Item) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_image)
        val nameTextView: TextView = itemView.findViewById(R.id.item_name)
        val priceTextView: TextView = itemView.findViewById(R.id.item_price)
        val deleteButton: ImageView = itemView.findViewById(R.id.item_delete)
        val updateButton: ImageView = itemView.findViewById(R.id.item_update)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        val bmp = item.img?.let { BitmapFactory.decodeStream(ByteArrayInputStream(it)) }
        holder.imageView.setImageBitmap(bmp)
        holder.nameTextView.text = item.name
        holder.priceTextView.text = item.price.toString()

        holder.deleteButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes") { _, _ ->
                    onItemDelete(item.id)
                }
                .setNegativeButton("No", null)
                .show()
        }

        holder.updateButton.setOnClickListener {
            val dialog = UpdateDialogFragment(item) { updatedItem ->
                onItemUpdate(updatedItem)
            }
            dialog.show((context as AppCompatActivity).supportFragmentManager, "UpdateDialog")
        }
    }

    override fun getItemCount(): Int = items.size
}
