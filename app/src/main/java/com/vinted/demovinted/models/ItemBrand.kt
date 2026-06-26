package com.vinted.demovinted.models

import com.squareup.moshi.Json // maps a JSON field name to a Kotlin property

data class ItemBrand( // a data holder describing a brand
    val id: String = "", // brand id; default empty
    @Json(name = "is_custom_brand") val isCustomBrand: Boolean = false, // JSON "is_custom_brand"
    @Json(name = "favourite_count") var favouriteCount: Int = 0, // JSON "favourite_count"
    @Json(name = "item_count") val itemCount: Int = 0, // JSON "item_count"
    @Json(name = "pretty_image_count") val prettyItemCount: String = "", // JSON "pretty_image_count"
    val title: String = "", // brand name shown in the UI
) {

    override fun toString() = title // printing a brand shows its title

    companion object { // members called on the type itself (like Python @classmethod)
        const val NO_BRAND_ID = "1" // id used for the "no brand" placeholder

        @JvmOverloads // Java interop: generate overloads for the default argument
        @JvmStatic // Java interop: expose as a static method
        fun createNoBrand(title: String = "") = ItemBrand(NO_BRAND_ID, title = title) // factory for a placeholder brand
    }
}