package com.ivieleague.ponderize.vc

import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.ivieleague.ponderize.Database
import com.ivieleague.ponderize.model.Verse
import com.ivieleague.ponderize.styleHeader
import com.ivieleague.ponderize.styleItem
import com.lightningkite.kotlincomponents.adapter.ViewControllerAdapter
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
class DatabaseVC(val stack: VCStack, val database: Database, val onResult: (ArrayList<Verse>) -> Unit) : AutocleanViewController() {

    override fun make(activity: VCActivity): View {
        super.make(activity)
        return _LinearLayout(activity).apply {
            gravity = Gravity.CENTER
            orientation = vertical
            textView("Scriptures") {
                styleHeader()
            }.lparams(wrapContent, wrapContent)
            listView {
                adapter = ViewControllerAdapter.quick(activity, database.volumes) {
                    TextView(context).apply {
                        styleItem()
                        backgroundResource = selectableItemBackground
                        itemBond.bind {
                            text = it.title
                        }
                        onClick {
                            stack.push(VolumeVC(stack, item, onResult))
                        }
                    }
                }
            }.lparams(matchParent, 0, 1f)
        }
    }
}