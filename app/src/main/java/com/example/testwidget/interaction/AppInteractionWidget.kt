package com.example.testwidget.interaction

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import com.example.testwidget.BasicWidgetProvider
import com.example.testwidget.R
import com.example.testwidget.WidgetEventType
import com.example.testwidget.WidgetUtil.updateRemoteView

@RequiresApi(Build.VERSION_CODES.S)
class AppInteractionWidget : AppWidgetProvider() {

    private val controller by lazy { AppInteractionWidgetController() }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("godgod", "onReceive")
        when (intent?.action) {
            WidgetEventType.SWITCH.name -> {
                controller.selectSwitch()
            }
            else -> {
                super.onReceive(context, intent)
            }
        }
    }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        if (context == null || appWidgetManager == null || appWidgetIds == null) return

        appWidgetIds.forEach { widgetId ->
            controller.insertWidgetId(widgetId)
            updateRemoteView(
                context = context,
                widgetId = widgetId,
                layout = R.layout.app_interaction_widget,
            ) {
                val buttonIntent = Intent(context, AppInteractionWidget::class.java).apply {
                    action = WidgetEventType.SWITCH.name
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    11,
                    buttonIntent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
                setOnCheckedChangeResponse(R.id.sw_enable, RemoteViews.RemoteResponse.fromPendingIntent(pendingIntent))
            }
        }
    }

}


