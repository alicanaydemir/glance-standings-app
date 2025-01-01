package com.aydemir.glancetestapp.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.os.Bundle
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class GlanceTestAppWidgetReceiver : GlanceAppWidgetReceiver() {

    //GlanceTestAppWidget çağırılır
    override val glanceAppWidget: GlanceAppWidget
        get() = GlanceTestAppWidget()

    // updatePeriodMillis özelliğiyle tanımlanan aralıklarla güncellemek için çağrılır.
    // kullanıcı widget'ı eklediğinde de çağrılır.
    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    // widget yeniden boyutlandırılınca çağrılır.
    override fun onAppWidgetOptionsChanged(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    // widget ekrandan kaldırıldığında çağırılır
    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        runDeleteDataWorker(context)
    }

    // widget ilk kez eklenmeye başlandığında veya başlatıldığında çağrılır.
    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        context?.let { runGetDataWorker(it) }

        //eğer selectedTeamId var ise PeriodicWorkRequest ile workmanager kullanılarak
        //puan durumu datasını düzenli olarak belli aralıklarla güncel tutabiliriz.
    }

}