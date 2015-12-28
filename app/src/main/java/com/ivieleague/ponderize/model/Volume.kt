package com.ivieleague.ponderize.model

import android.database.sqlite.SQLiteDatabase
import java.util.*

/**
 * Created by josep on 10/4/2015.
 */
class Volume(
        val database: SQLiteDatabase,
        var id: Int = 0,
        var title: String = "",
        var titleLong: String = "",
        var subtitle: String = "",
        var urlPart: String = "",
        var chapters: Int = 0,
        var verses: Int = 0) {

    val books: List<Book> get() {
        val cursor = database.query("lds_scriptures_books", null, "volume_id=?", arrayOf(id.toString()), null, null, null)
        val list: ArrayList<Book> = ArrayList()
        cursor.moveToFirst()
        if (!cursor.isAfterLast) do {
            list.add(Book(
                    this,
                    cursor.getInt(0),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getInt(8),
                    cursor.getInt(9)
            ))
        } while (cursor.moveToNext())
        return list
    }
}