package com.prikshitkumar.bookhub.activity

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.prikshitkumar.bookhub.R
import com.prikshitkumar.bookhub.database.BookDatabase
import com.prikshitkumar.bookhub.database.BookEntity
import com.squareup.picasso.Picasso
import org.json.JSONObject

class Description : AppCompatActivity() {

    lateinit var bookName: TextView
    lateinit var bookAuthor: TextView
    lateinit var bookPrice: TextView
    lateinit var bookRating: TextView
    lateinit var bookIcon: ImageView
    lateinit var bookDescription: TextView
    lateinit var addToFavorite: Button
    lateinit var progressBar: ProgressBar
    lateinit var progressBarLayout: RelativeLayout
    lateinit var toolbar: Toolbar

    var bookId: String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        initializeTheFields()
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(intent != null) {
            bookId = intent.getStringExtra("book_id")
        }
        else {
            Toast.makeText(this@Description, "Some Unexpected Error Occurred!!", Toast.LENGTH_SHORT).show()
            finish()
        }
        if(bookId == "100") {
            Toast.makeText(this@Description, "Some Unexpected Error Occurred!!", Toast.LENGTH_SHORT).show()
            finish()
        }

        val queue = Volley.newRequestQueue(this@Description)
        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()  /* If we want to send the data to the Server, then it is also in the form of String/JSON/etc. Here
         we are using JSONObject. jsonParams variable hold the values like credentials (Email, Password, etc) for fetching the required data
         from Server. */
        jsonParams.put("book_id", bookId)

        val jsonRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
            try {
                val success = it.getBoolean("success")
                if(success) {
                    progressBarLayout.visibility = View.GONE
                    val bookJSONObject = it.getJSONObject("book_data")
                    val bookIconUrl = bookJSONObject.getString("image") // For adding this url to local database instead of image.

                    Picasso.get().load(bookJSONObject.getString("image")).into(bookIcon)
                    bookName.text = bookJSONObject.getString("name")
                    bookAuthor.text = bookJSONObject.getString("author")
                    bookPrice.text = bookJSONObject.getString("price")
                    bookRating.text = bookJSONObject.getString("rating")
                    bookDescription.text = bookJSONObject.getString("description")

                    val bookEntity = BookEntity (
                        bookId?.toInt() as Int,
                        bookName.text.toString(),
                        bookAuthor.text.toString(),
                        bookRating.text.toString(),
                        bookPrice.text.toString(),
                        bookIconUrl
                    )

                    val checkFavorite = DBAsyncTask(applicationContext, bookEntity, 1).execute() // execute() is for start the binded action
                    val isFavorite = checkFavorite.get() /* get() method will return the result of background process, whether the actions is
                    successfully executed or not and returns the Boolean value.. */
                    if(isFavorite) {
                        addToFavorite.text = "Remove from Favorites"
                        val favColor = ContextCompat.getColor(applicationContext, R.color.orange)
                        addToFavorite.setBackgroundColor(favColor)
                    }
                    else {
                        addToFavorite.text = "Add to Favorites"
                        val favColor = ContextCompat.getColor(applicationContext, R.color.header_color)
                        addToFavorite.setBackgroundColor(favColor)
                    }

                    addToFavorite.setOnClickListener {
                        if(!DBAsyncTask(applicationContext, bookEntity, 1).execute().get()) {
                            val async = DBAsyncTask(applicationContext, bookEntity, 2).execute()
                            val result = async.get()
                            if(result) {
                                Toast.makeText(this@Description, "Book added to Favorites", Toast.LENGTH_SHORT).show()
                                addToFavorite.text = "Remove from Favorites"
                                val favColor = ContextCompat.getColor(applicationContext, R.color.orange)
                                addToFavorite.setBackgroundColor(favColor)
                            }
                            else {
                                Toast.makeText(this@Description, "Some Error Ocurred!!", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else {
                            val async = DBAsyncTask(applicationContext, bookEntity, 3).execute()
                            val result = async.get()
                            if(result){
                                Toast.makeText(this@Description, "Book removed from Favorites", Toast.LENGTH_SHORT).show()
                                addToFavorite.text = "Add to Favorites"
                                val favColor = ContextCompat.getColor(applicationContext, R.color.header_color)
                                addToFavorite.setBackgroundColor(favColor)
                            }
                            else {
                                Toast.makeText(this@Description, "Some Error Ocurred!!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                }
                else {
                    Toast.makeText(this@Description, "Couldn't load the data", Toast.LENGTH_SHORT).show()
                }
            }
            catch(e: Exception) {
                Toast.makeText(this@Description, "Some Error Occurred", Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener {
            Toast.makeText(this@Description, "Volley Error Occurred!!  $it", Toast.LENGTH_SHORT).show()

        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["token"] = "9bf534118365f1"
                return headers
            }
        }
        queue.add(jsonRequest)
    }
    

    fun initializeTheFields() {
        bookName = findViewById(R.id.id_descriptionBookTitle)
        bookAuthor = findViewById(R.id.id_descriptionBookAuthor)
        bookPrice = findViewById(R.id.id_descriptionBookPrice)
        bookRating = findViewById(R.id.id_descriptionBookRating)
        bookIcon = findViewById(R.id.id_descriptionBookIcon)
        bookDescription = findViewById(R.id.id_descriptionAboutTheBook)
        addToFavorite = findViewById(R.id.id_addToFavorite)
        progressBar = findViewById(R.id.id_descriptionProgressBar)
        progressBarLayout = findViewById(R.id.id_descriptionProgressBarLayout)
        toolbar = findViewById(R.id.id_descriptionToolbar)

        progressBarLayout.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    /* Description class is already inherited the AppCompatActivity class. And, in kotlin we can not inherit more than one class. That's why
     * we declare and initialize the class here (Nested class).
     * As we know AsyncTask needs the three parameters: 1) params, 2) Progress, 3) Results. But we only have to know that whether the applied
     * is successfully completed or not. So, we don't have to pass the 1st two values to the attributes.
     * AsyncTask has 4 methods:
     * 1) onPreExecute(), 2) doInBackground(), 3) onProgressUpdate(), 4) onPostExecute(). Read the documentation of all these methods.
     * doInBackground() method is mandatory to implement.
     *
     * Whenever we are using this AsyncTask class, we have to initialize the database class. And, if we want to perform database operations,
     * then we need the context, that tells which part of the app made the request for accessing the resources. Also, these operations are
     * used either for save the book to favorites or delete the book from favorites. Means, this class will also need one more parameter for
     * BookEntity.
     * This class "DBAsyncTask" needs to perform three operations:
     * 1) Check the database to see if the book is already added to favorites or not.
     * 2) Add a book to the favorites.
     * 3) Remove a book from the favorites. */

    class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode: Int) : AsyncTask<Void, Void, Boolean>() {

        /* Mode 1 -> Check DB if the book is in favorite or not.
         * Mode 2 -> Save the book into DB as favorite.
         * Mode 3 -> Remove the book from favorite. */

        val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build() // Initialization of Database
        /* databaseBuilder method needs three parameters.
         * 1st: context, 2nd: Java version of the class which is responsible for create the database, 3rd: Name of the Database.
         * build() is to build the database properly.
         * Note: It is always mandatory to close the database after performing the operations. Otherwise it takes the unnecessary memory. */

        override fun doInBackground(vararg params: Void?): Boolean {

            when(mode) {
                1 -> {
        // Check DB if the book is in favorite or not. For it, we need to check the id of the book whether that is present in database or not.
                    val book: BookEntity? = db.bookDao().getBookById(bookEntity.book_ID.toString())
                    db.close()
                    return book != null //it returns true of value of book is not null, otherwise returns false, if the value is null.
                }
                2 -> {
                    // Save the book into DB as favorite.
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }
                3 -> {
                    // Remove the book from favorite.
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }
            return false
        }

    }
}