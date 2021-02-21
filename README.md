# NetDepression
##介绍：
<br/>由于做的是网抑云而不是网易云，所以本着让用户感到抑郁的开发宗旨，功能少到令人发指（狗头）<br/>
##功能：
<br/>1.仅支持密码登录，登录后用户就可以看到自己美丽的头像和美丽的背景图
<br/>2.仅后两个碎片中显示的歌单支持点击
<br/>3.歌单里展示出来的歌，只要官方能放这边就也可以听
<br/>4.时间实在来不及，其他功能后续会补<br/>
##优点：
<br/>只有UI。着实花了一番心思，xml一直在改<br/>
##缺点：
<br/>这也太多了，这么拉挎的app的缺点讲一天都讲不完，不仅功能少，bug还多<br/>
##设计思路：
<br/>关于数据展示：主界面三个碎片中的数据展示使用MVP架构，由fragment通知presenter，再由presenter调用model中的网络请求和json解析方法
<br/>关于音乐播放：PlayActivity作为前台，通过自定义binder控制MusicService，MediaPlayer相关的操作都在服务中完成
<br/>关于用户界面：总之就是一定要好看，不改好看就不想往下做
