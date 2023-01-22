package com.example.githubapi

data class UserInfo(
    var login: String? = null,
    var id: String? = null,
    var avatar_url: String? = null,
    var html_url: String? = null,
    var repos_url: String? = null,
    var repositories: MutableList<Repository>? = null,
    )