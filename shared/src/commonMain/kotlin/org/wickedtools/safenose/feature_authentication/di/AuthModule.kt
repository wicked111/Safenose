package org.wickedtools.safenose.feature_authentication.di

import org.koin.dsl.module
import org.wickedtools.safenose.feature_authentication.data.repository.AuthRepositoryImpl
import org.wickedtools.safenose.feature_authentication.domain.repository.AuthRepository
import org.wickedtools.safenose.feature_authentication.domain.usecases.GetCurrentUserUseCase
import org.wickedtools.safenose.feature_authentication.domain.usecases.LoginUseCase
import org.wickedtools.safenose.feature_authentication.domain.usecases.SignUpUseCase
import org.wickedtools.safenose.feature_authentication.presentation.viewmodels.AuthViewModel

val authModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    factory { LoginUseCase(get()) }
    factory { SignUpUseCase(get()) }
    factory { GetCurrentUserUseCase(get()) }
    factory { AuthViewModel(get(), get(), get()) }
}
