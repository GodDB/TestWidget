package com.example.testwidget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.widget.RemoteViews
import androidx.annotation.LayoutRes

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


    fun getRemoteView(context: Context, @LayoutRes layout: Int): RemoteViews {
        return RemoteViews(
            context.packageName,
            layout
        )
    }

    inline fun updateRemoteView(
        context: Context,
        widgetId: Int,
        @LayoutRes layout: Int,
        onUpdate: RemoteViews.() -> Unit
    ) {
        val remoteView = getRemoteView(context, layout)
        remoteView.onUpdate()
        AppWidgetManager.getInstance(context).updateAppWidget(widgetId, remoteView)
    }
}
