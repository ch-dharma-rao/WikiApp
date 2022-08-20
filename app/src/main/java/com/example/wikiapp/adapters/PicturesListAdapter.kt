package com.example.wikiapp.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wikiapp.databinding.CardLayoutPicturesBinding
import com.example.wikiapp.model.ImageDetails
import com.example.wikiapp.view.WebViewActivity

class PicturesListAdapter :
    ListAdapter<ImageDetails, PicturesListAdapter.PicturesViewHolder>(PicturesListAdapter.DiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PicturesListAdapter.PicturesViewHolder {
        val binding =
            CardLayoutPicturesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PicturesListAdapter.PicturesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PicturesListAdapter.PicturesViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

    }

    class PicturesViewHolder(private val binding: CardLayoutPicturesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ImageDetails) {
            var url = "https://commons.wikimedia.org/wiki/Special:FilePath/" + item.title.toString()
            var description = "https://commons.wikimedia.org/wiki/File:" +item.title
            var title = item.title

            if (title.contains(".jpg") || title.contains(".JPG"))
                title.replace(".jpg", "")


            binding.root.setOnClickListener {
                var intent: Intent = Intent(binding.root.context, WebViewActivity::class.java)
                intent.putExtra("url", description)
                binding.root.context?.startActivity(intent)
            }

//            binding.imageTitle.text = item.title.toString().replace
            binding.imageTitle.text = item.title.toString()
            Log.d("IMAGE TITLE", item.title.toString())


            Glide.with(itemView).asDrawable().load(url).into(binding.pictureImage)

        }

    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<ImageDetails>() {
        override fun areItemsTheSame(oldItem: ImageDetails, newItem: ImageDetails): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ImageDetails, newItem: ImageDetails): Boolean {
            return oldItem == newItem
        }

    }


}