# slimming_android

一个具备集 运动记录及查看,饮食热量营养查询,健康记录(心率监测,血压输入记录等),数据图表展示,个人信息维护 一体的简易Android客户端

>心率识别参考:https://github.com/ZhaoYukai/HeartRate

>其原理是通过摄像头闪光灯发出来的光,通过闪光灯识别手指的血管，读取摄像头红色素平均值/明暗变化,绘制心率曲线,检测曲线上下脉冲,计算心率值.

若要自己搭建一个服务器环境,请将本项目对应服务端部署好,并修改本项目RequestUtil.java中的BASE_URL为对应服务器地址

### Apk release: Demo下载体验
[slimming.apk.zip](slimming.apk.zip)
###### 项目地址
* [Coding repository](https://git.coding.net/yanweijia/slimming_android.git) slimming_android
* [Github repository](https://github.com/yanweijia/slimming_android) slimming_android
* [Coding repository](https://git.coding.net/yanweijia/slimming_server.git) slimming_server
* [Github repository](https://github.com/yanweijia/slimming_server) slimming_server

###### 课程设计题目要求:
基于安卓的健康减肥软件的设计与开发
>现代手机成为人们不可或缺的一部分，而在手机中，安卓手机已经成为年轻人群的主流
 手机，而年轻人也对减肥瘦身如火如荼，健康减肥也是愈演愈烈。采用安卓平台的健康
 减肥软件就体现出了很大的优势。采用安卓移动平台，让人们随时随地进行减肥，灵活
 准确的记录减肥成果，让人们的减肥更加透明化。健康减肥软件主要以运动量的测定和
 评估为主，加以平衡膳食的健康生活方式为辅助，健康指数查询，给出了健康人的心率、
 血压、血糖等的标准指数，让大家作为健康的参考；减肥日记，用来记录每天的走路数，
 让人们清晰的知道自己消耗的卡路里；饮食搭配用来合理的搭配食物，给出饮食建议，
 知道自己将要增加多少卡路里，以免过多的摄入；健康膳食，提供了大量早中晚的食物
 以及这些食物的热量，摄入多少出现的问题等信息；运动循迹是利用 GPS 定位来标注你
 的位置和所走路线的，计算消耗的卡路里，这样让使用者更能了解自己运动的状态和位
 置。

###### 简化后要求:

- 灵活准确的**记录减肥成果**
- 健康减肥软件主要以**运动量的测定和评估**为主
    - -**减肥日记**，用来记录每天的**走路数**，消耗的**卡路里**-
    - 给出了健康**人的心率、血压、血糖**等的标准指数
    - **运动循迹**是利用GPS定位来标注你的位置和所走路线的，计算消耗的卡路里
- 加以平衡膳食的健康生活方式为辅助，**健康指数查询**
- **饮食搭配**用来合理的搭配食物，给出饮食建议，知道自己将要增加多少卡路里，以免过多的摄入
- 提供了大量早中晚的食物以及这些食物的热量，摄入多少出现的问题等信息

###### 作者介绍
| 作者  | 主页  | Email |
|:--: | :--|:--|
| 严唯嘉| [风旋碧浪@严唯嘉](http://www.yanweijia.cn) | happyboyywj#163.com|
| 乐汉| [uuppoo321](https://coding.net/u/uuppo321) |  |


## Dev & IDE : 开发工具

#### Android 端
MVP:Model View Presentation,  DataBinding技术

| 名称 | 介绍 | 网址 |
|:--|:--| :--|
|Android Studio 3.0 Beta7| 安卓开发工具| [AndroidStudio](https://developer.android.google.cn/studio/index.html)|
|网易mumu|安卓模拟器|[网易mumu](http://mumu.163.com/)|
|nox夜神|安卓模拟器|[夜神安卓模拟器](https://www.yeshen.com/)|


#### Web 端
SSM框架:Spring+SpringMVC+Mybatis+mysql

| 名称 | 介绍 | 网址 |
|:--|:--| :--|
|IDEA|java开发IDE|[IDEA官网](http://www.jetbrains.com/idea)|
|spring&springMVC|网页开发框架|[spring](http://spring.io/)|
|maven|java依赖管理|IDEA自带|
|tomcat 9.0|java web 容器|[tomcat](http://tomcat.apache.org/)|
|postman| http测试工具,Chrome App,看下方截图| 在chrome里面下载|

#### 其他工具
| 名称 | 介绍 | 网址 |
|:--|:--| :--|
|leangoo| 看板,敏捷开发,拆分任务,每日移动已完成任务|[Leangoo](http://www.leangoo.com)|

## Gradle Dependence

| 名称      |    链接   | 备注|
| :-------- | :--| :-- |
| 底部tab  | [LuseenBottomNavigation](https://github.com/armcha/LuseenBottomNavigation)|    |
| 安卓Util库    |  [blankj:AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode) |  参见 [AndroidUtilCode_CN.md](help_doc/AndroidUtilCode_CN.md) |
| jackson      |     |   |
| 圆形ImageView | [CircleImageView](https://github.com/hdodenhof/CircleImageView) | |
| 平滑加载图片的类库 | [Glide](https://muyangmin.github.io/glide-docs-cn/doc/download-setup.html) |  |
| 高德地图定位&地图api | [amap高德地图api](http://lbs.amap.com/dev) |  |
| Android 自定义图表库 | [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) |  |


Code:
``` gradle
    compile 'com.github.armcha:LuseenBottomNavigation:1.8.2'
    compile 'com.blankj:utilcode:1.9.0'
    compile 'com.fasterxml.jackson.core:jackson-databind:2.9.1'
    compile 'de.hdodenhof:circleimageview:2.1.0'
    compile 'com.github.bumptech.glide:glide:4.1.1'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.1.1'

    compile 'com.amap.api:location:3.6.1'  //定位
    compile 'com.amap.api:3dmap:5.4.0'  //导航,已包含3d地图
    compile 'com.amap.api:search:5.3.1'   //搜索
    compile 'com.github.PhilJay:MPAndroidChart:v3.0.2'
```

## Ref:项目用到的技术/参考文档

#### DataBinding 技术
* [ DataBinding快速入门 ](http://blog.csdn.net/dxplay120/article/details/52036448)
* [ DataBinding-是时候放弃butterknife了 ](http://blog.csdn.net/dxplay120/article/details/52036448)
* [**Android MVVM框架 - Data Binding Library介绍**](https://github.com/derron/DataBinding-album-sample/blob/master/MVVM.md)
* [DataBinding 中 BindingConversion 的使用](http://blog.csdn.net/zhuhai__yizhi/article/details/52924196)
* [Android dataBinding与ListView及事件详解](http://www.jb51.net/article/95064.htm)
* [databinding中的ImageView与Glide结合使用](http://blog.csdn.net/zhuhai__yizhi/article/details/52922092)

#### Fragment介绍
* [Android Fragment 真正的完全解析（上）](http://blog.csdn.net/lmj623565791/article/details/37970961/)
* [**Fragment详解之六——如何监听fragment中的回退事件与怎样保存fragment状态**](http://blog.csdn.net/harvic880925/article/details/45013501)

#### BottomNavBar
* [LuseenBottomNavigation - Github](https://github.com/armcha/LuseenBottomNavigation)

#### SwipeRefreshLayout
* [SwipeRefreshLayout详解和自定义上拉加载更多](http://www.jianshu.com/p/d23b42b6360b)

#### 资源
* [IconFont 阿里巴巴矢量图标库](http://www.iconfont.cn/)

#### 地图定位
* [高德地图定位SDK](http://lbs.amap.com/api/android-location-sdk/guide/android-location/getlocation)


#### 异常处理参考文档

* [ADB Install Fails With INSTALL_FAILED_TEST_ONLY - Stack overFlow](https://stackoverflow.com/questions/25274296/adb-install-fails-with-install-failed-test-only)
* [AndroidStudio完美解决"Activity使用Handler时出现警告信息"的问题 - CSDN](http://blog.csdn.net/nzfxx/article/details/51854305)
* [【已解决】Android Studio中的gradle无法引用HttpClient库](http://blog.csdn.net/u011726984/article/details/48528573)
* [我的Android进阶之旅------>解决：Execution failed for task ':app:transformResourcesWithMergeJavaResForDebug'.](http://blog.csdn.net/ouyang_peng/article/details/50538658)
* [jackson下划线和驼峰命名法自动忽略](http://blog.csdn.net/z69183787/article/details/51279603)
* [Android------startActivityForResult的详细用法](http://blog.csdn.net/sunchaoenter/article/details/6612039)
* [Android:attempt to re-open an already-closed object](http://www.cnblogs.com/wanqieddy/p/3503136.html)
* [Android实现圆形Imageview，带白色边框](http://blog.csdn.net/a_ycmbc/article/details/51373211)
* [stackOverFlow - Warning:Android custom view should extend AppCompatTextView](https://stackoverflow.com/questions/43246336/android-custom-view-should-extend-appcompattextview)
* [Android报错：No package identifier when getting value for resource number 0x00000003](http://www.jianshu.com/p/b4df9b81c1e0)
* [笔记12--GridView--且解决与ScrollView共用的冲突问题](http://blog.csdn.net/mwj_88/article/details/23094399)
* [ImageView 简单加载网络图片实例代码](http://www.jb51.net/article/38825.htm)
* [Android 背景渐变色(shape,gradient)](http://l62s.iteye.com/blog/1659433)
* [Android 开发学习之路 - Android6.0运行时权限](http://www.cnblogs.com/Fndroid/p/5542526.html)
* [Jackson 将 json 字符串转换成泛型List](http://www.cnblogs.com/quanyongan/archive/2013/04/16/3024993.html)
* [Android 分享 - 调用系统自带分享功能](http://www.jianshu.com/p/0a0e2258b3d6)
* [android 调用系统图库查看指定路径的图片](http://blog.csdn.net/lyl278401555/article/details/12840925)

## ScreenShot: Postman Intro
![Postman image](screenshot/postman.png)

## ScreenShot: 截图演示

![ScreenShot](screenshot/IMG_1354.JPG)

![ScreenShot](screenshot/IMG_1355.JPG)

![ScreenShot](screenshot/IMG_1356.JPG)

![ScreenShot](screenshot/IMG_1357.JPG)

![ScreenShot](screenshot/IMG_1358.JPG)

![ScreenShot](screenshot/IMG_1359.JPG)

![ScreenShot](screenshot/IMG_1360.JPG)

![ScreenShot](screenshot/IMG_1361.JPG)

![ScreenShot](screenshot/IMG_1362.JPG)

![ScreenShot](screenshot/IMG_1363.JPG)

![ScreenShot](screenshot/IMG_1364.JPG)

![ScreenShot](screenshot/IMG_1365.JPG)

![ScreenShot](screenshot/IMG_1366.JPG)

![ScreenShot](screenshot/IMG_1367.JPG)

![ScreenShot](screenshot/IMG_1368.JPG)

![ScreenShot](screenshot/IMG_1369.JPG)

![ScreenShot](screenshot/IMG_1370.JPG)

![ScreenShot](screenshot/IMG_1371.JPG)

![ScreenShot](screenshot/IMG_1372.JPG)

![ScreenShot](screenshot/IMG_1373.JPG)

![ScreenShot](screenshot/IMG_1374.JPG)

![ScreenShot](screenshot/IMG_1375.JPG)

![ScreenShot](screenshot/IMG_1376.JPG)

![ScreenShot](screenshot/IMG_1377.JPG)

![ScreenShot](screenshot/IMG_1378.JPG)

![ScreenShot](screenshot/IMG_1379.JPG)

![ScreenShot](screenshot/IMG_1340.png)

![ScreenShot](screenshot/IMG_1341.png)

![ScreenShot](screenshot/IMG_1350.png)
