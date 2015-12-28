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
import com.lightningkite.kotlincomponents.databinding.Bond
import com.lightningkite.kotlincomponents.dip
import com.lightningkite.kotlincomponents.vertical
import com.lightningkite.kotlincomponents.viewcontroller.AutocleanViewController
import com.lightningkite.kotlincomponents.viewcontroller.containers.VCStack
import com.lightningkite.kotlincomponents.viewcontroller.implementations.VCActivity
import org.jetbrains.anko.*
import java.util.*

/**
 * Created by josep on 10/4/2015.
 */
class MainVC(val stack: VCStack, val widgetId: Int) : AutocleanViewController() {

    val versesBond: Bond<ArrayList<Verse>> = listener(Bond(ArrayList<Verse>()))
    var verses: ArrayList<Verse> by versesBond

    override fun make(activity: VCActivity): View {
        super.make(activity)

        versesBond.bind {
            if (verses.size() == 0) return@bind
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
        if (verseFromSP.size() > 0) verses = verseFromSP

        return _LinearLayout(activity).apply {
            padding = dip(8)
            orientation = vertical
            gravity = Gravity.CENTER
            textView("Current Scripture:") {
                styleHeader()
            }.lparams(wrapContent, wrapContent)

            textView {
                styleDefault()
                versesBond.bind {
                    text = it.title
                }
            }.lparams(wrapContent, wrapContent)

            scrollView {
                textView {
                    styleDefault()
                    versesBond.bind {
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