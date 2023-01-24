package com.example.githubapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RepositoryRecyclerAdapter internal constructor(
    private val reposList: List<Repository>,
) : RecyclerView.Adapter<RepositoryRecyclerAdapter.MyViewHolder>(){

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvCreatedAt: TextView = itemView.findViewById(R.id.tvCreatedAt)
        val tvUpdatedAt: TextView = itemView.findViewById(R.id.tvUpdatedAt)
        val tvLanguage: TextView = itemView.findViewById(R.id.tvLanguage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).
        inflate(R.layout.recyclerview_repository_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val repos: Repository = reposList[position]
        holder.tvName.text = repos.name
        holder.tvLanguage.text = " (" + repos.language + ")"
        holder.tvCreatedAt.text = repos.created_at!!.replace("T", " (").replace("Z", ")")
        holder.tvUpdatedAt.text = repos.updated_at!!.replace("T", " (").replace("Z", ")")
    }

    override fun getItemCount() = reposList.size
}