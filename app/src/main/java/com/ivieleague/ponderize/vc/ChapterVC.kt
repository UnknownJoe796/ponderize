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
import com.lightningkite.kotlincomponents.adapter.ViewControllerAdapter
import com.lightningkite.kotlincomponents.alpha
import com.lightningkite.kotlincomponents.databinding.Bond
import com.lightningkite.kotlincomponents.selectableItemBackground
import com.lightningkite.kotlincomponents.vertical
import com.lightningkite.kotlincomponents.viewcontroller.AutocleanViewController
import com.lightningkite.kotlincomponents.viewcontroller.containers.VCStack
import com.lightningkite.kotlincomponents.viewcontroller.implementations.VCActivity
import org.jetbrains.anko.*
import java.util.*

/**
 * Created by josep on 10/4/2015.
 */
class ChapterVC(val stack: VCStack, val chapter: Chapter, val onResult: (ArrayList<Verse>) -> Unit) : AutocleanViewController() {

    public val versesBond: Bond<ArrayList<Verse>> = listener(Bond(ArrayList()))
    public var verses: ArrayList<Verse> by versesBond

    override fun make(activity: VCActivity): View {
        super.make(activity)
        return _LinearLayout(activity).apply {
            gravity = Gravity.CENTER
            orientation = vertical

            textView(chapter.title) {
                styleHeader()
            }.lparams(wrapContent, wrapContent)

            listView {
                adapter = ViewControllerAdapter.quick(activity, chapter.verses) {
                    TextView(context).apply {
                        styleItem()
                        itemBond.bind {
                            text = it.verse.toString() + ") " + it.text
                            if (verses.contains(it)) {
                                val selDraw = ContextCompat.getDrawable(context, selectableItemBackground)
                                background = LayerDrawable(arrayOf(selDraw, ColorDrawable(Color.WHITE.alpha(.25f))))
                            } else {
                                backgroundResource = selectableItemBackground
                            }
                        }
                        versesBond.bind {
                            if (it.contains(item)) {
                                val selDraw = ContextCompat.getDrawable(context, selectableItemBackground)
                                background = LayerDrawable(arrayOf(selDraw, ColorDrawable(Color.WHITE.alpha(.25f))))
                            } else {
                                backgroundResource = selectableItemBackground
                            }
                        }
                        onClick {
                            if (verses.contains(item)) {
                                verses.remove(item)
                                versesBond.update()
                            } else {
                                val index = verses.indexOfFirst { item.verse < it.verse }
                                if (index == -1) verses.add(item)
                                else verses.add(index, item)
                                versesBond.update()
                            }
                        }
                    }
                }
            }.lparams(matchParent, 0, 1f)

            textView {
                styleDefault()
                gravity = Gravity.CENTER
                versesBond.bind {
                    text = it.title
                }
            }.lparams(matchParent, wrapContent)

            linearLayout {
                button {
                    styleDefault()
                    versesBond.bind {
                        if (it.size() <= 0) {
                            isEnabled = false
                            text = "Select a verse..."
                        } else if (it.size() == 1) {
                            isEnabled = true
                            text = "Use this verse"
                        } else {
                            isEnabled = true
                            text = "Use these verses"
                        }
                    }
                    onClick {
                        if (verses.size() >= 1) {
                            onResult(verses)
                            stack.back({ it is MainVC })
                        }
                    }
                }.lparams(0, wrapContent, 1f)

                button("Clear") {
                    styleDefault()
                    versesBond.bind {
                        if (it.size() == 0)
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
}