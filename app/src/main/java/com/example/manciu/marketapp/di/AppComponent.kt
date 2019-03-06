package com.example.manciu.marketapp.di

import android.app.Application
import com.example.manciu.marketapp.MarketApplication
import com.example.manciu.marketapp.data.persistence.PersistenceModule
import com.example.manciu.marketapp.data.remote.ApiModule
import com.example.manciu.marketapp.di.scope.ApplicationScope
import com.example.manciu.marketapp.data.viewmodel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule

@Component(
        modules = [
            AndroidSupportInjectionModule::class,
            AppModule::class,
            ActivityBuilder::class,
            ApiModule::class,
            PersistenceModule::class,
            ViewModelModule::class
        ]
)
@ApplicationScope
interface AppComponent : AndroidInjector<DaggerApplication> {

    fun inject(marketApplication: MarketApplication)

    override fun inject(instance: DaggerApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}