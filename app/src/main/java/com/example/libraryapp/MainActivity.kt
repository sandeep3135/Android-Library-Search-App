package com.example.libraryapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    // 1. Declare the variables
    private lateinit var adapter: BookAdapter

    // This is your "Database" of books
    private val fullBookList = listOf(
        Book("Kotlin Basics", "JetBrains"),
        Book("Android Development", "Google"),
        Book("Java Fundamentals", "Oracle"),
        Book("SEO Strategy 2026", "Sandeep"),
        Book("Mastering Git", "Linus Torvalds"),
        Book("UI/UX Design", "Adobe")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 2. Initialize the views from XML
        val rvBooks: RecyclerView = findViewById(R.id.rvBooks)
        val etSearch: EditText = findViewById(R.id.etSearch)
        val btnSearch: Button = findViewById(R.id.btnSearch)

        // 3. Setup the RecyclerView
        // This tells the list to show items in a vertical column
        rvBooks.layoutManager = LinearLayoutManager(this)

        // Connect the Adapter to the RecyclerView
        adapter = BookAdapter(fullBookList)
        rvBooks.adapter = adapter

        // 4. Setup the Search logic
        btnSearch.setOnClickListener {
            val query = etSearch.text.toString().lowercase().trim()

            // Filter the full list based on what the user typed
            val filteredList = fullBookList.filter { book ->
                book.title.lowercase().contains(query) ||
                        book.author.lowercase().contains(query)
            }

            // Update the adapter to show only the filtered results
            adapter.updateList(filteredList)
        }
    }
}