package com.prikshitkumar.bookhub.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.prikshitkumar.bookhub.R
import com.prikshitkumar.bookhub.adapters.DashboardAdapter
import com.prikshitkumar.bookhub.model.Book
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

class Dashboard : Fragment() {

    /* For working with RecyclerView we have to take care of three things:
     *  1) LayoutManager  (It manages the arrangement of List Items)
     *  2) ViewHolder    (It holds the View of list Items and use the reference of these view for all other items of List)
     *  3) Adapter       (It bind the data with View or Items of the List and Adapter holds the ViewHolder for this binding work. ViewHolder
                           is also a class)
    */

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerViewAdapter: DashboardAdapter
    lateinit var progressBarLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    var ratingComparator = Comparator<Book>{ book1, book2 ->
        if(book1.book_Rating.compareTo(book2.book_Rating, true) == 0) { // means if the value of both the object is same.
            book1.book_Name.compareTo(book2.book_Name, true)
        }
        else {
            book1.book_Rating.compareTo(book2.book_Rating, true)
        }
         /* In case of Strings for ignoring the cases (uppercase / lowercase), 2nd attribute (ignoreCase) is used. */
    } /* For Comparison of two objects/Strings/.. we don't have to write the code by ourself. Because Kotlin provide us the class called
       * "Comparator" which performs all the comparison operations and return the result. */


    val bookInfoList = arrayListOf<Book>(
        /*  Book("Harry Porter","Prikshit Kumar", "Rs. 198", "4.9", R.drawable.header_icon),
            Book("Rich Dad Poor Dad","Prince Dhiman", "Rs. 200", "4.5", R.drawable.header_icon),
            Book("Believing in Yourself","Jai Kumar", "Rs. 159", "4.1", R.drawable.header_icon),
            Book("Quauntum Physics","Nitish Dhiman", "Rs. 56", "3.9", R.drawable.header_icon)
         */
    )


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        // "attachToRoot" is "False" because we don't want to attach the View with Activity Permanently.

        setHasOptionsMenu(true)     /* Now we have to tell the Compiler that this toolbar has the menu file inside it. For that we have to use the
        method called: "setHasOptionsMenu" */

        recyclerView = view.findViewById(R.id.id_recyclerView_Dashboard)
        layoutManager = LinearLayoutManager(activity)

        progressBarLayout = view.findViewById(R.id.id_progressBarLayout)
        progressBar = view.findViewById(R.id.id_progressBar)
        progressBarLayout.visibility = View.VISIBLE

        /* Volley is an HTTP library that makes networking for Android apps easier and, most importantly, faster.
        *  We can request to fetch the data or store the data in Server using Volley Library easily.
        *  Volley Library is best for Developers for networking Operations..
        *
        *  Some another methods for fetching and sending the data to Server are:
        *  1) RetroFit (Library)
        *  2) OKHTTP (Library)
        *  3) HttpURLConnection (Method)
        *
        *  We can also specify the form of response of the Server like pdf, String, JSON, etc.. And, this format is specified in the headers
        *  method.
        *  To load the Image from the URL we have to use the library "PICASSO". This library is used to populate the Image to ImageView from
        *  the link.
        */

        val queue = Volley.newRequestQueue(activity as Context)  // This variable holds the Responses from API
        val url = "http://13.235.250.119/v1/book/fetch_books/"

        try {
            progressBarLayout.visibility = View.GONE

            val jsonRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {
            /* Here we will handle the Response from Server/API
             * Now parse the JSON File and put the values in ArrayList that we had created for Book and then send this ArrayList to the
             * Adapter */
                    val success = it.getBoolean("success")
                    if (success) {
                        val data = it.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val bookJSONObject = data.getJSONObject(i)
                            val bookObject = Book(
                                bookJSONObject.getString("book_id"),
                                bookJSONObject.getString("name"),
                                bookJSONObject.getString("author"),
                                bookJSONObject.getString("rating"),
                                bookJSONObject.getString("price"),
                                bookJSONObject.getString("image"),
                            )
                            bookInfoList.add(bookObject)
                            recyclerViewAdapter = DashboardAdapter(
                                activity as Context,
                                bookInfoList
                            ) // "as" keyword is used for Type-Casting in Kotlin

                            recyclerView.adapter = recyclerViewAdapter
                            recyclerView.layoutManager = layoutManager

                // RecyclerView have the method addItemDecoration for decorate the items such as adding divider between the items, etc...
                            recyclerView.addItemDecoration(
                                DividerItemDecoration(
                                    recyclerView.context,
                                    (layoutManager as LinearLayoutManager).orientation
                                )
                            )
                        }
                    }
                    else {
                        Toast.makeText(context, "Couldn't load the data", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener {
                    // Here we will handle the Errors
                    if(activity != null) {
                        Toast.makeText(activity as Context, "Volley Error Occurred!!", Toast.LENGTH_SHORT).show()
                    }
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>() // HashMap is the subclass of MutableMap Class. So, we can also return this.
                    headers["content_Type"] = "application/JSON"  // Specify the datatype of sending and receiving form of data to/from API.
                    headers["token"] = "9bf534118365f1" // Unique Token (anything but remember this).
                    return headers  // header is sent to the API.
                }
            }
            queue.add(jsonRequest)
        }
        catch(e: JSONException){
            Toast.makeText(activity as Context, "JSON Exception, might be Server Side Issue!!", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) { //menu variable holds the menu xml file that we created
        inflater.inflate(R.menu.menu_dashboard, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.id_actionSort) {
            Collections.sort(bookInfoList, ratingComparator)  /* This method will sort the items according to ratingComparator in ascending
                order and store the result in bookInfoList. */
            bookInfoList.reverse()
        }
        recyclerViewAdapter.notifyDataSetChanged() /* This is for notify the Recycler View Adapter that we change (sort) the data.. */

        return super.onOptionsItemSelected(item)
    }
}