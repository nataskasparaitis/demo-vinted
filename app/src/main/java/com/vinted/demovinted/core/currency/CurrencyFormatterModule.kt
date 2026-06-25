package com.vinted.demovinted.core.currency

import dagger.Binds // Hilt: map an interface to an implementation
import dagger.Module // Hilt: marks a class that teaches Hilt how to build things
import dagger.hilt.InstallIn // Hilt: which scope this module lives in
import dagger.hilt.components.SingletonComponent // the app-wide (singleton) scope

@Module // this is a Hilt module
@InstallIn(SingletonComponent::class) // its recipes live for the whole app
interface CurrencyFormatterModule { // written as an interface (required for @Binds)
    @Binds // recipe: "when something needs a CurrencyFormatter..."
    fun bindCurrencyFormatter(impl: DefaultCurrencyFormatter): CurrencyFormatter // ...give it a DefaultCurrencyFormatter
}