package com.example.testwidget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.widget.RemoteViews

object WidgetUtil {

    fun AppWidgetManager.getWidgetsSize(context: Context, widgetId: Int): Pair<Int, Int> {
        val isPortrait = context.resources.configuration.orientation == ORIENTATION_PORTRAIT
        val width = getWidgetWidth(isPortrait, widgetId)
        val height = getWidgetHeight(isPortrait, widgetId)
        val widthInPx = context.dip(width)
        val heightInPx = context.dip(height)
        return widthInPx to heightInPx
    }

    private fun AppWidgetManager.getWidgetWidth(isPortrait: Boolean, widgetId: Int): Int =
        if (isPortrait) {
            getWidgetSizeInDp(this, widgetId, AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
        } else {
            getWidgetSizeInDp(this, widgetId, AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH)
        }

    private fun AppWidgetManager.getWidgetHeight(isPortrait: Boolean, widgetId: Int): Int =
        if (isPortrait) {
            getWidgetSizeInDp(this, widgetId, AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT)
        } else {
            getWidgetSizeInDp(this, widgetId, AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
        }

    private fun getWidgetSizeInDp(appWidgetManager: AppWidgetManager, widgetId: Int, key: String): Int =
        appWidgetManager.getAppWidgetOptions(widgetId).getInt(key, 0)

    private fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()


    fun AppWidgetManager.getRemoteView(context: Context, widgetId: Int): RemoteViews {
        return RemoteViews(
            context.packageName,
            this.getAppWidgetInfo(widgetId).initialLayout
        )
    }

    inline fun AppWidgetManager.updateRemoteView(
        context: Context,
        widgetId: Int,
        onUpdate: RemoteViews.() -> Unit
    ) {
        val remoteView = getRemoteView(context, widgetId)
        remoteView.onUpdate()
        updateAppWidget(widgetId, remoteView)
    }
}
