package com.ibaevzz.shoppinglist.di

object ShoppingComponentProvider {
    private lateinit var appComponent: ShoppingComponent
    fun appComponent() = appComponent

    fun build() {
        appComponent = DaggerShoppingComponent.factory().create()
    }
}