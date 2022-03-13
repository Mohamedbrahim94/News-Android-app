package com.example.newsapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapplication.api.ArticlesItem
import com.example.newsapplication.api.SourcesItem

class ArticlesAdapter : RecyclerView.Adapter<ArticlesAdapter.articlesViewHolder>() {

    var data: List<ArticlesItem>? = null

    class articlesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val articleDate: TextView = itemView.findViewById(R.id.articleDateTextView)
        val articleTitle: TextView = itemView.findViewById(R.id.articleTitleTextView)
        val articleDescription: TextView = itemView.findViewById(R.id.articleDescriptionTextView)
        val articleImage: ImageView = itemView.findViewById(R.id.articleImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): articlesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_article, parent, false
        )
        return articlesViewHolder(view)
    }

    override fun onBindViewHolder(holder: articlesViewHolder, position: Int) {
        var article = data?.get(position)
        holder.articleDate.setText(article?.publishedAt) // dd/mm/yy hh:mm
        holder.articleDescription.setText(article?.description)
        holder.articleTitle.setText(article?.title)
        Glide.with(holder.itemView).load(article?.urlToImage).into(holder.articleImage)

    }

    override fun getItemCount(): Int {
        return data?.size ?:0
    }
    fun getData(commingData:List<ArticlesItem>?){
        data = commingData
        notifyDataSetChanged()
    }
}