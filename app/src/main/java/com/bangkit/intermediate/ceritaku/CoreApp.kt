package com.bangkit.intermediate.ceritaku

import android.app.Application
import com.bangkit.intermediate.ceritaku.di.modules.appModule
import com.bangkit.intermediate.ceritaku.di.modules.repositoryModule
import com.bangkit.intermediate.ceritaku.di.modules.viewModelModule
import com.bangkit.intermediate.ceritaku.utils.Prefs
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.stopKoin

class CoreApp: Application() {
    override fun onCreate() {

        super.onCreate()
        Prefs.init(this)
        startKoin {
            androidContext(this@CoreApp)
            modules(listOf(
                viewModelModule,
                repositoryModule,
                appModule
            ))
        }
    }
    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}