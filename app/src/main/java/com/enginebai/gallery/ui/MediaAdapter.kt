package com.enginebai.gallery.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.enginebai.gallery.R
import com.enginebai.gallery.model.Media
import kotlinx.android.synthetic.main.item_media.view.*

class MediaAdapter(private val clickListener: (Media) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val mediaList = mutableListOf<Media>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MediaViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_media, parent, false))
    }

    override fun getItemCount() = mediaList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val media = mediaList[position]
        (viewHolder as MediaViewHolder).apply {
            bind(media)
            itemView.setOnClickListener {
                clickListener(media)
            }
        }
    }
}

class MediaViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

    fun bind(media: Media) {
        Glide.with(view)
            .load(media.path)
            .into(view.imageMedia)
    }
}