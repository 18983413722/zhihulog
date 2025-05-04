package com.example.zhihulog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: NewsViewModel
    private lateinit var adapter1: Newsadapter
    private lateinit var adapter2: BannerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout
    private lateinit var vp2: androidx.viewpager2.widget.ViewPager2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        recyclerView = findViewById(R.id.recyclerView)
        
        adapter1 = Newsadapter()

        recyclerView.adapter = adapter1
        adapter1.onItewClickListener = { storyitem ,indix ->
            val currentPosition = indix
            startDetailActivity(storyitem,currentPosition)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.loadData(20250504.toString(),true)

        viewModel.currentDate.observe(this){ date->
            Log.d("DATE","${date}")
            viewModel.loadData(date)
        }

        vp2 = findViewById(R.id.bannerViewPager)
        adapter2 = BannerAdapter()
        vp2.adapter = adapter2
        var i= 1
        viewModel.bannerItems.observe(this){ bannerItems->
            bannerItems?.let {
                if(i==1){
                    adapter2.submitList(bannerItems)
                    i++
                }
            }
        }

        swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        swipeRefreshLayout.setOnRefreshListener{
            viewModel.decreaseDate()
            swipeRefreshLayout.isRefreshing = false
        }
        viewModel.stories.observe(this){ story->
            story?.let {
                adapter1.submitList(story)
            }
        }

        viewModel.error.observe(this) { error ->
            Log.e("MainActivity", "Error: $error")
        }
        viewModel.isLoading.observe(this) { isLoading ->
            swipeRefreshLayout.isRefreshing = isLoading
        }

    }

    private fun handleNewsList(): List<StoryItem> {
        return viewModel.stories.value ?: emptyList()
    }

    fun startDetailActivity(storyitem: StoryItem, indix: Int) {
        val newsList = handleNewsList()
        val intent = Intent(this, StoryItemDetailActivity::class.java).apply {
            putExtra("STORY_ID", storyitem.id)
            putExtra("STORY_URL", storyitem.url)
            putExtra("STORY_INDIX",indix)
            putParcelableArrayListExtra("NEWS_LIST", ArrayList(newsList))
        }
        startActivity(intent)
    }

}

