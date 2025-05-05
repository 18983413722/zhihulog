1.该APP是仿写知乎日报的一个app，
  主要功能有：
    （1）实现顶部banner
    （2）实现点击新闻事件可查看新闻详情
    （3）在查看新闻详情界面时，左右滑动可以切换到上一篇新闻或下一篇新闻
    （4）实现上拉刷新，向上滑动时可加载往日新闻，并添加到列表中
  功能实现思路：
    （1）banner使用官方库的vp2，通过adapter将网络请求来的数据绑定至视图
    （2）在adapter实现点击事件，并将被点击xinwen的item回调主页面，主页面跳转页面，使用webView加载详情
    （3）在（2）的基础上，主页面跳转时传入数据该item在整个新闻列表的位置以及传入整个新闻列表，通过滑动检测来判断是加载前一个新闻呢还是后一个新闻
    （4）通过swipeRefreshLayout来监听上滑事件，上滑时获取新加载的数据，并添加到新闻列表中，由List的LiveData监测并刷新页面
2.GIF展示：

  ![ca7b91ae9c23e89315cc7b1fd176d9c9](https://github.com/user-attachments/assets/62268d39-fdcc-4c39-ae82-0ec0b038467b)

  ![35b8942fef329bf3e43eb4cc86952a70](https://github.com/user-attachments/assets/badbf46b-17be-4cdd-a82c-f198e96698e2)

  ![2a3bc66ae08de0a98d9bd6ca2937d7e4](https://github.com/user-attachments/assets/aa5a3377-77e4-4540-80dc-ddab855e1197)
3.技术亮点：
  在实现左右滑动切换新闻时，整个新闻列表始终无法传入另一个界面，后了解到，Android 的跨进程通信机制只能传输基本数据类型或实现了序列化接口的对象，而我的自定义列表类只有通过Parcelable注解才能进行传递
4.心得体会：
  实践少了，很多板块学的时候没问题，用起来才发现理解不透彻，对Ui板块生疏，ui中的很多功能都未了解
5.待提升：
  画面僵硬，动画很少，UI质量不高，很多预期中的功能没有实现
