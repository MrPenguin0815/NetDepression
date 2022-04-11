# 红岩网校寒假考核——基于网易API的音乐APP网抑云

## 技术要点

**MVP架构**

MVP 架构可以深层次地解耦视图、业务逻辑、数据源三者的关系，让它们之间的相互依赖性大大降低。

<img src="app/mvp图解.svg"  />

使用MVP架构后访问网络得到数据并显示出来的流程：

1. Activity 启动时，告诉 Presenter 我需要数据
2. Presenter 访问数据接口得到原数据并交给Model
3. Model 一看数据不规范，赶紧处理一下，处理完成再还给 Presenter 
4. Presenter 把处理后的数据汇报给 Activity（View），View 拿到数据就做显示的操作，工作结束

**自定义控件**

流式文字布局 的设计思路：

<img src="app/流式布局图解.svg"  />

其中Line类的`public void layout()`方法较为复杂。当一行中剩余空间较多时，应适当调宽词间距来使整体效果更美观。具体实现为，把 原本宽度+每个文本框平均能分到多少剩余宽度 设置为一个子控件可占的最大宽度，并为其打包一个新的`MeasureSpec`对象。生成`MeasureSpec`对象时使用`AT_MOST`模式来增加适配度。

波纹图像 的设计思路：

单个波纹的呈现：使用安卓自带的Animation库创建一个包含`ScaleAnimation`和`AlphaAnimation`的动画集，同时对白色圆环矢量图采取放大和变淡效果。

多个波纹的循环：把三个相同的动画存进数组，使用一个handler对象每隔一段时间发送开始动画的消息，关键方法是`sendEmptyMessageDelayed()`，这样数组中的动画就可以在固定时差下轮流循环播放。

**异步消息处理**

<img src="app/异步消息处理图解.svg"  />

本项目中两处可以体现安卓的异步消息处理机制。

音乐的后台播放服务：设置负责音乐播放的子线程中每500毫秒，就会对当前音乐播放信息打包进一个`Bundle` 对象，通过`Message`发送到主线程，主线程中根据接收的消息来刷新进度条和相关时间的显示。

网络请求得到响应后的UI修改：由于安卓不允许在子线程中更改界面显示，所以每次接收到返回的数据后要回调`runOnUiThread(new Runnable() {})`创建匿名新线程，其实就是返回主线程，然后在`run()`方法体内做具体的控件内容更新。

**关于interface的更多技巧**

在给各种可滑动可回收布局写适配器时，从处理点击事件中意识到一些trick。适配器往往在创建时需要传入相关数据对象的列表，一个数据对象对应一个子项item。而很多时候点击子项后需要进行活动的跳转，该操作需要当前活动运行的语境`context`。同一个适配器类是可以用在很多不同活动中的，所以显然不可能在适配器内部进行点击事件的处理。解决方法是，让`Activity`实现某个特定listener接口，让item持有这个listener的一个引用，然后在活动里面重写接口里的抽象方法，实现具体的响应处理。

<img src="app/interface使用技巧.svg"  />

不仅仅是在处理点击问题时，同一个`Activity`只能有一个对象，而很多时候其他的类都需要获取自己所在活动的信息，又不能直接持有的引用，就 ‘拔高‘ 所谓`Activity`的 ’概念’ ，通过实现某个`Interface`把它进化成某种可以被引用的、更高级、更抽象的概念，这样就可以被其他对象远程遥控了。

## 界面概述与展示

**启动界面**(`LaunchActivity`)：为了实现登录和注册按键的背景自定义，控件不使用button，而是文本框叠加图片并给文本框添加监听。

使用`SharedPreferences`进行登录状态的存取。

<img src="app/启动界面.jpg" width="300px" />

**登录界面**(`PasswordLoginActivity`)：通过监听password文本框是否已被编辑决定按键颜色。

使用HTTP网络请求加接口回调检查手机号是否注册和输入密码是否正确。本项目用工具类`HttpUtil`封装了网络请求的参数设置和子线程的创建。

<img src="app/登录界面（未输入）.jpg"  width="300px" />

**主界面**(`HomeActivity`)：

`ViewPager`加碎片实现三个分区的具体内容，底部使用`TabLayout`提示分区名称。设置两者联动，不论滑动上方还是点击下方都可以实现切换。

发现 分区：

发现分区由顶部搜索框、轮播图、快速入口、专属场景歌单、歌曲推荐、音乐日历、视频合辑推荐等模块构成，按照不同block从服务器上拉取数据，呈现布局为主流的`CardView`+`RecylerView`。

点击左上角的图标可以再一次进行网络请求，达到刷新的效果。点击文本框可以进入搜索界面。

<img src="app/发现界面数据展示.jpg" alt="d" width="300px" />

热门 分区：

顶部设计了自动收缩标题栏。数据为从服务器中获取当前最热门的的歌曲门类和话题。话题的内容较多，这里设计为仅展示标题、概要和文章引用的前三张图片。所有歌单都可以点击并进入详情，并播放相关歌曲。

<img src="热门界面数据展示.jpg"  width="300px" />

我的 分区：

头像背景采用自定义控件`WaveImgeView`，在压暗的用户自定义背景上效果会更加突出。下面用`ScollView`展示了收藏歌单，滑动到最底部可以退出登录。

<img src="app/个人界面数据展示.gif"  width="300px" />

**搜索界面**(`SearchActivity`)：

用户在可编辑文本框输入并点击搜索按钮后，根据关键词配置网络请求的url，并用`RecyclerView`呈现出搜索结果。当用户点击加载更多时，保存原有网络请求中的关键词key，修改访问数据的页数key，由此获取与上一轮不同的搜索结果，此时只需要更改传给适配器的数据并调用方法`notifyDataSetChanged()`更新视图即可。

<img src="app/搜索界面.jpg"  width="500px" />

**歌单详情界面**(`DetailActivity`)：

顶部背景使用的高斯模糊直接采纳了网上的方案，自己并不理解其中关于图像处理的原理。

每个歌曲条目中的菱形图示是通过在原图上覆盖一层四角为白，中间透明的蒙版实现的。

<img src="app/歌单详情界面.jpg" width="300px" />

**音乐播放界面**(`PLayActivity`)：

服务中持有一个媒体播放器`MediaPlayer`和计时器`Timer`。前者解析网络音乐资源，后者进行播放时间的管理。

为了使在播放界面上对于按钮的点击可以控制服务中播放器的种种操作，服务里面建立内部类`MusicControl`继承自`Binder`，再让播放活动与该类的一个实例捆绑。

<img src="app/音乐正常播放.jpg" width="300px" />

