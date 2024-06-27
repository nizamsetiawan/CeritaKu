package com.bangkit.intermediate.ceritaku.di.modules
import com.bangkit.intermediate.ceritaku.repository.DataRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { DataRepository(get()) } }
