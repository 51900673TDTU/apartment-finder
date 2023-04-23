package com.application.housefinder.appartment.unit

data class Post(
    var id: Int,
    var owner: String,
    var title: String,
    var description: String,
    var imgURLs: List<String>
)