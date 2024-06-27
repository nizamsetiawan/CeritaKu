package com.bangkit.intermediate.ceritaku.di.modules

import com.bangkit.intermediate.ceritaku.source.RemoteDataSource
import com.bangkit.intermediate.ceritaku.source.network.ApiConfig
import com.bangkit.intermediate.ceritaku.utils.Prefs
import org.koin.dsl.module

val appModule = module {
    single { ApiConfig.provideApiService }
    single { RemoteDataSource(get()) }
    single { Prefs.init(get()) }

}
