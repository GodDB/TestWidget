package com.example.testwidget.di

import android.appwidget.AppWidgetManager
import com.example.testwidget.MainViewModel
import com.example.testwidget.db.AppDB
import com.example.testwidget.db.AppDao
import com.example.testwidget.interaction.AppInteractionWidgetController
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val modules: List<Module> = listOf(
    module {
        single {
            AppDB.create(get())
        }
    },

    module {
        single<AppDao> {
            get<AppDB>().appDao()
        }
    },

    module {
        viewModel {
            MainViewModel()
        }
    },

    module {
        single<AppWidgetManager> {
            AppWidgetManager.getInstance(get())
        }
    },

    module {
        single {
            AppInteractionWidgetController()
        }
    }

)
