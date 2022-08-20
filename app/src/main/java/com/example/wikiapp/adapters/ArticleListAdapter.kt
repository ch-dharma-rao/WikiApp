package com.example.wikiapp.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wikiapp.databinding.CardLayoutArticlesBinding
import com.example.wikiapp.model.Article
import com.example.wikiapp.view.WebViewActivity

class ArticleListAdapter(private val onArticleClicked: (Article) -> Unit) :
    ListAdapter<Article, ArticleListAdapter.ArticleViewHolder>(DiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {

        val binding =
            CardLayoutArticlesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

    }

    class ArticleViewHolder(private val binding: CardLayoutArticlesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Article) {

            binding.root.setOnClickListener {
                var intent: Intent = Intent(binding.root.context, WebViewActivity::class.java)
                intent.putExtra("url", "https://en.wikipedia.org/?curid=" + item.pageID)
                binding.root.context?.startActivity(intent)
            }

            binding.articleTitle.text = item.title// + item.imgUrl
            binding.articleDescription.text = item.description

            if (item.description.toString() == null) {
                item.description = item.title
            }
//            Log.d("DESCRIPION", item.description.toString())


            Glide.with(itemView).asDrawable().load(item.imgUrl).into(binding.articleImage)

        }

    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.pageID == newItem.pageID
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }
}