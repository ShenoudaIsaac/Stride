package com.shenouda.stride.feature_auth.core.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shenouda.stride.feature_auth.data.datasource.remote.AuthRemoteDataSource
import com.shenouda.stride.feature_auth.data.datasource.remote.FirebaseAuthDataSource
import com.shenouda.stride.feature_auth.data.repository_impl.AuthRepositoryImpl
import com.shenouda.stride.feature_auth.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name ="stride_prefs")
@Module
@InstallIn
abstract class AuthModule {
    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(impl: FirebaseAuthDataSource): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    companion object {

        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth =
            FirebaseAuth.getInstance()

        @Provides
        @Singleton
        fun provideFirebaseFirestore(): FirebaseFirestore =
            FirebaseFirestore.getInstance()

        @Provides
        @Singleton
        fun provideDataStore(
            @ApplicationContext context: Context,
        ): DataStore<Preferences> = context.dataStore
    }
}