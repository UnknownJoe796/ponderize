package com.ivieleague.ponderize

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.ivieleague.ponderize.model.Verse
import com.lightningkite.kotlincomponents.logging.logD
import java.util.*

/**
 * Created by josep on 10/4/2015.
 */
class PonderizeRemoteViewsService() : RemoteViewsService() {
    var verses: ArrayList<Verse> = ArrayList()
    var widgetId: Int = -1
    override fun onBind(intent: Intent?): IBinder? {
        widgetId = intent?.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID) ?: AppWidgetManager.INVALID_APPWIDGET_ID
        logD(widgetId)
        return super.onBind(intent)
    }

    fun reload() {
        verses = Config.getVerses(this@PonderizeRemoteViewsService, widgetId)
    }

    override fun onGetViewFactory(intent: Intent?): RemoteViewsService.RemoteViewsFactory? {
        return object : RemoteViewsService.RemoteViewsFactory {
            override fun onDataSetChanged() {
                reload()
            }

            override fun getCount(): Int = verses.size

            override fun getViewTypeCount(): Int = 1

            override fun getViewAt(position: Int): RemoteViews? {
                return RemoteViews(packageName, R.layout.textview).apply {
                    if (verses.size > 1) {
                        setTextViewText(R.id.text, verses[position].verse.toString() + ") " + verses[position].text)
                    } else {
                        setTextViewText(R.id.text, verses[position].text)
                    }
                }
            }

            override fun hasStableIds() = true

            override fun onDestroy() {
            }

            override fun getLoadingView(): RemoteViews? {
                return RemoteViews(packageName, R.layout.textview)
            }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun onCreate() {
            }

        }
    }

}