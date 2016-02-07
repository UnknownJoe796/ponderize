package com.ivieleague.ponderize

import android.appwidget.AppWidgetManager
import android.content.Context
import com.ivieleague.ponderize.model.Verse
import com.lightningkite.kotlincomponents.gsonFrom
import com.lightningkite.kotlincomponents.gsonTo
import com.lightningkite.kotlincomponents.logging.logD
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
        logD(verseJSON)
        if (verseJSON == null) {
            verseJSON = context.defaultSharedPreferences.getString(extraVerse(AppWidgetManager.INVALID_APPWIDGET_ID), null)
        }
        if (verseJSON == null) return ArrayList()
        try {
            val results = verseJSON.gsonFrom<ArrayList<Verse>>()!!
            logD(results)
            return results
        } catch(e: Exception) {
            try {
                val result = verseJSON.gsonFrom<Verse>()!!
                logD(result)
                return arrayListOf(result)
            } catch(e: Exception) {
                e.printStackTrace()
                return ArrayList()
            }
        }
    }

    public fun setVerses(context: Context, id: Int, verses: ArrayList<Verse>) {
        val verseJSON = verses.gsonTo()
        logD(verseJSON)
        context.defaultSharedPreferences.edit().putString(extraVerse(id),
                verseJSON
        ).commit()
    }
}