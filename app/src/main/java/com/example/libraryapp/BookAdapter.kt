package com.example.libraryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// This class connects your list of books to the screen
class BookAdapter(private var bookList: List<Book>) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    // This "ViewHolder" holds the references to the views in item_book.xml
    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvAuthor: TextView = view.findViewById(R.id.tvAuthor)
    }

    // 1. onCreateViewHolder: Tells Android which layout to use for each row
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    // 2. onBindViewHolder: "Glues" the data (from Book.kt) into the views (item_book.xml)
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentBook = bookList[position]
        holder.tvTitle.text = currentBook.title
        holder.tvAuthor.text = currentBook.author
    }

    // 3. Tells the RecyclerView how many items are in the list
    override fun getItemCount(): Int = bookList.size

    // This special function helps us update the list when we search!
    fun updateList(newList: List<Book>) {
        this.bookList = newList
        notifyDataSetChanged() // Refreshes the screen
    }
}