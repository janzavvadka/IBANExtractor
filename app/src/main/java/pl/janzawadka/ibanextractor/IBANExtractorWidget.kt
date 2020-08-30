package pl.janzawadka.ibanextractor

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.EditText
import android.widget.RemoteViews
import java.util.*

const val WIDGET_SYNC = "WIDGET_SYNC"

class IBANExtractorWidget : AppWidgetProvider() {

    lateinit var preference: MyPreference

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        if (!::preference.isInitialized)
            preference = MyPreference(context)

        val ids = preference.getWidgetIds()
        for (appWidgetId in appWidgetIds) {
            ids.add(appWidgetId.toString())
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
        preference.updateWidgetIds(ids)
    }

    override fun onEnabled(context: Context) {
        val intent = Intent(context, IBANExtractorWidget::class.java)
        intent.action = WIDGET_SYNC
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val now = Calendar.getInstance()
        now.set(Calendar.SECOND, 0)
        now.add(Calendar.MINUTE, 1)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC, now.timeInMillis, 60000, pendingIntent)
    }

    override fun onDisabled(context: Context) {

        print("test")
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context, intent: Intent?) {

        if (WIDGET_SYNC == intent?.action) {
            if (!::preference.isInitialized)
                preference = MyPreference(context)

            val ids = preference.getWidgetIds()
            for (id in ids)
                updateAppWidget(context, AppWidgetManager.getInstance(context), id.toInt())
        }
        super.onReceive(context, intent)
    }

    companion object {

        internal fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {

            val intent = Intent(context, IBANExtractorWidget::class.java)
            intent.action = WIDGET_SYNC
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

            IBANExtractor(context).extractFromClipboard()

            val views = RemoteViews(context.packageName, R.layout.iban_extractor_widget)
            views.setOnClickPendingIntent(R.id.iv_sync, pendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

}