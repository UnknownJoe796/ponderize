package com.ivieleague.ponderize.vc

import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.ivieleague.ponderize.model.Verse
import com.ivieleague.ponderize.model.Volume
import com.ivieleague.ponderize.styleHeader
import com.ivieleague.ponderize.styleItem
import com.lightningkite.kotlincomponents.adapter.LightningAdapter
import com.lightningkite.kotlincomponents.linearLayout
import com.lightningkite.kotlincomponents.observable.bind
import com.lightningkite.kotlincomponents.selectableItemBackgroundResource
import com.lightningkite.kotlincomponents.vertical
import com.lightningkite.kotlincomponents.viewcontroller.StandardViewController
import com.lightningkite.kotlincomponents.viewcontroller.containers.VCStack
import com.lightningkite.kotlincomponents.viewcontroller.implementations.VCActivity
import org.jetbrains.anko.*
import java.util.*

/**
 * Created by josep on 10/4/2015.
 */
class VolumeVC(val stack: VCStack, val volume: Volume, val onResult: (ArrayList<Verse>) -> Unit) : StandardViewController() {

    override fun makeView(activity: VCActivity): View {
        return linearLayout(activity) {
            gravity = Gravity.CENTER
            orientation = vertical

            textView(volume.title) {
                styleHeader()
            }.lparams(wrapContent, wrapContent)

            listView {
                adapter = LightningAdapter(volume.books) {itemObs ->
                    TextView(context).apply {
                        styleItem()
                        backgroundResource = selectableItemBackgroundResource
                        bind(itemObs) {
                            text = it.title
                        }
                        onClick {
                            stack.push(BookVC(stack, itemObs.value, onResult))
                        }
                    }
                }
            }.lparams(matchParent, 0, 1f)
        }
    }
}