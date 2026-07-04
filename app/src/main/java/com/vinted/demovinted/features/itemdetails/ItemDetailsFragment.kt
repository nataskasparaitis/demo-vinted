package com.vinted.demovinted.features.itemdetails // package (folder path)

import android.os.Bundle // carries arguments/saved state
import android.view.View // a UI element
import android.widget.ImageView // an image widget
import androidx.core.view.isVisible // show/hide helper (maps Boolean to VISIBLE/GONE)
import androidx.fragment.app.Fragment // base class for a sub-screen
import androidx.fragment.app.commit // run a fragment transaction
import androidx.fragment.app.viewModels // gets a ViewModel for this fragment
import androidx.lifecycle.Lifecycle // lifecycle states
import androidx.lifecycle.lifecycleScope // coroutine scope tied to the view
import androidx.lifecycle.repeatOnLifecycle // collect only while visible
import androidx.recyclerview.widget.GridLayoutManager // grid arrangement
import by.kirich1409.viewbindingdelegate.viewBinding // one-liner ViewBinding
import com.bumptech.glide.Glide // image loader
import com.vinted.demovinted.R // generated resource ids
import com.vinted.demovinted.core.currency.CurrencyFormatter // price formatter
import com.vinted.demovinted.core.recyclerview.EvenSpacingItemDecorator // grid spacing
import com.vinted.demovinted.databinding.FragmentItemDetailsBinding // typed views for the layout
import com.vinted.demovinted.features.feed.FeedAdapter // reuse the feed grid adapter
import com.vinted.demovinted.features.feed.api.responses.CatalogItemListResponse // suggestions response
import com.vinted.demovinted.models.ItemBox // UI item model
import dagger.hilt.android.AndroidEntryPoint // lets Hilt inject into this fragment
import kotlinx.coroutines.launch // start a coroutine
import javax.inject.Inject // marks a field for Hilt to fill

@AndroidEntryPoint // Hilt-enabled fragment
class ItemDetailsFragment : Fragment(R.layout.fragment_item_details) { // uses fragment_item_details.xml

    private val binding: FragmentItemDetailsBinding by viewBinding() // typed access to the layout's views
    private val viewModel: ItemDetailsViewModel by viewModels() // the screen's ViewModel (built by Hilt)

    @Inject // Hilt fills this in after construction (field injection)
    lateinit var currencyFormatter: CurrencyFormatter // formats the header price; assigned later

    private val feedAdapter: FeedAdapter by lazy { // built on first use
        FeedAdapter(::navigateToItemsDetails) // suggestions grid; tapping opens that item
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // runs after the view exists
        super.onViewCreated(view, savedInstanceState) // base behavior first

        viewModel.init() // start loading suggestions + favorite status

        setUpView() // configure the views
        observeState() // start watching the ViewModel
    }

    private fun setUpView() { // configure the suggestions grid + favorite button
        binding.otherItems.apply { // configure the RecyclerView
            adapter = feedAdapter // attach the adapter
            (layoutManager as GridLayoutManager).spanCount = 2 // 2 columns
            addItemDecoration( // add spacing
                EvenSpacingItemDecorator((resources.displayMetrics.density * 8).toInt()), // 8dp gaps
            )
        }
        binding.buttonFavorite.setOnClickListener { viewModel.onFavoriteClick() } // heart click -> toggle favorite
    }

    private fun navigateToItemsDetails(itemBox: ItemBox) { // open a suggested item's details
        val fragment = newInstance(itemBox) // build a details fragment for it

        parentFragmentManager.commit { // run a fragment transaction
            replace(R.id.container, fragment) // swap in the new screen
            addToBackStack(null) // let Back return to this screen
        }
    }

    private fun observeState() { // subscribe to the ViewModel state
        viewLifecycleOwner.lifecycleScope.launch { // coroutine tied to the view
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) { // only while visible
                viewModel.state.collect { // for each new state...
                    setItemDetails(it.itemBox) // fill in the item details
                    updateItems(it.catalogItemListResponse) // refresh the suggestions
                    binding.buttonFavorite.setBackgroundResource( // set the heart icon...
                        if (it.isFavorite) R.drawable.ic_favorite else R.drawable.ic_unfavorite // filled or outline
                    )
                    binding.progress.isVisible = it.isLoading // show/hide the spinner
                }
            }
        }
    }

    private fun setItemDetails(itemBox: ItemBox) { // put item data into the header views
        with(binding) { // run with binding as receiver (so we can omit "binding.")
            itemCategory.text = itemBox.category // category text
            itemPrice.text = currencyFormatter.format(itemBox.price) // formatted price
            itemBrandTitle.text = itemBox.brandTitle // brand text
            setItemSize(itemBox.size) // size row (shown or hidden)
            itemImage.setImage(itemBox.mainPhoto?.url) // load the main photo
        }
    }

    private fun FragmentItemDetailsBinding.setItemSize(size: String?) { // extension: show/hide the size row
        size?.let { // if size exists...
            itemSize.text = it // set the size text
            itemSize.isVisible = true // show the value
            itemSizeTitle.isVisible = true // show the label
        } ?: run { // otherwise...
            itemSize.isVisible = false // hide the value
            itemSizeTitle.isVisible = false // hide the label
        }
    }

    private fun ImageView.setImage(photoUrl: String?) { // extension: load a URL into this image view
        Glide.with(this) // start a Glide load
            .load(photoUrl) // from this URL
            .into(this) // into this image view
    }

    private fun updateItems(catalogItemListResponse: CatalogItemListResponse) { // refresh the suggestions grid
        val items = catalogItemListResponse.items.map { item -> // convert each raw item...
            ItemBox.fromFeedItem( // ...to a UI item
                feedItem = item, // the raw item
                currencyFormatter = currencyFormatter, // format its price
            )
        }
        feedAdapter.submitList(items) // hand the list to the adapter
    }

    companion object {

        fun newInstance(item: ItemBox): ItemDetailsFragment { // factory: create a details screen for an item
            return ItemDetailsFragment().apply { // make the fragment and configure it
                arguments = Bundle().apply { putParcelable(ItemDetailsConstant.ARG_EXTRA_ITEM, item) } // stash the item in arguments
            }
        }
    }
}