package com.gmat.di


import com.gmat.data.repository.QRCodeRepository
import com.gmat.data.repository.QRCodeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindQRRepo(
        qrCodeRepositoryImpl: QRCodeRepositoryImpl
    ) : QRCodeRepository

}