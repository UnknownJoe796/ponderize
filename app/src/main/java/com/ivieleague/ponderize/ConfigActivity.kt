package com.ivieleague.ponderize

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import com.ivieleague.ponderize.vc.MainVC
import com.lightningkite.kotlincomponents.viewcontroller.containers.VCStack
import com.lightningkite.kotlincomponents.viewcontroller.implementations.VCActivity
import org.jetbrains.anko.defaultSharedPreferences
import java.util.*

class ConfigActivity : VCActivity() {
    var appWidgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID

    companion object {
        val stacks: HashMap<Int, VCStack> = HashMap()
        fun getStack(id: Int): VCStack {
            return stacks[id] ?: VCStack().apply {
                push(MainVC(this, id))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appWidgetId = defaultSharedPreferences.getInt("widget_id", AppWidgetManager.INVALID_APPWIDGET_ID)
        appWidgetId = intent?.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId) ?: appWidgetId
        if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            defaultSharedPreferences.edit().putInt("widget_id", appWidgetId).commit()
        }

        val stack = getStack(appWidgetId)
        stack.onEmptyListener = {
            finish()
        }
        attach(stack)
    }

    override fun finish() {
        val resultValue = Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(Activity.RESULT_OK, resultValue);
        super.finish()
    }
}
