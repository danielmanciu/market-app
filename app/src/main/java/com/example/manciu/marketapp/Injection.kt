package com.example.manciu.marketapp

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.example.manciu.marketapp.persistence.LocalDatabase
import com.example.manciu.marketapp.remote.RemoteService
import com.example.manciu.marketapp.repository.Repository
import com.example.manciu.marketapp.ui.viewmodel.ProductViewModel
import com.example.manciu.marketapp.ui.viewmodel.ProductViewModelFactory
import com.example.manciu.marketapp.utils.Constants.API_BASE_URL
import com.google.gson.GsonBuilder
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

object Injection {

    fun provideUserDatabase(context: Context): LocalDatabase {
        return LocalDatabase.getInstance(context)
    }

    fun provideRepository(context: Context): Repository {
        val database = provideUserDatabase(context)
        return Repository.getInstance(database)
    }

    fun provideHeaderInterceptor() = Interceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
                .header("Accept", "application/json")
                .method(original.method(), original.body())
                .build()

        chain.proceed(request)
    }

    fun provideLoggingInterceptor() : HttpLoggingInterceptor =
            HttpLoggingInterceptor().also {
                it.level = HttpLoggingInterceptor.Level.BODY
            }

    fun provideHttpClient(interceptor: Interceptor,
                          loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
            OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .addInterceptor(loggingInterceptor)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build()

    fun provideHttpClient() : OkHttpClient {
        val headerInterceptor = provideHeaderInterceptor()
        val loggingInterceptor = provideLoggingInterceptor()
        return provideHttpClient(headerInterceptor, loggingInterceptor)
    }

    fun provideGsonConverterFactory(): Converter.Factory =
            GsonConverterFactory.create(GsonBuilder().create())

    fun provideCallAdapterFactory(): CallAdapter.Factory =
            RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

    fun provideRetrofit(okHttpClient: OkHttpClient,
                        gsonConverter: Converter.Factory,
                        callAdapterFactory: CallAdapter.Factory
    ): Retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(gsonConverter)
            .addCallAdapterFactory(callAdapterFactory)
            .client(okHttpClient)
            .build()

    fun provideService(): RemoteService {
        val headerInterceptor = provideHeaderInterceptor()
        val loggingInterceptor = provideLoggingInterceptor()
        val httpClient = provideHttpClient(headerInterceptor, loggingInterceptor)
        val gsonFactory = provideGsonConverterFactory()
        val callAdapterFactory = provideCallAdapterFactory()
        val retrofit = provideRetrofit(httpClient, gsonFactory, callAdapterFactory)

        return retrofit.create(RemoteService::class.java)
    }

    fun provideViewModelFactory(context: Context): ProductViewModelFactory {
        val repository = provideRepository(context)
        val service = provideService()

        return ProductViewModelFactory(repository, service)
    }

    fun provideViewModel(context: Context): ProductViewModel {
        val viewModelFactory = provideViewModelFactory(context as FragmentActivity)
        return ViewModelProviders.of(context, viewModelFactory).get(ProductViewModel::class.java)
    }

}
