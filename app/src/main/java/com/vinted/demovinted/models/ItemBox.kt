package com.vinted.demovinted.models

import android.os.Parcelable // lets an object be passed between screens
import com.vinted.demovinted.core.currency.CurrencyFormatter // formats the price
import kotlinx.parcelize.Parcelize // auto-generates the Parcelable code
import java.math.BigDecimal // exact decimal type for money

@Parcelize // make this passable between screens
data class ItemBox(
    val itemId: String = "", // item id as text
    val mainPhoto: Photo? = null, // the main photo; may be null
    val price: BigDecimal = BigDecimal.ZERO, // raw price; default 0
    val formattedPrice: String = "", // price already formatted for display
    val category: String = "", // category text
    val brandTitle: String? = null, // brand name; may be null
    val size: String? = null, // size text; may be null
) : Parcelable { // passable between screens

    companion object { // the factory lives on the type itself

        fun fromFeedItem( // build an ItemBox from a raw FeedItem
            feedItem: FeedItem, // the raw item
            currencyFormatter: CurrencyFormatter, // used to format the price
        ): ItemBox {
            return ItemBox( // create the UI item
                itemId = feedItem.id.toString(), // id as text
                mainPhoto = feedItem.mainPhoto, // photo frm the computed property
                price = feedItem.price, // keep the raw price too
                formattedPrice = currencyFormatter.format(feedItem.price).toString(), // pre-format the price
                brandTitle = feedItem.itemBrand.title, // brand title from the computed property
                category = feedItem.category, // category text
            )
        }
    }
}