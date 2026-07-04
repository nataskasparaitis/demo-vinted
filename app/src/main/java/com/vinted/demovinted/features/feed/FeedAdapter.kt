package com.vinted.demovinted.features.feed // package (folder path)

import android.view.LayoutInflater // turns an XML layout into real views
import android.view.View // a UI element
import android.view.ViewGroup // a container of views
import androidx.recyclerview.widget.DiffUtil // computes list differences
import androidx.recyclerview.widget.ListAdapter // adapter that diffs lists for you
import androidx.recyclerview.widget.RecyclerView // the list/grid widget
import com.bumptech.glide.Glide // image loader
import com.vinted.demovinted.R // generated resource ids
import com.vinted.demovinted.models.ItemBox // the UI item model
import com.vinted.demovinted.databinding.ItemFeedBinding // typed views for item_feed.xml

class FeedAdapter( // bridges data <-> grid
    private val onItemClick: (ItemBox) -> Unit = {}, // callback for taps; default does nothing
) : ListAdapter<ItemBox, FeedViewHolder>(FeedDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder { // make a reusable cell
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false) // inflate item_feed.xml
        return FeedViewHolder(view) // wrap it in a view holder
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) { // fill a cell with data
        holder.bind(currentList[position]) // bind the item at this position
        holder.itemView.setOnClickListener { onItemClick(currentList[position]) } // tap -> call the callback with this item
    }
}

private class FeedDiffCallback : DiffUtil.ItemCallback<ItemBox>() { // tells ListAdapter how to compare items
    override fun areItemsTheSame(oldItem: ItemBox, newItem: ItemBox): Boolean { // same logical item?
        return oldItem.itemId == newItem.itemId // compare ids
    }

    override fun areContentsTheSame( // did the item's contents change?
        oldItem: ItemBox,
        newItem: ItemBox
    ): Boolean {
        return oldItem == newItem // full data-class equality
    }
}

class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { // holds the views for one cell

    private val binding = ItemFeedBinding.bind(itemView) // typed access to the cell's views
    fun bind(item: ItemBox) { // put an item's data into the views

        binding.itemCategory.text = item.category // set category text
        binding.itemPrice.text = item.formattedPrice // set price text
        binding.itemBrand.text = item.brandTitle // set brand text
        item.size?.let { binding.itemSize.text = it } ?: run { // if size exists, show it...
            binding.itemSize.visibility = View.GONE // ...otherwise hide the size view
        }

        Glide.with(itemView) // load the photo...
            .load(item.mainPhoto?.url) // ...from this URL (may be null)
            .fallback(R.color.colorGray) // gray placeholder if no URL
            .into(binding.itemImage) // into the image view
    }
}