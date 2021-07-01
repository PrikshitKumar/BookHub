package com.prikshitkumar.bookhub.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDao {
    @Insert
    fun insertBook(bookEntity: BookEntity)  /* Room Library will take care of these two functions insertBook and deleteBook.
        So, we don't need to add the Queries by our self here. And for all other operations we need write the Query. As we use the annotation
        like @Insert, @Delete. So, because of these annotation the insertion and deletion actions are perform automatically. That's why we don't
        need to write any query here... */

    @Delete
    fun deleteBook(bookEntity: BookEntity)

    @Query("select * from books") /* This is how we can write the Queries. "books is the table name". */
    fun getAllBooks(): List<BookEntity> /* Now, for get the books from database we have to write the Query for this function. */

    @Query("select * from books where book_Id = :bookId") /* : is used for telling the compiler that the value is came from the function
     that is just below the Query. */
    fun getBookById(bookId: String): BookEntity
}

/* Here all the functions has only declaration not the function body. Because all the operation performed in the database class and taken care
* by the ROOM Library. Hence, we don't need to gave the implementation to these functions. That's why Dao is the Interface not the class.. */