package com.jason.giphyapp.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.jason.giphyapp.R
import com.jason.giphyapp.adapters.GifAdapter.Companion.DETAIL_EXTRA
import com.jason.giphyapp.models.Gif
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    private var details: Gif? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        details = intent.getSerializableExtra(DETAIL_EXTRA) as? Gif

        details?.let { bindDetails() } ?: throwError()
    }

    private fun throwError() {
        Toast.makeText(this, getString(R.string.details_load_error), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun bindDetails() {
        details?.images?.original?.url?.let {
            if (it.isNotEmpty()) {
                Glide.with(this)
                        .asGif()
                        .load(it)
                        .apply(RequestOptions()
                                .centerInside()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .error(R.drawable.ic_filter_drama))
                        .listener(object : RequestListener<GifDrawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
                                gif_progress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(resource: GifDrawable?, p1: Any?, target: com.bumptech.glide.request.target.Target<GifDrawable>?, p3: DataSource?, p4: Boolean): Boolean {
                                gif_progress.visibility = View.GONE
                                return false
                            }
                        })
                        .into(gif_image)
            } else {
                throwError()
            }
        }

        details?.username?.let {
            user_name.text = String.format(getString(R.string.user_name_formatted),
                    if (it.isNotEmpty()) {
                        it
                    } else {
                        getString(R.string.unknown)
                    })

        }

        details?.type?.let {
            type.text = String.format(getString(R.string.type_formatted),
                    if (it.isNotEmpty()) {
                        it
                    } else {
                        getString(R.string.unknown)
                    })
        }
        details?.url?.let {
            url.text = String.format(getString(R.string.url_formatted),
                    if (it.isNotEmpty()) {
                        it
                    } else {
                        getString(R.string.unknown)
                    })
            setShareIntent(it)
        }

        details?.source?.let {
            source.text = String.format(getString(R.string.source_formatted),
                    if (it.isNotEmpty()) {
                        it
                    } else {
                        getString(R.string.unknown)
                    })
        }
    }

    private fun setShareIntent(url: String) {
        share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, url)
            startActivity(Intent.createChooser(intent, "Share URL"))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when (item?.itemId) {
            android.R.id.home-> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
