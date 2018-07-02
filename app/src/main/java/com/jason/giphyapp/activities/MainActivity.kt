package com.jason.giphyapp.activities

import android.app.Activity
import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.jason.giphyapp.GiphyApp
import com.jason.giphyapp.R
import com.jason.giphyapp.adapters.GifAdapter
import com.jason.giphyapp.models.Gif
import com.jason.giphyapp.models.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: GifAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as GiphyApp).getAppComponent().inject(this)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        adapter = GifAdapter()

        initObserver()
        setupRecycler()

        intent?.let { handleIntent(it) }
    }

    private fun setupRecycler() {
        recycler_view.layoutManager = GridLayoutManager(this, SPAN_COUNT)
        recycler_view.adapter = adapter
        recycler_view.smoothScrollToPosition(viewModel.currentPosition)
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                viewModel.shouldDownLoadNextPage(recycler_view.layoutManager.itemCount,
                        (recycler_view.layoutManager as GridLayoutManager).findFirstVisibleItemPosition(),
                        recycler_view.layoutManager.childCount)
            }
        })
    }

    private fun initObserver() {
        viewModel.gifObserver.observe(this, Observer<ArrayList<Gif>> {
            it?.let { gifs ->
                if (gifs.isEmpty()) {
                    setViewVisibilities(false, true)
                    empty_view.text = getString(R.string.empty_search_results)
                } else {
                    setViewVisibilities(true, false)
                    adapter.gifs.addAll(gifs)
                    adapter.notifyDataSetChanged()
                }
            } ?: setViewVisibilities(false, true)
        })
    }

    private fun setViewVisibilities(shown: Boolean, empty: Boolean) {
        when {
            empty -> {
                empty_view.visibility = View.VISIBLE
                progress_bar.visibility = View.GONE
                recycler_view.visibility = View.GONE
            }
            shown -> {
                empty_view.visibility = View.GONE
                progress_bar.visibility = View.GONE
                recycler_view.visibility = View.VISIBLE
            }
            else -> {
                empty_view.visibility = View.GONE
                progress_bar.visibility = View.VISIBLE
                recycler_view.visibility = View.GONE
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleIntent(intent) }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.search -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            setViewVisibilities(false, false)
            adapter.gifs.clear()
            viewModel.resetPaging()
            viewModel.searchQuery = intent.getStringExtra(SearchManager.QUERY)
            viewModel.getGifsFromSearch()
        } else {
            viewModel.getTrendingGifs()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo((getSystemService(Context.SEARCH_SERVICE) as SearchManager).getSearchableInfo(componentName))
        setupSearchQuery()
        return true
    }

    private fun setupSearchQuery() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchQuery = it }
                resetSearchView()
                return false
            }

            override fun onQueryTextChange(newText: String?) = true
        })
    }

    private fun resetSearchView() {
        hideSoftKeyboard(this)
        searchView.setQuery("", false)
        searchView.isIconified = true
    }

    private fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }

    companion object {
        val TAG: String = MainActivity::class.java.name
        const val SPAN_COUNT = 2
    }
}
