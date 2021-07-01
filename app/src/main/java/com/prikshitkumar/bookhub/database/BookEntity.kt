package com.prikshitkumar.bookhub.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/* Components of ROOM PERSISTENCE LIBRARY:
 * 1) Entity (Tables) : THe tables which store the data.
 * 2) DAO (Data Access Object) : This is the Object which has the methods for accessing data from the database.
 * 3) Database : This is the Main connection point to our DataBase.
 *
 * As Entity is the used for store the data into tables. So, here we have to declare this class as "Data class".. */

@Entity(tableName= "books") /* This is known as "Annotation". It is used to tell the compiler, "what we are creating". So, this annotation will
    tell the compiler that it is an Entity's class. By using these annotations, we can direct perform the action that is binded with that annotation.
    And, hence we don't have to write the Queries for all the pre-defined actions.
    If we don't give any name there to the table, then by default it takes the class Name..
 */
data class BookEntity(
    @PrimaryKey val book_ID: Int,
    @ColumnInfo(name = "book_Name") val book_Name: String,
    @ColumnInfo(name = "author_Name") val author_Name: String,
    @ColumnInfo(name = "book_Rating") val book_Rating: String,
    @ColumnInfo(name = "book_Price") val book_Price: String,
    @ColumnInfo(name = "book_Icon") val book_Icon: String
    /* ColumnInfo specifies the column name to the tables in database. */
)