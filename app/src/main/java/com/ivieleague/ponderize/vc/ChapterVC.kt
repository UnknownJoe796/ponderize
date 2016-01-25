package com.ivieleague.ponderize.vc

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.ivieleague.ponderize.model.Chapter
import com.ivieleague.ponderize.model.Verse
import com.ivieleague.ponderize.model.title
import com.ivieleague.ponderize.styleDefault
import com.ivieleague.ponderize.styleHeader
import com.ivieleague.ponderize.styleItem
import com.lightningkite.kotlincomponents.*
import com.lightningkite.kotlincomponents.adapter.LightningAdapter
import com.lightningkite.kotlincomponents.adapter.ViewControllerAdapter
import com.lightningkite.kotlincomponents.databinding.Bond
import com.lightningkite.kotlincomponents.observable.KObservable
import com.lightningkite.kotlincomponents.viewcontroller.AutocleanViewController
import com.lightningkite.kotlincomponents.viewcontroller.StandardViewController
import com.lightningkite.kotlincomponents.viewcontroller.containers.VCStack
import com.lightningkite.kotlincomponents.viewcontroller.implementations.VCActivity
import org.jetbrains.anko.*
import java.util.*

/**
 * Created by josep on 10/4/2015.
 */
class ChapterVC(val stack: VCStack, val chapter: Chapter, val onResult: (ArrayList<Verse>) -> Unit) : StandardViewController() {

    public val versesBond: KObservable<ArrayList<Verse>> = KObservable(ArrayList())
    public var verses: ArrayList<Verse> by versesBond

    override fun makeView(activity: VCActivity): View = verticalLayout(activity) {
        gravity = Gravity.CENTER

        textView(chapter.title) {
            styleHeader()
        }.lparams(wrapContent, wrapContent)

        listView {
            adapter = LightningAdapter(chapter.verses) { itemObs ->
                TextView(context).apply {
                    styleItem()
                    connect(itemObs) {
                        text = it.verse.toString() + ") " + it.text
                        if (verses.contains(it)) {
                            val selDraw = ContextCompat.getDrawable(context, selectableItemBackgroundResource)
                            background = LayerDrawable(arrayOf(selDraw, ColorDrawable(Color.WHITE.alpha(.25f))))
                        } else {
                            backgroundResource = selectableItemBackgroundResource
                        }
                    }
                    connect(versesBond) {
                        if (it.contains(itemObs.value)) {
                            val selDraw = ContextCompat.getDrawable(context, selectableItemBackgroundResource)
                            background = LayerDrawable(arrayOf(selDraw, ColorDrawable(Color.WHITE.alpha(.25f))))
                        } else {
                            backgroundResource = selectableItemBackgroundResource
                        }
                    }
                    onClick {
                        if (verses.contains(itemObs.value)) {
                            verses.remove(itemObs.value)
                            versesBond.update()
                        } else {
                            val index = verses.indexOfFirst { itemObs.value.verse < it.verse }
                            if (index == -1) verses.add(itemObs.value)
                            else verses.add(index, itemObs.value)
                            versesBond.update()
                        }
                    }
                }
            }
        }.lparams(matchParent, 0, 1f)

        textView {
            styleDefault()
            gravity = Gravity.CENTER
            connect(versesBond) {
                text = it.title
            }
        }.lparams(matchParent, wrapContent)

        linearLayout {
            button {
                styleDefault()
                connect(versesBond) {
                    if (it.size <= 0) {
                        isEnabled = false
                        text = "Select a verse..."
                    } else if (it.size == 1) {
                        isEnabled = true
                        text = "Use this verse"
                    } else {
                        isEnabled = true
                        text = "Use these verses"
                    }
                }
                onClick {
                    if (verses.size >= 1) {
                        onResult(verses)
                        stack.back({ it is MainVC })
                    }
                }
            }.lparams(0, wrapContent, 1f)

            button("Clear") {
                styleDefault()
                connect(versesBond) {
                    if (it.size == 0)
                        isEnabled = false
                    else
                        isEnabled = true
                }
                onClick {
                    verses.clear()
                    versesBond.update()
                }
            }.lparams(wrapContent, wrapContent)
        }.lparams(matchParent, wrapContent)
    }
}