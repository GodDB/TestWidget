package com.example.testwidget.interaction

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.testwidget.R
import com.example.testwidget.WidgetUtil.updateRemoteView
import com.example.testwidget.db.AppDao
import com.example.testwidget.db.SwitchEntity
import com.example.testwidget.db.WidgetIdEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@RequiresApi(Build.VERSION_CODES.S)
class AppInteractionWidgetController : KoinComponent {
    private val appDao by inject<AppDao>()
    private val context by inject<Context>()
    private val controllerScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    init {
        combine(
            appDao.getSwitchFlow(),
            appDao.getWidgetIdFlow()
        ) { switchList, widgetIdList ->
            (switchList.firstOrNull()?.enable ?: false) to widgetIdList
        }.onEach {
            val (enable: Boolean, widgetIdList: List<WidgetIdEntity>) = it

            widgetIdList.forEach {
                updateRemoteView(
                    context = context,
                    widgetId = it.id,
                    layout = R.layout.app_interaction_widget,
                ) {
                    setCompoundButtonChecked(R.id.sw_enable, enable)
                }
            }
        }.launchIn(controllerScope)
    }

    fun insertWidgetId(widgetId: Int) {
        controllerScope.launch {
            appDao.insertAppWidgetId(WidgetIdEntity(widgetId))
        }
    }

    fun selectSwitch() {
        controllerScope.launch {
            val enable = appDao.getSwitchValues().firstOrNull()?.enable ?: false
            appDao.insertSwitchValue(SwitchEntity(enable = !enable))
        }
    }
}
