package com.vinted.demovinted.models

import java.math.BigDecimal // exact decimal type for money

class FeedItem( // one item exactly as the API sends it
    val id: Int = 0, // item id
    val price: BigDecimal = BigDecimal.ONE, // price; default 1
    private val photo: String = "", // raw photo filename (private: internal use)
    private val brand: String = "", // raw brand name (private)
    val category: String = "", // category text
)   {

    val mainPhoto: Photo // computed property (like Python @property)
        get() = Photo(url = "https://mobile-homework-api.vinted.com/images/$photo") // build a full image URL from the filename

    val itemBrand: ItemBrand // computed property
        get() = ItemBrand.createNoBrand(brand) // wrap the brand name in a ItemBrand
}