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

        // 3. Setup the RecyclerView
        rvBooks.layoutManager = LinearLayoutManager(this)

        // Connect the Adapter to the RecyclerView
        adapter = BookAdapter(fullBookList)
        rvBooks.adapter = adapter

        // Set the click listener to open the Pop-up Dialog
        fabAddBook.setOnClickListener {
            showAddBookDialog()
        }

        // 4. Setup the Search logic
        etSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().lowercase().trim()
                val filteredList = fullBookList.filter {
                    it.title.lowercase().contains(query) || it.author.lowercase().contains(query)
                }
                adapter.updateList(filteredList)
            }

            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        // 5. ADDED GESTURE LOGIC: Swipe-to-Delete with Confirmation Box
        val swipeHandler = object : androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(
            0,
            androidx.recyclerview.widget.ItemTouchHelper.LEFT or androidx.recyclerview.widget.ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                // Read the actual list currently visible inside the Adapter
                val currentList = adapter.getBookList()
                val bookToDelete = currentList[position]

                // Show the explicit confirmation pop-up dialog
                val builder = androidx.appcompat.app.AlertDialog.Builder(this@MainActivity)
                builder.setTitle("Delete Book")
                builder.setMessage("Are you sure you want to remove '${bookToDelete.title}'?")
                builder.setCancelable(false) // Force user to use a button

                // If user confirms delete action
                builder.setPositiveButton("Delete") { _, _ ->
                    fullBookList.remove(bookToDelete) // Delete by object identity, not raw position
                    saveBooks()

                    // Re-filter the UI list using current search input
                    val query = etSearch.text.toString().lowercase().trim()
                    val updatedFilteredList = fullBookList.filter {
                        it.title.lowercase().contains(query) || it.author.lowercase().contains(query)
                    }
                    adapter.updateList(updatedFilteredList)
                    checkEmptyState()

                    android.widget.Toast.makeText(this@MainActivity, "Book removed", android.widget.Toast.LENGTH_SHORT).show()
                }

                // If user clicks cancel, push the item card right back to center screen
                builder.setNegativeButton("Cancel") { dialog, _ ->
                    adapter.notifyItemChanged(position)
                    dialog.dismiss()
                }

                builder.show()
            }
        }

        // 6. Setup Light/Dark Mode Toggle
        val btnThemeToggle: android.widget.ImageButton = findViewById(R.id.btnThemeToggle)

        btnThemeToggle.setOnClickListener {
            // Check what theme the app is currently running
            val currentMode = androidx.appcompat.app.AppCompatDelegate.getDefaultNightMode()

            if (currentMode == androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES) {
                // If it's dark, switch to light mode
                androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(
                    androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
                )
                android.widget.Toast.makeText(this, "Light Mode Activated", android.widget.Toast.LENGTH_SHORT).show()
            } else {
                // If it's light/default, switch to dark mode
                androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(
                    androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
                )
                android.widget.Toast.makeText(this, "Dark Mode Activated", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        // Attach our new controller layout to the RecyclerView frame
        val itemTouchHelper = androidx.recyclerview.widget.ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rvBooks)
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

                // LOGIC: Check if this exact Title AND Author already exist (ignoring Case)
                val isDuplicate = fullBookList.any {
                    it.title.equals(title, ignoreCase = true) &&
                            it.author.equals(author, ignoreCase = true)
                }

                if (isDuplicate) {
                    // If it's a match, show a warning and do NOT add
                    android.widget.Toast.makeText(this, "This book & author already exist!", android.widget.Toast.LENGTH_SHORT).show()
                } else {
                    // If it's unique, add it as usual
                    fullBookList.add(Book(title, author))
                    saveBooks()
                    adapter.updateList(fullBookList)
                    checkEmptyState()
                }
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
    // Updated: Accept currentList parameter so it works perfectly during search sessions
    fun showDeleteDialog(position: Int, currentList: List<Book>) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        val bookToDelete = currentList[position]

        builder.setTitle("Delete Book")
        builder.setMessage("Do you want to remove '${bookToDelete.title}' from your library?")

        builder.setPositiveButton("Delete") { _, _ ->
            fullBookList.remove(bookToDelete) // Delete by object matching
            saveBooks()

            // Refresh layout using whatever text is in search input field
            val etSearch: EditText = findViewById(R.id.etSearch)
            val query = etSearch.text.toString().lowercase().trim()
            val updatedList = fullBookList.filter {
                it.title.lowercase().contains(query) || it.author.lowercase().contains(query)
            }
            adapter.updateList(updatedList)
            checkEmptyState()

            android.widget.Toast.makeText(this, "Book removed", android.widget.Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
// this function for updating the book list
// Updated: Pre-fills and edits data based on the accurate current list view
fun showEditBookDialog(position: Int, currentList: List<Book>) {
    val builder = androidx.appcompat.app.AlertDialog.Builder(this)
    val bookToEdit = currentList[position]
    builder.setTitle("Edit Book")

    val layout = android.widget.LinearLayout(this)
    layout.orientation = android.widget.LinearLayout.VERTICAL
    layout.setPadding(60, 40, 60, 10)

    val inputTitle = android.widget.EditText(this)
    inputTitle.setText(bookToEdit.title) // Pre-fill accurately
    layout.addView(inputTitle)

    val inputAuthor = android.widget.EditText(this)
    inputAuthor.setText(bookToEdit.author) // Pre-fill accurately
    layout.addView(inputAuthor)

    builder.setView(layout)

    builder.setPositiveButton("Update") { _, _ ->
        val newTitle = inputTitle.text.toString().trim()
        val newAuthor = inputAuthor.text.toString().trim()

        if (newTitle.isNotEmpty() && newAuthor.isNotEmpty()) {
            // Locate where the book sits inside master array database index
            val masterIndex = fullBookList.indexOf(bookToEdit)
            if (masterIndex != -1) {
                fullBookList[masterIndex] = Book(newTitle, newAuthor)
                saveBooks()

                // Refresh view layout
                val etSearch: EditText = findViewById(R.id.etSearch)
                val query = etSearch.text.toString().lowercase().trim()
                val updatedList = fullBookList.filter {
                    it.title.lowercase().contains(query) || it.author.lowercase().contains(query)
                }
                adapter.updateList(updatedList)
            }
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
            //android.widget.Toast.makeText(this, "List is EMPTY!", android.widget.Toast.LENGTH_SHORT).show()
        } else {
            emptyStateContainer.visibility = android.view.View.GONE
        }
    }
}