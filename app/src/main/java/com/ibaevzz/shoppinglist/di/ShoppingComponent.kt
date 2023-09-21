package com.ibaevzz.shoppinglist.di

import android.content.Context
import com.ibaevzz.shoppinglist.fragments.ItemsFragment
import com.ibaevzz.shoppinglist.fragments.ListsFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, ViewModelsModule::class])
interface ShoppingComponent {

    fun inject(listsFragment: ListsFragment)
    fun inject(itemsFragment: ItemsFragment)

    @Component.Factory
    interface Factory {
        fun create(): ShoppingComponent
    }

}