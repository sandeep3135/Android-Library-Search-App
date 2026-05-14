package com.example.libraryapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    // 1. Declare the variables
    private lateinit var adapter: BookAdapter

    // This is your "Database" of books
    // CHANGE 1: Use mutableListOf so we can add items
    private val fullBookList = mutableListOf(
        Book("Kotlin Basics", "JetBrains"),
        Book("Android Development", "Google"),
        Book("SEO Strategy 2026", "Sandeep"),
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
        loadBooks()

        // CHANGE 2: Link the new UI elements
        val etNewTitle: EditText = findViewById(R.id.etNewTitle)
        val etNewAuthor: EditText = findViewById(R.id.etNewAuthor)
        val btnAddBook: Button = findViewById(R.id.btnAddBook)

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

        // CHANGE 3: The Add Book Logic
        btnAddBook.setOnClickListener {
            val title = etNewTitle.text.toString().trim()
            val author = etNewAuthor.text.toString().trim()

            if (title.isNotEmpty() && author.isNotEmpty()) {
                // Add new book to the list
                fullBookList.add(Book(title, author))

                // Refresh the adapter to show the new item
                adapter.updateList(fullBookList)

                // Clear inputs for the next entry
                etNewTitle.text.clear()
                etNewAuthor.text.clear()
                saveBooks()
            }
        }
        // 4. Setup the Search logic
        btnSearch.setOnClickListener {
            val query = etSearch.text.toString().lowercase().trim()

            // Filter the full list based on what the user typed
            val filteredList = fullBookList.filter {
                it.title.lowercase().contains(query) || it.author.lowercase().contains(query)
            }

            // Update the adapter to show only the filtered results
            adapter.updateList(filteredList)
        }
    }

    private fun saveBooks() {
        val sharedPreferences = getSharedPreferences("LibraryPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()

        // Convert our list into a JSON string
        val json = gson.toJson(fullBookList)

        // Save the string to the phone's memory
        editor.putString("book_list", json)
        editor.apply()
    }

    private fun loadBooks() {
        val sharedPreferences = getSharedPreferences("LibraryPrefs", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("book_list", null)

        if (json != null) {
            // This complex line tells Gson exactly how to turn text back into a List of Books
            val type = object : TypeToken<MutableList<Book>>() {}.type
            val savedList: MutableList<Book> = gson.fromJson(json, type)

            fullBookList.clear()
            fullBookList.addAll(savedList)
        }
    }
}