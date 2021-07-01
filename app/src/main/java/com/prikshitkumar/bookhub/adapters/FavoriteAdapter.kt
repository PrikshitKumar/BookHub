package com.prikshitkumar.bookhub.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.prikshitkumar.bookhub.R
import com.prikshitkumar.bookhub.activity.Description
import com.prikshitkumar.bookhub.database.BookEntity
import com.squareup.picasso.Picasso

class FavoriteAdapter(val context: Context, val bookList: List<BookEntity>) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_favorite_single_row, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val book = bookList[position]
        holder.bookName.text = book.book_Name
        holder.bookAuthor.text = book.author_Name
        holder.bookPrice.text = book.book_Price
        holder.bookRating.text = book.book_Rating
        Picasso.get().load(book.book_Icon).into(holder.bookIcon)

        holder.itemUIContent.setOnClickListener {
            val intent = Intent(context, Description::class.java)
            intent.putExtra("book_id", book.book_ID)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookName: TextView = view.findViewById(R.id.id_favoriteBookTitle)
        val bookAuthor: TextView = view.findViewById(R.id.id_favoriteBookAuthor)
        val bookRating: TextView = view.findViewById(R.id.id_favoriteBookRating)
        val bookPrice: TextView = view.findViewById(R.id.id_favoriteBookPrice)
        val bookIcon: ImageView = view.findViewById(R.id.id_favoriteBookIcon)
        val itemUIContent: LinearLayout = view.findViewById(R.id.id_favoriteLayout)
    }
}