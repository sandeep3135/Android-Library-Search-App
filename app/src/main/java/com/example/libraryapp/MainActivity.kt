package com.example.libraryapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// This is our "Data Model"
data class Book(val title: String, val author: String)

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // STEP 1: Connect the UI elements to Kotlin (MUST BE FIRST)
        val bookInput = findViewById<EditText>(R.id.bookInput)
        val searchBtn = findViewById<Button>(R.id.searchBtn)
        // 1. Find the clear button
        val clearBtn = findViewById<Button>(R.id.clearBtn)
        val resultTxt = findViewById<TextView>(R.id.resultTxt)

        // STEP 2: Create your data list
        val libraryBooks = listOf(
            Book("Half Girlfriend", "Chetan Bhagat"),
            Book("The Alchemist", "Paulo Coelho"),
            Book("Rich Dad Poor Dad", "Robert Kiyosaki")
        )

        // STEP 3: Add the "Click" logic
        // 1. Search Button Logic
        searchBtn.setOnClickListener {
            // .trim() removes any accidental spaces at the start or end
            val userText = bookInput.text.toString().trim()
            //Validation: Added a check to make sure the user didn't just hit "Search" with an empty box.
            if (userText.isEmpty()) {
                bookInput.error = "Please enter a name"
                return@setOnClickListener
            }

            // Search logic: Check if userText matches TITLE or AUTHOR
            val foundBook = libraryBooks.find {
                it.title.equals(userText, ignoreCase = true) ||
                        it.author.equals(userText, ignoreCase = true)
            }

            // Display the result
            if (foundBook != null) {
                resultTxt.text = "Found: ${foundBook.title} \nBy: ${foundBook.author}"
            } else {
                resultTxt.text = "No book or author found matching '$userText'"
            }
        }

// 2. Clear Button Logic (Moved OUTSIDE the search click listener)
        clearBtn.setOnClickListener {
            bookInput.setText("")
            resultTxt.text = "Results cleared"
        }
    }
}