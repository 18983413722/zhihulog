package com.example.zhihulog

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.lang.Math.abs

class StoryItemDetailActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private var currentIndex = 0
    private lateinit var newsList: List<StoryItem>
    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_story_item_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mian)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val storyid = intent.getIntExtra("STORY_ID",-1)
        val storyurl = intent.getStringExtra("STORY_URL")
        currentIndex = intent.getIntExtra("CURRENT_INDEX", 0)
        newsList = intent.getParcelableArrayListExtra("NEWS_LIST") ?: emptyList()
        webView = findViewById(R.id.webView)
        webView.apply {
            settings.javaScriptEnabled = true
            loadUrl(storyurl?:"https://daily.zhihu.com")
        }
        webView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            false
        }
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            // 设置滑动最小距离和速度
            private val SWIPE_THRESHOLD = 100  // dp
            private val SWIPE_VELOCITY_THRESHOLD = 100  // dp

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                val deltaX = e2.x - (e1?.x ?: return false)
                val deltaXAbs = abs(deltaX)
                val deltaYAbs = abs(e2.y - (e1?.y ?: return false))

                // 判断横向滑动是否满足条件
                if (deltaXAbs > SWIPE_THRESHOLD && deltaXAbs > deltaYAbs) {
                    if (deltaX > 0) {
                        // 向右滑动 → 加载上一篇
                        loadPreviousStory()
                    } else {
                        // 向左滑动 ← 加载下一篇
                        loadNextStory()
                    }
                    return true
                }
                return false
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    private fun loadNextStory() {
        if (currentIndex < newsList.size - 1) {
            currentIndex++
            updateWebViewContent()
        } else {
            Toast.makeText(this, "已经是最后一篇", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadPreviousStory() {
        if (currentIndex > 0) {
            currentIndex--
            updateWebViewContent()
        } else {
            Toast.makeText(this, "已经是第一篇", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateWebViewContent() {
        val currentStory = newsList[currentIndex]
        webView.loadUrl(currentStory.url)
    }

    fun onBackPressed(view: View) {
        finish()
    }
}

