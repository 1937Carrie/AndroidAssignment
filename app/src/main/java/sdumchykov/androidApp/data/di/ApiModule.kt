package sdumchykov.androidApp.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sdumchykov.androidApp.domain.api.RemoteData
import sdumchykov.androidApp.domain.api.ServerApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    private const val URL = "http://178.63.9.114:7777/api/"

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()
    }

    @Singleton
    @Provides
    fun serverApi(okHttpClient: OkHttpClient): ServerApi {
        return Retrofit.Builder().client(okHttpClient).baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ServerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMainRemoteData(serverApi: ServerApi): RemoteData = RemoteData(serverApi)
}