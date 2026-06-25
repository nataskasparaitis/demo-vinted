package com.vinted.demovinted.models

import android.os.Parcelable // lets an object be passed between screens
import com.squareup.moshi.Json // maps a JSON field name to a Kotlin property
import kotlinx.parcelize.Parcelize // auto-generates the Parcelable code

@Parcelize // generate the screen-passing serialization for this class
data class Photo( // a data holder (auto equals/copy/toString)
    val id: String = "", // photo id; default empty
    val url: String = "", // image web address; default empty
    @Json(name = "is_main") val isMain: Boolean = false, // JSON "is_main" -> isMain
    @Json(name = "full_size_url") val fulSizeUrl: String? = null // JSON "full_size_url"; may be null
) : Parcelable // this class can be passed between screens