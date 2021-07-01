 package com.prikshitkumar.bookhub.fragments

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.prikshitkumar.bookhub.R
import com.prikshitkumar.bookhub.activity.Description
import com.prikshitkumar.bookhub.adapters.FavoriteAdapter
import com.prikshitkumar.bookhub.database.BookDatabase
import com.prikshitkumar.bookhub.database.BookEntity

class Favorite : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerViewAdapter: FavoriteAdapter
    lateinit var progressBarLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    var dbBookList = listOf<BookEntity>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        recyclerView = view.findViewById(R.id.id_Favorite_RecyclerView)
        layoutManager = GridLayoutManager(activity as Context, 2) /* Last time we use the LinearLayoutManager. Here we are using
        GridLayoutManager. And spanCount= 2 means number of items in each row.. */

        progressBarLayout = view.findViewById(R.id.id_favoriteProgressBarLayout)
        progressBar = view.findViewById(R.id.id_favoriteProgressBar)
        progressBarLayout.visibility = View.VISIBLE

        dbBookList = RetrieveFavorites(activity as Context).execute().get()
        if(activity != null) {
            progressBarLayout.visibility = View.GONE
            recyclerViewAdapter = FavoriteAdapter(activity as Context, dbBookList)
            recyclerView.adapter = recyclerViewAdapter
            recyclerView.layoutManager = layoutManager
        }

        return view
    }

    /* Before initialize the Adapter we have to retrieve the list of books from database. Since we created Dao for retrieving all the data, returns
    * the list of bookEntity. So, the return type for doInBackground will the bookEntity */
    class RetrieveFavorites(val context: Context) : AsyncTask<Void, Void, List<BookEntity>>(){
        override fun doInBackground(vararg params: Void?): List<BookEntity> {
            val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()

            return db.bookDao().getAllBooks()
        }

    }

}