package com.ivieleague.ponderize.vc

import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.ivieleague.ponderize.model.Book
import com.ivieleague.ponderize.model.Verse
import com.ivieleague.ponderize.styleHeader
import com.ivieleague.ponderize.styleItem
import com.lightningkite.kotlincomponents.adapter.LightningAdapter
import com.lightningkite.kotlincomponents.observable.bind
import com.lightningkite.kotlincomponents.selectableItemBackgroundResource
import com.lightningkite.kotlincomponents.vertical
import com.lightningkite.kotlincomponents.viewcontroller.ViewController
import com.lightningkite.kotlincomponents.viewcontroller.containers.VCStack
import com.lightningkite.kotlincomponents.viewcontroller.implementations.VCActivity
import org.jetbrains.anko.*
import java.util.*

/**
 * Created by josep on 10/4/2015.
 */
class BookVC(val stack: VCStack, val book: Book, val onResult: (ArrayList<Verse>) -> Unit) : ViewController {
    override fun make(activity: VCActivity): View {
        return _LinearLayout(activity).apply {
            gravity = Gravity.CENTER
            orientation = vertical

            textView(book.title) {
                styleHeader()
            }.lparams(wrapContent, wrapContent)

            listView {
                adapter = LightningAdapter(book.chapters.toList()) { obs ->
                    TextView(context).apply {
                        backgroundResource = selectableItemBackgroundResource
                        styleItem()
                        bind(obs) {
                            text = it.title
                        }

                        onClick {
                            stack.push(ChapterVC(stack, obs.value, onResult))
                        }
                    }
                }
            }.lparams(matchParent, 0, 1f)
        }
    }
}