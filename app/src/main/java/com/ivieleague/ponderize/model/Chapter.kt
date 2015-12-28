package com.ivieleague.ponderize.model

import android.database.sqlite.SQLiteDatabase
import java.util.*

/**
 * Created by josep on 10/4/2015.
 */
class Chapter(
        var book: Book,
        var index: Int = 0
) {
    public val database: SQLiteDatabase get() = book.database

    val verses: List<Verse> get() {
        val cursor = database.query("lds_scriptures_verses", null, "chapter=? AND book_id=?", arrayOf(index.toString(), book.id.toString()), null, null, null)
        val list: ArrayList<Verse> = ArrayList()
        cursor.moveToFirst()
        if (!cursor.isAfterLast) do {
            list.add(Verse(
                    this,
                    cursor.getInt(0),
                    cursor.getInt(4),
                    cursor.getInt(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8)
            ))
        } while (cursor.moveToNext())
        return list
    }

    val title: String = book.title + " " + index
}