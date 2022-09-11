package com.example.testwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.testwidget.WidgetUtil.getWidgetsSize
import com.example.testwidget.WidgetUtil.updateRemoteView

class BasicWidgetProvider : AppWidgetProvider() {

    private val REQUEST_CODE = 10

    override fun onReceive(context: Context?, intent: Intent?) {
        showLog(context, "onReceive")

        when (intent?.action) {
            WidgetEventType.BUTTON.name -> {
                showLog(context, "button click")
            }
            else -> {
                super.onReceive(context, intent)
            }
        }
    }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        showLog(context, "onUpdate")
        Log.e("godgod", "${appWidgetIds?.map { it.toString() + ",   " }}")
        if (context != null && appWidgetManager != null && appWidgetIds != null) {
            setupWidget(context, appWidgetManager, appWidgetIds)
        }
    }

    private fun setupWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { widgetId ->
            val widgetSize = appWidgetManager.getWidgetsSize(context, widgetId)
            updateRemoteView(
                context = context,
                widgetId = widgetId,
                layout = R.layout.basic_widget,
                onUpdate = {
                    val buttonIntent = Intent(context, BasicWidgetProvider::class.java).apply {
                        action = WidgetEventType.BUTTON.name
                        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                    }
                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        REQUEST_CODE,
                        buttonIntent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                    setOnClickPendingIntent(R.id.btn_widget, pendingIntent)
                    setTextViewText(R.id.btn_widget, "WIDGET 클릭 \n (${widgetSize.first}, ${widgetSize.second})")
                }
            )
        }
    }

    override fun onAppWidgetOptionsChanged(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetId: Int, newOptions: Bundle?) {
        showLog(context, "onAppWidgetOptionsChanged")
        if (context != null && appWidgetManager != null) {
            val widgetSize = appWidgetManager.getWidgetsSize(context, appWidgetId)
            updateRemoteView(
                context = context,
                widgetId = appWidgetId,
                layout = R.layout.basic_widget,
            ) {
                setTextViewText(R.id.btn_widget, "WIDGET 클릭 \n (${widgetSize.first}, ${widgetSize.second})")
            }
        }
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        showLog(context, "onDeleted")
    }

    override fun onDisabled(context: Context?) {
        showLog(context, "onDisabled")
    }

    override fun onEnabled(context: Context?) {
        showLog(context, "onEnabled")
    }

    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        showLog(context, "onRestored")
    }

    private fun showLog(context: Context?, msg: String) {
        Log.e("godgod", "showToast context == $context   msg == $msg")
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

}

