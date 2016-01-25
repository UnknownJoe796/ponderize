package com.ivieleague.ponderize.vc

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.view.Gravity
import android.view.View
import com.ivieleague.ponderize.*
import com.ivieleague.ponderize.model.Verse
import com.ivieleague.ponderize.model.text
import com.ivieleague.ponderize.model.title
import com.lightningkite.kotlincomponents.async.doAsync
import com.lightningkite.kotlincomponents.dip
import com.lightningkite.kotlincomponents.linearLayout
import com.lightningkite.kotlincomponents.observable.KObservable
import com.lightningkite.kotlincomponents.vertical
import com.lightningkite.kotlincomponents.viewcontroller.StandardViewController
import com.lightningkite.kotlincomponents.viewcontroller.containers.VCStack
import com.lightningkite.kotlincomponents.viewcontroller.implementations.VCActivity
import org.jetbrains.anko.*
import java.util.*

/**
 * Created by josep on 10/4/2015.
 */
class MainVC(val stack: VCStack, val widgetId: Int) : StandardViewController() {

    val versesBond: KObservable<ArrayList<Verse>> = KObservable(ArrayList<Verse>())
    var verses: ArrayList<Verse> by versesBond

    override fun makeView(activity: VCActivity): View {
        connect(versesBond) {
            if (verses.size == 0) return@connect
            Config.setVerses(activity, widgetId, it)
            if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                val component = ComponentName(activity, PonderizeAppWidgetProvider::class.java);
                val appWidgetManager = AppWidgetManager.getInstance(activity)
                appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.text_holder);
                appWidgetManager.updateAppWidget(
                        widgetId,
                        Widget.remoteViews(activity, widgetId, it)
                )
            }
        }

        val verseFromSP = Config.getVerses(activity, widgetId)
        if (verseFromSP.size > 0) verses = verseFromSP

        return linearLayout(activity) {
            padding = dip(8)
            orientation = vertical
            gravity = Gravity.CENTER
            textView("Current Scripture:") {
                styleHeader()
            }.lparams(wrapContent, wrapContent)

            textView {
                styleDefault()
                connect(versesBond) {
                    text = it.title
                }
            }.lparams(wrapContent, wrapContent)

            scrollView {
                textView {
                    styleDefault()
                    connect(versesBond) {
                        text = it.text
                    }
                }.lparams(wrapContent, wrapContent)
            }.lparams(wrapContent, 0, 1f)

            button("Change Verse") {
                var preventClick: Boolean = false
                onClick {
                    if (preventClick) return@onClick
                    preventClick = true
                    text = "Loading Database..."

                    doAsync({ Database(context) }) {
                        preventClick = false
                        text = "Change Scripture"

                        stack.push(DatabaseVC(stack, it) { result ->
                            verses = result
                        })
                    }

                }
            }

            button("Done") {
                onClick {
                    stack.pop()
                }
            }
        }
    }
}