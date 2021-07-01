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
import com.prikshitkumar.bookhub.model.Book
import com.squareup.picasso.Picasso

class DashboardAdapter(val context: Context, val itemList: ArrayList<Book>) : RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
    /* If we have 100 items to show in the list. Then, this method shown the number of items that much fits in the user screen and uses
    *  those view for next items. All this work is handled in this method */

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclarview_items_ui, parent, false)
        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        /* This method is responsible for the recycling and reusing of the ViewHolders. This method also takes care of data goes to the
         * correct position. */

        val book= itemList[position]
        holder.bookName.text = book.book_Name
        holder.bookAuthor.text = book.author_Name
        holder.bookPrice.text = book.book_Price
        holder.bookRating.text = book.book_Rating
//        holder.book_Icon.setImageResource(book.book_Icon)
        /* To load the Image from the URL we have to use the library "PICASSO" This library is used to populate the Image to ImageView
         * from the link. */
        Picasso.get().load(book.book_Icon).into(holder.bookIcon)

        holder.itemUIContent.setOnClickListener {
            val intent = Intent(context, Description::class.java)
            intent.putExtra("book_id", book.book_ID)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        /* This method stores the total number of items of the List */
        return itemList.size
    }

    class DashboardViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val bookName: TextView = view.findViewById(R.id.id_bookTitle)
        val bookAuthor: TextView = view.findViewById(R.id.id_bookAuthor)
        val bookRating: TextView = view.findViewById(R.id.id_bookRating)
        val bookPrice: TextView = view.findViewById(R.id.id_bookPrice)
        val bookIcon: ImageView = view.findViewById(R.id.id_bookIcon)
        val itemUIContent: LinearLayout = view.findViewById(R.id.id_item_UI_LinearLayout)
    }
}