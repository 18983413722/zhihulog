package com.example.zhihulog

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ZhihuDailyResponse(
    val date: String,
    val stories: List<Story>? = emptyList(),
    val top_stories: List<TopStory>? = emptyList()
)

data class Story(
    val id: Int,
    val title: String,
    val hint: String,
    val url: String,
    val images: List<String>?,
    val ga_prefix: String
)

data class TopStory(
    val id: Int,
    val title: String,
    val hint: String,
    val url: String,
    val image: String
)

data class BannerItem(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val url: String,
    val hint: String
)

@Parcelize
data class StoryItem(
    val id: Int,
    val title: String,
    val hint: String,
    val imageUrl: String?,
    val url: String
) : Parcelable

