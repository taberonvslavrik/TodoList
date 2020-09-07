package com.todo

import android.app.Application
import com.todo.di.networkModule
import com.todo.di.repositoryModule
import com.todo.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TodoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@TodoApplication)
            modules(
                networkModule,
                repositoryModule,
                viewModelsModule
            )
        }
    }
}