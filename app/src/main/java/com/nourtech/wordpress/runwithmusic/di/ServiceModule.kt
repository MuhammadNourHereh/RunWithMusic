package com.nourtech.wordpress.runwithmusic.di

import android.content.Context
import com.nourtech.wordpress.runwithmusic.services.components.TrackingNotification
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @ServiceScoped
    @Provides
    fun provideTrackingNotification(@ApplicationContext app: Context)
            = TrackingNotification(app)
}