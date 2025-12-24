package com.example.teapost

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MENU : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: ItemAdapter
    private val db by lazy { ItemDatabaseHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load items from the database
        loadItemsFromDatabase()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_add -> {
                    // Start Addmenu activity
                    startActivity(Intent(this, Addmenu::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun loadItemsFromDatabase() {
        lifecycleScope.launch {
            val items = withContext(Dispatchers.IO) { db.getAllItems() }
            itemAdapter = ItemAdapter(this@MENU, items, { itemId ->
                lifecycleScope.launch {
                    val deleted = withContext(Dispatchers.IO) { db.deleteItem(itemId) }
                    if (deleted) {
                        loadItemsFromDatabase() // Reload items
                    }
                }
            }, { updatedItem ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) { db.updateItem(updatedItem) }
                    loadItemsFromDatabase() // Reload items
                }
            })

            recyclerView.adapter = itemAdapter
        }
    }
}
