package com.ivieleague.ponderize.model

import android.database.sqlite.SQLiteDatabase
import java.util.*

/**
 * Created by josep on 10/4/2015.
 */
class Verse(
        @Transient var chapter: Chapter? = null,
        var id: Int = 0,
        var verse: Int = 0,
        var pilcrow: Int = 0,
        var text: String = "",
        var title: String = "",
        var titleShort: String = ""
) : kotlin.Comparable<Verse> {
    override fun compareTo(other: Verse): Int {
        return verse.compareTo(other.verse)
    }

    public val database: SQLiteDatabase? get() = chapter?.database
}

public val ArrayList<Verse>.title: String get() {
    if (size == 0) {
        return "None"
    } else if (size == 1) {
        return this[0].title
    } else {
        val builder = StringBuilder()
        var sublist: ArrayList<Verse> = ArrayList()
        sublist.add(this[0])
        builder.append(this[0].title)
        for (verse in this.listIterator(1)) {
            if (verse.verse == sublist.last().verse + 1) {
                sublist.add(verse)
            } else {
                if (sublist.size == 1) {
                    builder.append(", ")
                } else {
                    builder.append(" - ").append(sublist.last().verse).append(", ")
                }

                sublist.clear()
                sublist.add(verse)
                builder.append(verse.verse.toString())
            }
        }
        if (sublist.size > 1) {
            builder.append(" - ").append(sublist.last().verse)
        }
        //TODO: Improve this text for unordered verses
        return builder.toString()
    }
}

public val ArrayList<Verse>.text: String get() {
    if (size <= 0) {
        return "No verse selected."
    } else if (size == 1) {
        return this[0].text
    } else {
        return joinToString("\n\n") { it.verse.toString() + ") " + it.text }
    }
}