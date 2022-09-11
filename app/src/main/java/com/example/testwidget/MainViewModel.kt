package com.example.testwidget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testwidget.db.AppDao
import com.example.testwidget.db.SwitchEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel : ViewModel(), KoinComponent {

    private val appDao: AppDao by inject<AppDao>()

    val enabled: StateFlow<Boolean> = appDao.getSwitchFlow()
        .map { it.firstOrNull()?.enable ?: false }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        )


    fun selectSwitch() {
        viewModelScope.launch {
            appDao.insertSwitchValue(SwitchEntity(enable = !enabled.value))
        }
    }

}
