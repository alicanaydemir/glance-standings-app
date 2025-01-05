package com.aydemir.glancestandingsapp.util

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.aydemir.glancestandingsapp.widget.GlanceStandingsAppWidget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun Context.updateWidget() {
    withContext(Dispatchers.Main) {
        val manager = GlanceAppWidgetManager(this@updateWidget)
        val widget = GlanceStandingsAppWidget()
        val glanceIds = manager.getGlanceIds(widget.javaClass)
        glanceIds.forEach { glanceId ->
            widget.update(this@updateWidget, glanceId)
        }
    }
}