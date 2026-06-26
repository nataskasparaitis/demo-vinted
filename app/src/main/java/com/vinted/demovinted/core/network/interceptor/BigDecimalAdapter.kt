package com.vinted.demovinted.core.network.interceptor

import com.squareup.moshi.FromJson // marks the JSON-to-object method
import com.squareup.moshi.ToJson // marks the object-to-JSOM method
import java.math.BigDecimal // exact decimal type

object BigDecimalAdapter { // as single shared instance (stateless converter)

    @FromJson // teach Moshi how to READ a BigDecimal
    fun fromJson(string: String) = BigDecimal(string) // parse the JSON string into a BigDecimal

    @ToJson // teach Moshi how to WRITE a BigDecimal
    fun toJson(value: BigDecimal) = value.toString() // turn the BigDecimal back into a string
}