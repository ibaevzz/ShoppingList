package com.ibaevzz.shoppinglist.application

import android.app.Application
import com.ibaevzz.shoppinglist.di.ShoppingComponentProvider

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        ShoppingComponentProvider.build()
    }
}