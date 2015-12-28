package com.ivieleague.ponderize

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.ivieleague.ponderize.model.Verse
import com.ivieleague.ponderize.model.title
import com.lightningkite.kotlincomponents.logging.logD
import java.util.*

/**
 * Created by josep on 10/4/2015.
 */
public object Widget {
    public fun remoteViews(context: Context, id: Int, verses: ArrayList<Verse>): RemoteViews {

        logD(id)

        // Create an Intent to launch ExampleActivity
        val intent = Intent(context, ConfigActivity::class.java);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        val adapterIntent = Intent(context, PonderizeRemoteViewsService::class.java)
        adapterIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)

        val remoteViews = RemoteViews(context.packageName, R.layout.appwidget)
        remoteViews.setTextViewText(R.id.title, verses.title)
        remoteViews.setRemoteAdapter(R.id.text_holder, adapterIntent)
        remoteViews.setOnClickPendingIntent(R.id.title, pendingIntent);

        return remoteViews
    }
}