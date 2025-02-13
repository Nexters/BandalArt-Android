package com.nexters.bandalart

import android.app.Application
import com.nexters.bandalart.di.initKoin
import org.koin.android.ext.koin.androidContext

class BandalartApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@BandalartApplication)
        }
    }
}
