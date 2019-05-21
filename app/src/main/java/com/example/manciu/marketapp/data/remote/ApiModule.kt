package com.example.manciu.marketapp.data.remote

import com.example.manciu.marketapp.data.remote.ApiConstants.ACCEPT_HEADER
import com.example.manciu.marketapp.data.remote.ApiConstants.API_BASE_URL
import com.example.manciu.marketapp.data.remote.ApiConstants.APPLICATION_JSON
import com.example.manciu.marketapp.di.scope.ApplicationScope
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class ApiModule {

    @Provides
    @ApplicationScope
    internal fun provideHeaderInterceptor() = Interceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
                .header(ACCEPT_HEADER, APPLICATION_JSON)
                .method(original.method(), original.body())
                .build()

        chain.proceed(request)
    }

    @Provides
    @ApplicationScope
    internal fun provideLoggingInterceptor(): HttpLoggingInterceptor =
            HttpLoggingInterceptor().also {
                it.level = HttpLoggingInterceptor.Level.BODY
            }

    @Provides
    @ApplicationScope
    internal fun provideHttpClient(interceptor: Interceptor,
                          loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
            OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .addInterceptor(loggingInterceptor)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build()

    @Provides
    @ApplicationScope
    internal fun provideGsonConverterFactory(): Converter.Factory =
            GsonConverterFactory.create(GsonBuilder().create())

    @Provides
    @ApplicationScope
    internal fun provideCallAdapterFactory(): CallAdapter.Factory =
            RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

    @Provides
    @ApplicationScope
    internal fun provideRetrofit(okHttpClient: OkHttpClient,
                        gsonConverter: Converter.Factory,
                        callAdapterFactory: CallAdapter.Factory
    ): Retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(gsonConverter)
            .addCallAdapterFactory(callAdapterFactory)
            .client(okHttpClient)
            .build()

    @Provides
    @ApplicationScope
    internal fun provideService(retrofit: Retrofit): RemoteService =
            retrofit.create(RemoteService::class.java)
}