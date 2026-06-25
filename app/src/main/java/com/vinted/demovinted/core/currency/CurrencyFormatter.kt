package com.vinted.demovinted.core.currency

import java.math.BigDecimal // exact decimal type for money (like Python's Decimal)
import java.text.NumberFormat // Java helper that formats numbers as currency
import java.util.Currency // represents a currency (EUR, USD, ...)
import java.util.Locale // represents a language/region (ENGLISH, FRENCH, ...)
import javax.inject.Inject // marks a constructor Hilt is allowed to call

fun interface CurrencyFormatter { // interface with one method (so it can be made from a lambda)
    fun format( // turn an amount into display text
        amount: BigDecimal?, // the price; ? means it may be null
    ): CharSequence // returns String-like text
}

class DefaultCurrencyFormatter @Inject constructor() : CurrencyFormatter { // real impl; Hilt can build it
    override fun format(amount: BigDecimal?): CharSequence { // implement the interface method
        val numberFormat = NumberFormat.getCurrencyInstance(Locale.ENGLISH) // currency formatter for English
        val currency = try { // choose a currency, guarding against errors
            Currency.getInstance(Locale.ENGLISH) // currency for the English locale
        } catch (e: Exception) { // if that lookup fails...
            Currency.getInstance("EUR") // ...fall back to euros
        }
        numberFormat.currency = currency // tell the formatter which currency to use

        return numberFormat.format(amount) // format the amount and return the text
    }
}