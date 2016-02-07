package com.ivieleague.ponderize

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context

/**
 * Created by josep on 10/4/2015.
 */
class PonderizeAppWidgetProvider() : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        val N = appWidgetIds.size;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (i in 0..N - 1) {
            val appWidgetId = appWidgetIds[i];

            val verse = Config.getVerses(context, appWidgetId)

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, Widget.remoteViews(context, appWidgetId, verse));
        }
    }

    /*public fun onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    }*/
}