package com.ivieleague.ponderize

import android.appwidget.AppWidgetManager
import android.content.Context
import com.ivieleague.ponderize.model.Verse
import com.lightningkite.kotlincomponents.gsonFrom
import com.lightningkite.kotlincomponents.gsonTo
import org.jetbrains.anko.defaultSharedPreferences
import java.util.*

/**
 * Created by josep on 10/4/2015.
 */
object Config {
    const val ROOT: String = "http://scriptures.desh.es/api/v1"

    const val VERSE: String = "verse"
    public fun extraVerse(id: Int): String = VERSE + id.toString()

    public fun getVerses(context: Context, id: Int): ArrayList<Verse> {
        var verseJSON: String? = context.defaultSharedPreferences.getString(extraVerse(id), null)
        if (verseJSON == null) {
            verseJSON = context.defaultSharedPreferences.getString(extraVerse(AppWidgetManager.INVALID_APPWIDGET_ID), null)
        }
        if (verseJSON == null) return ArrayList()
        try {
            return verseJSON.gsonFrom<ArrayList<Verse>>()!!
        } catch(e: Exception) {
            try {
                return arrayListOf(verseJSON.gsonFrom<Verse>()!!)
            } catch(e: Exception) {
                e.printStackTrace()
                return ArrayList()
            }
        }
    }

    public fun setVerses(context: Context, id: Int, verses: ArrayList<Verse>) {
        context.defaultSharedPreferences.edit().putString(extraVerse(id),
                verses.joinToString(", ", "[", "]") { it.gsonTo() }
        ).commit()
    }
}