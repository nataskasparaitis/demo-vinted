package com.vinted.demovinted.features.feed // package (folder path)

import android.os.Bundle // carries saved screen state
import android.view.View // a UI element
import android.widget.Toast // small pop-up message
import androidx.fragment.app.Fragment // base class for a sub-screen
import androidx.fragment.app.viewModels // gets a ViewModel for this fragment
import androidx.lifecycle.Lifecycle // lifecycle states
import androidx.lifecycle.lifecycleScope // coroutine scope tied to the view
import androidx.lifecycle.repeatOnLifecycle // collect only while the screen is visible
import androidx.recyclerview.widget.GridLayoutManager // grid arrangement
import by.kirich1409.viewbindingdelegate.viewBinding // one-liner ViewBinding
import com.vinted.demovinted.R // generated resource ids
import com.vinted.demovinted.core.recyclerview.EvenSpacingItemDecorator // grid spacing
import com.vinted.demovinted.models.ItemBox // UI item model
import com.vinted.demovinted.databinding.FeedFragmentBinding // typed views for feed_fragment.xml
import com.vinted.demovinted.features.feed.FeedViewModel.Event // the event type
import dagger.hilt.android.AndroidEntryPoint // lets Hilt inject into this fragment
import kotlinx.coroutines.launch // start a coroutine

@AndroidEntryPoint // Hilt-enabled fragment
class FeedFragment : Fragment(R.layout.feed_fragment) { // uses feed_fragment.xml as its layout

    private val binding: FeedFragmentBinding by viewBinding() // typed access to the layout's views
    private val viewModel: FeedViewModel by viewModels() // the screen's ViewModel (built by Hilt)

    private val feedAdapter = FeedAdapter() // the grid adapter (no click handling yet)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // runs after the view exists
        super.onViewCreated(view, savedInstanceState) // base behavior first

        observeViewState() // start watching the ViewModel

        binding.feedList.apply { // configure the RecyclerView
            adapter = feedAdapter // attach the adapter
            (layoutManager as GridLayoutManager).spanCount = 2 // make it 2 columns
            addItemDecoration(EvenSpacingItemDecorator((resources.displayMetrics.density * 8).toInt())) // add 8dp gaps
        }
    }

    private fun observeViewState() { // subscribe to ViewModel streams
        viewLifecycleOwner.lifecycleScope.launch { // coroutine tied to the view
            viewModel.feedEvent.collect(::handleNewEvent) // handle each one-shot event
        }

        viewLifecycleOwner.lifecycleScope.launch { // another coroutine
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) { // only while visible
                viewModel.feedState.collect{ state -> handleSuccess(state.content) } // render each new state
            }
        }
    }

    private fun handleNewEvent(event: Event) { // react to an event
        when (event) { // it is one of the sealed Event types
            is Event.Error -> showError(event.t.message) // show the error message
        }
    }

    private fun handleSuccess(items: List<ItemBox>) { // got new items
        feedAdapter.submitList(items) // hand them to the adapter (it diffs + updates)
    }

    private fun showError(message: String?) { // pop up an error
        Toast.makeText(requireActivity(), "Something went wrong - $message", Toast.LENGTH_LONG) // build the toast
            .show() // show it
    }

    companion object { // factory on the type itself

        fun newInstance() = FeedFragment() // create a new feed fragment
    }
}