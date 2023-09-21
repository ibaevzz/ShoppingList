package com.ibaevzz.shoppinglist.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ibaevzz.shoppinglist.view_models.ItemsViewModel
import com.ibaevzz.shoppinglist.view_models.ListsViewModel
import com.ibaevzz.shoppinglist.view_models.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelsModule{
    @Binds
    fun bindViewModelFactory(impl: ViewModelFactory): ViewModelProvider.Factory

    @IntoMap
    @ViewModelKey(ListsViewModel::class)
    @Binds
    fun bindListsViewModel(impl: ListsViewModel): ViewModel

    @IntoMap
    @ViewModelKey(ItemsViewModel::class)
    @Binds
    fun bindItemsViewModel(impl: ItemsViewModel): ViewModel
}
