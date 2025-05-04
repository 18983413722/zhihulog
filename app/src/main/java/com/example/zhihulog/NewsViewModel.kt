package com.example.zhihulog
import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NewsViewModel: ViewModel() {
    //val repository  = Repository()
    private val apiService = RetrofitClient.apiService

    private val _currentDate = MutableLiveData<String>(20250503.toString())
    val currentDate :LiveData<String> = _currentDate

    private val _bannerItems = MutableLiveData<List<BannerItem>>()
    val bannerItems: LiveData<List<BannerItem>> = _bannerItems

    private val _stories = MutableLiveData<List<StoryItem>>()
    val stories: LiveData<List<StoryItem>> = _stories

    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    @SuppressLint("SuspiciousIndentation")
    fun loadData(date: String,isFirstLoad : Boolean = false) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = if(isFirstLoad){
                    apiService.getLatestNews()
                } else {
                    apiService.getNewsBefore(date)
                }
                Log.d("NETWORK", "数据返回：${response.stories?.size}条常规故事 + ${response.top_stories?.size}条置顶故事")
                Log.d("NETWORK", "开始转换")
                val bannerItems = response.top_stories?.map {
                    BannerItem(
                        id = it.id,
                        title = it.title,
                        imageUrl = it.image,
                        url = it.url,
                        hint = it.hint
                    )
                }?: emptyList()
                val storyItems = response.stories?.map {
                    StoryItem(
                        id = it.id,
                        title = it.title,
                        hint = it.hint,
                        imageUrl = it.images?.firstOrNull(),
                        url = it.url
                    )
                }?: emptyList()
                withContext(Dispatchers.Main) {
                    if(date!= 20250503.toString()){
                        val currentList = _stories.value?.toMutableList()?: mutableListOf()
                            currentList.addAll(storyItems)
                        _stories.value = currentList
                    }else{
                        _stories.value = storyItems
                    }
                    _bannerItems.value = bannerItems
                    _isLoading.value = false
                }
                Log.d("NETWORK", "数据加载完成：${_stories?.value?.size}条")
                Log.d("NETWORK", "数据加载完成：${_bannerItems?.value?.size}条")
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = "加载失败: ${e.message}"
                    _isLoading.value = false
                }
            }
        }
    }

    fun decreaseDate() {
        _currentDate.value?.let { dateStr ->
            val date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).parse(dateStr)
            val calendar = Calendar.getInstance().apply { time = date!! }
            calendar.add(Calendar.DAY_OF_YEAR, -1) // 减1天
            _currentDate.value = SimpleDateFormat("yyyyMMdd").format(calendar.time)
        }
    }
}