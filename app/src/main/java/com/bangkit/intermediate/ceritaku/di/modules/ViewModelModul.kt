package com.bangkit.intermediate.ceritaku.di.modules

import com.bangkit.intermediate.ceritaku.ui.viewModels.AuthViewModel
import com.bangkit.intermediate.ceritaku.ui.viewModels.HomeViewModel
import com.bangkit.intermediate.ceritaku.ui.viewModels.StoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { StoryViewModel(get()) }


}