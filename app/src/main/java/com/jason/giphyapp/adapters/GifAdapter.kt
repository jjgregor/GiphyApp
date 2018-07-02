package com.jason.giphyapp.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.jason.giphyapp.R
import com.jason.giphyapp.activities.DetailsActivity
import com.jason.giphyapp.models.Gif
import kotlinx.android.synthetic.main.recycler_view_item.view.*

class GifAdapter(val gifs: ArrayList<Gif>) : RecyclerView.Adapter<GifAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(gifs[position])
    }

    override fun getItemCount() = gifs.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItem(item: Gif) {
            with(itemView) {
                Glide.with(context)
                        .asGif()
                        .load(item.images.fixed_height.url)
                        .apply(RequestOptions()
                                .centerCrop()
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
                        .into(gif)

                setOnClickListener { context.startActivity(Intent(context, DetailsActivity::class.java).putExtra(DETAIL_EXTRA, item)) }
            }
        }
    }

    companion object {
        const val DETAIL_EXTRA = "detail-extra"
    }

}