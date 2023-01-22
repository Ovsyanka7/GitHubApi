package com.example.githubapi

data class Repository(
    var name: String? = null,
    var html_url: String? = null,
    var created_at: String? = null,
    var updated_at: String? = null,
    var pushed_at: String? = null,
    var language: String? = null,
    var commits_url: String? = null,
    var commitList: MutableList<Commit> = mutableListOf(),
)