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
        checkEmptyState()

        // Link the new Floating Action Button
        val fabAddBook: com.google.android.material.floatingactionbutton.FloatingActionButton = findViewById(R.id.fabAddBook)

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

        // Set the click listener to open the Pop-up Dialog
        fabAddBook.setOnClickListener {
            showAddBookDialog()
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

    private fun showAddBookDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Add New Book")

        // We create a container to hold our input fields inside the pop-up
        val layout = android.widget.LinearLayout(this)
        layout.orientation = android.widget.LinearLayout.VERTICAL
        layout.setPadding(60, 40, 60, 10)

        val inputTitle = android.widget.EditText(this)
        inputTitle.hint = "Book Title"
        layout.addView(inputTitle)

        val inputAuthor = android.widget.EditText(this)
        inputAuthor.hint = "Author Name"
        layout.addView(inputAuthor)

        builder.setView(layout)

        // When the user clicks "Add"
        builder.setPositiveButton("Add") { _, _ ->
            val title = inputTitle.text.toString().trim()
            val author = inputAuthor.text.toString().trim()

            if (title.isNotEmpty() && author.isNotEmpty()) {
                fullBookList.add(Book(title, author))
                saveBooks() // Use your existing save logic
                adapter.updateList(fullBookList)
                checkEmptyState()
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
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

    // This function must be 'fun' (not private) so the Adapter can see it
    fun showDeleteDialog(position: Int) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Delete Book")
        builder.setMessage("Do you want to remove '${fullBookList[position].title}' from your library?")

        builder.setPositiveButton("Delete") { _, _ ->
            // 1. Remove the book from our list
            fullBookList.removeAt(position)

            // 2. Save the new list to SharedPreferences (Persistence!)
            saveBooks()

            // 3. Tell the adapter to refresh the UI
            adapter.updateList(fullBookList)
            checkEmptyState()

            android.widget.Toast.makeText(this, "Book removed", android.widget.Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
// this function for updating the book list
    fun showEditBookDialog(position: Int) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Edit Book")

        val layout = android.widget.LinearLayout(this)
        layout.orientation = android.widget.LinearLayout.VERTICAL
        layout.setPadding(60, 40, 60, 10)

        val inputTitle = android.widget.EditText(this)
        // PRE-FILL: Set text to the current title
        inputTitle.setText(fullBookList[position].title)
        layout.addView(inputTitle)

        val inputAuthor = android.widget.EditText(this)
        // PRE-FILL: Set text to the current author
        inputAuthor.setText(fullBookList[position].author)
        layout.addView(inputAuthor)

        builder.setView(layout)

        builder.setPositiveButton("Update") { _, _ ->
            val newTitle = inputTitle.text.toString().trim()
            val newAuthor = inputAuthor.text.toString().trim()

            if (newTitle.isNotEmpty() && newAuthor.isNotEmpty()) {
                // Update the data in the list
                fullBookList[position] = Book(newTitle, newAuthor)
                saveBooks() // Save changes
                adapter.updateList(fullBookList) // Refresh UI
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun checkEmptyState() {
        // Change TextView to View so it works with any layout type
        val emptyStateContainer: android.view.View = findViewById(R.id.tvEmptyMessage)
        if (fullBookList.isEmpty()) {
            emptyStateContainer.visibility = android.view.View.VISIBLE
            // Temporary Debug Toast:
            android.widget.Toast.makeText(this, "List is EMPTY!", android.widget.Toast.LENGTH_SHORT).show()
        } else {
            emptyStateContainer.visibility = android.view.View.GONE
        }
    }
}