package com.ivieleague.ponderize.model

import android.database.sqlite.SQLiteDatabase
import java.util.*

/**
 * Created by josep on 10/4/2015.
 */
class Book(
        var volume: Volume,
        var id: Int = 0,
        var title: String = "",
        var titleJst: String = "",
        var titleLong: String = "",
        var titleShort: String = "",
        var subtitle: String = "",
        var urlPart: String = "",
        var numChapters: Int = 0,
        var verses: Int = 0) {
    public val database: SQLiteDatabase get() = volume.database

    val chapters: List<Chapter> get() {
        val list: ArrayList<Chapter> = ArrayList()
        for (i in 1..numChapters) {
            list.add(Chapter(this, i))
        }
        return list
    }
}