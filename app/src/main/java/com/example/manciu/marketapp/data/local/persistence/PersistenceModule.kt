package com.example.manciu.marketapp.data.local.persistence

import android.content.Context
import androidx.room.Room
import com.example.manciu.marketapp.data.local.persistence.PersistenceConstants.DATABASE_NAME
import com.example.manciu.marketapp.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class PersistenceModule {

    @Provides
    @ApplicationScope
    fun provideDatabase(context: Context): ProductDatabase =
            Room.databaseBuilder(context, ProductDatabase::class.java, DATABASE_NAME).build()

    @Provides
    @ApplicationScope
    fun provideRepository(database: ProductDatabase): ProductRepository =
            ProductRepository(database)

}