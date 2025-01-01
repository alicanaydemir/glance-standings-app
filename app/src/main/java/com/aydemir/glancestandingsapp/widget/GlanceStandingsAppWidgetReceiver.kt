package com.aydemir.glancestandingsapp.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class GlanceStandingsAppWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget
        get() = GlanceStandingsAppWidget()

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        runDeleteDataWorker(context)
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        context?.let { runGetDataWorker(it) }

        //if selectedTeamId exists, using PeriodicWorkRequest and workmanager.
        //We can keep the score data updated at regular intervals.
    }

}