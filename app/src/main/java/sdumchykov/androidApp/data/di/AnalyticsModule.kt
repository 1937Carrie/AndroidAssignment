package sdumchykov.androidApp.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sdumchykov.androidApp.data.repository.LocalLocalUsersRepositoryImpl
import sdumchykov.androidApp.data.repository.NetworkUsersRepositoryImpl
import sdumchykov.androidApp.data.storage.SharedPreferencesStorage
import sdumchykov.androidApp.domain.repository.LocalUsersRepository
import sdumchykov.androidApp.domain.repository.NetworkUsersRepository
import sdumchykov.androidApp.domain.storage.Storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        usersRepository: LocalLocalUsersRepositoryImpl
    ): LocalUsersRepository

    @Binds
    @Singleton
    abstract fun bindSharedPreferencesStorage(
        storage: SharedPreferencesStorage
    ): Storage

    @Binds
    @Singleton
    abstract fun bindNetworkRepository(
        networkUsersRepository: NetworkUsersRepositoryImpl
    ): NetworkUsersRepository
}
