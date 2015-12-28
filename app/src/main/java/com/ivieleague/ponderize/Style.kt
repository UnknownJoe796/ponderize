package com.ivieleague.ponderize

import android.graphics.Color
import android.view.Gravity
import android.widget.TextView
import com.lightningkite.kotlincomponents.dip
import org.jetbrains.anko.padding
import org.jetbrains.anko.textColor

/**
 * Created by josep on 10/4/2015.
 */
public fun TextView.styleDefault() {
    textSize = 18f
    textColor = Color.WHITE
    padding = dip(4)
}

public fun TextView.styleItem() {
    styleDefault()
    minHeight = dip(40)
    textSize = 18f
    gravity = Gravity.CENTER_VERTICAL
    padding = dip(8)
}

public fun TextView.styleHeader() {
    styleDefault()
    textSize = 24f
}